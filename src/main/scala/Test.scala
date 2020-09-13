
import cats.effect.{ContextShift, IO}
import cats.implicits._

import scala.concurrent.ExecutionContext.global

object CatsIOParallelApp extends App {
  import scala.concurrent.ExecutionContext.Implicits.global
  private implicit val cs: ContextShift[IO] = IO.contextShift(global)

  def printThreadId(msg: String) =
    println(s"${Thread.currentThread.getId} : $msg")

  def io(x: String) = IO {
    println(s"starting $x")
    printThreadId(x)
    Thread.sleep(100)
    println(s"finishing $x")
    "XX " + x + " XX"
  }

  printThreadId("main start")

  val i1 = IO.shift *> io("one")
  val i2 = IO.shift *> io("two")
  val i3 = IO.shift *> io("three")

  val result = List(i1, i2, i3).parSequence

  printThreadId("main wait")
  println(result.unsafeRunSync())
  printThreadId("main end")
}
