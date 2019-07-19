package tst

import com.typesafe.config.Config
import kamon.Kamon
import kamon.datadog.DatadogAgentReporter.MeasurementFormatter
import kamon.util.EnvironmentTagBuilder

class Formatter(config: Config) extends MeasurementFormatter {

  private val tagFilterKey = config.getString("filter-config-key")
  private val filter       = Kamon.filter(tagFilterKey)
  private val envTags =
    EnvironmentTagBuilder.create(config.getConfig("additional-tags"))

  override def formatMeasurement(
      measurementData: String,
      tags: Map[String, String]
  ): String = {

    val filteredTags = envTags ++ tags.filterKeys(filter.accept)

    val stringTags: String = if (filteredTags.nonEmpty) {
      "|#" + filteredTags
        .map { case (k, v) ⇒ k + ":" + v }
        .mkString(",")
    } else {
      ""
    }

    StringBuilder.newBuilder
      .append(measurementData)
      .append(stringTags)
      .result()
  }
}
