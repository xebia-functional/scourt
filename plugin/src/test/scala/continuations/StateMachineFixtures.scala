package continuations

import scala.io.Source

trait StateMachineFixtures {

  def loadFile(file: String): String = Source.fromResource(s"$file.scala").mkString

  val expectedOneSuspendContinuation =
    loadFile("OneSuspendContinuation")

  val expectedOneSuspendContinuationTwoBlocks =
    loadFile("OneSuspendContinuationTwoBlocks")

  val expectedOneSuspendContinuationThreeBlocks =
    loadFile("OneSuspendContinuationThreeBlocks")

  val expectedOneSuspendContinuationFourBlocks =
    loadFile("OneSuspendContinuationFourBlocks")

}
