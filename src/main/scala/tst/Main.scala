package tst

import kamon.Kamon
import kamon.datadog.DatadogAgentReporter

import scala.concurrent.Await
import scala.concurrent.duration._
import scala.io.StdIn

object Main extends App {

  Kamon.addReporter(new DatadogAgentReporter())

  while (true) {
    for (i <- 1 to 10) {
      Kamon.counter("dragisa.test.counter").increment()
      Kamon.histogram("dragisa.test.histogram").record(i % 3)
      Thread.sleep(10)
    }

    println("Enter for next")

    StdIn.readLine()
  }

  sys.addShutdownHook {
    println("\nBye!\n")
    Await.result(Kamon.stopAllReporters(), 10.seconds)
  }

}
