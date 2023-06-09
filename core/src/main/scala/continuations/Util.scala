package continuations

extension [A](either: Either[Throwable, A]) {
  inline def rethrow: A = either match {
    case Right(a) => a
    case Left(e) => throw e
  }

  inline def void: Unit = either match {
    case Right(_) => ()
    case Left(e) => throw e
  }
}
