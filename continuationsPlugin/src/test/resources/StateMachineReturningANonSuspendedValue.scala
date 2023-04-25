package continuations {
  import continuations.jvm.internal.SuspendApp
  final lazy module val compileFromString$package: 
    continuations.compileFromString$package
   = new continuations.compileFromString$package()
  @SourceFile("compileFromString.scala") final module class 
    compileFromString$package
  () extends Object() { this: continuations.compileFromString$package.type =>
    private def writeReplace(): AnyRef = 
      new scala.runtime.ModuleSerializationProxy(classOf[continuations.compileFromString$package.type])
    def program: Any =
      {
        private class $foo$Frame($completion: continuations.Continuation[Any | Null]) extends continuations.jvm.internal.ContinuationImpl($completion
          ,
        $completion.context) {
          var $result: Either[Throwable, Any | Null | continuations.Continuation.State.Suspended.type] = _
          var $label: Int = _
          def $result_=(x$0: Either[Throwable, Any | Null | (continuations.Continuation.State.Suspended : continuations.Continuation.State)]): Unit
             = 
          ()
          def $label_=(x$0: Int): Unit = ()
          protected override def invokeSuspend(
            result: Either[Throwable, Any | Null | (continuations.Continuation.State.Suspended : continuations.Continuation.State)]
          ): Any | Null = 
            {
              this.$result = result
              this.$label = this.$label.|(scala.Int.MinValue)
              foo(this)
            }
          override def create(value: Any | Null, completion: continuations.Continuation[Any | Null]): continuations.Continuation[Unit] =
            new continuations.jvm.internal.BaseContinuationImpl(completion)
        }
        def foo(completion: continuations.Continuation[Int]): 
          Int | Null | (continuations.Continuation.State.Suspended : continuations.Continuation.State)
         = 
          {
            {
              val $continuation: $foo$Frame =
                completion match 
                  {
                    case x$0 @ x$0:$foo$Frame if x$0.$label.&(scala.Int.MinValue).!=(0) =>
                      x$0.$label = x$0.$label.-(scala.Int.MinValue)
                      x$0
                    case _ => new $foo$Frame(completion)
                  }
              $continuation.$label match 
                {
                  case 0 => 
                    continuations.Continuation.checkResult($continuation.$result)
                    println("Start")
                    $continuation.$label = 1
                    val safeContinuation: continuations.SafeContinuation[Int] = continuations.SafeContinuation.init[Int]($continuation)
                    {
                      {
                        safeContinuation.resume(1)
                      }
                    }
                    safeContinuation.getOrThrow() match
                      {
                        case continuations.Continuation.State.Suspended => return continuations.Continuation.State.Suspended
                        case orThrow @ <empty> => return[label1] ()
                      }
                  case 1 => 
                    continuations.Continuation.checkResult($continuation.$result)
                    label1[Unit]: <empty>
                    val x: String = "World"
                    println("Hello")
                    println(x)
                    $continuation.$label = 2
                    val safeContinuation: continuations.SafeContinuation[Int] = continuations.SafeContinuation.init[Int]($continuation)
                    {
                      {
                        safeContinuation.resume(2)
                      }
                    }
                    safeContinuation.getOrThrow() match
                      {
                        case continuations.Continuation.State.Suspended => return continuations.Continuation.State.Suspended
                        case orThrow @ <empty> => ()
                      }
                  case 2 => continuations.Continuation.checkResult($continuation.$result)
                  case _ => throw new IllegalArgumentException("call to \'resume\' before \'invoke\' with coroutine")
                }
            }
            println("End")
            10
          }
        continuations.jvm.internal.SuspendApp.apply(
          {
            private final class $anon() extends continuations.jvm.internal.Starter {
              override def invoke[A](completion: continuations.Continuation[A]): A | Any | Null = foo(completion)
            }
            new continuations.jvm.internal.Starter {...}
          }
        )
      }
  }
}

