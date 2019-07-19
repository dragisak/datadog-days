name := "datadog-days"
version := "0.1"
scalaVersion := "2.12.8"

val kamonCoreVersion = "1.1.6"
val kamonDatadogVersion = "1.0.0"

libraryDependencies ++= List(
  "io.kamon" %% "kamon-core" % kamonCoreVersion,
  "io.kamon" %% "kamon-datadog" % kamonDatadogVersion
)
