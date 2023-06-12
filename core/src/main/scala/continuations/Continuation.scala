package continuations

import scala.concurrent.ExecutionContext

trait Continuation[-A]:
  type Ctx <: Tuple
  val executionContext: ExecutionContext
  def context: Ctx
  def resume(value: A): Unit
  def raise(error: Throwable): Unit
  def contextService[T](): T | Null =
    context.toList.find(_.isInstanceOf[T]).map(_.asInstanceOf[T]).orNull

object Continuation:
  enum State:
    case Suspended, Undecided, Resumed

  type Result = Either[Throwable, Any | Null | State.Suspended.type]

  def checkResult(result: Result): Unit =
    if (result ne null) result.void

end Continuation

inline def BuildContinuation[T](
    ec: ExecutionContext,
    onRaise: Throwable => Unit,
    onResume: T => Unit): Continuation[T] =
  new Continuation[T]:
    override type Ctx = EmptyTuple
    override val executionContext: ExecutionContext = ec

    override def context: Ctx = EmptyTuple

    override def resume(value: T): Unit = ec.execute {
      new Runnable:
        override def run(): Unit = onResume(value)
    }

    override def raise(error: Throwable): Unit = ec.execute {
      new Runnable:
        override def run(): Unit = onRaise(error)
    }

    private val ec = ExecutionContext.global

abstract class RestrictedContinuation(
    completion: Continuation[Any | Null] | Null
) extends BaseContinuationImpl(completion):

  if (completion != null)
    require(completion.context == EmptyTuple)

  override type Ctx = EmptyTuple
  override val context: EmptyTuple = EmptyTuple
