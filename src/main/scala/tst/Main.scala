package tst

import kamon.Kamon
import kamon.datadog.DatadogAgentReporter

import scala.concurrent.Await
import scala.concurrent.duration._
import scala.io.StdIn

object Main extends App {

  Kamon.addReporter(new DatadogAgentReporter())

  for (i <- 1 to 10) {
    Kamon.counter("test.counter").increment()
    Thread.sleep(10)
  }

  println("Pres ENTER to stop")

  StdIn.readLine()

  println("\nBye!\n")
  Await.result(Kamon.stopAllReporters(), 10.seconds)

}
