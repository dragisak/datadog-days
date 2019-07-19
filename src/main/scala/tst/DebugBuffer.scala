package tst

import com.typesafe.config.Config
import kamon.datadog.DatadogAgentReporter.PacketBuffer

class DebugBuffer(config: Config) extends PacketBuffer {
  private val metricSeparator      = "\n"
  private val measurementSeparator = ":"
  private val buffer               = new StringBuilder()
  private val maxPacketSizeInBytes = config.getBytes("agent.max-packet-size")
  private val hostname             = config.getString("agent.hostname")
  private val port                 = config.getInt("agent.port")

  override def appendMeasurement(key: String, measurementData: String): Unit = {
    val data = key + measurementSeparator + measurementData

    if (fitsOnBuffer(metricSeparator + data)) {
      val mSeparator = if (buffer.nonEmpty) metricSeparator else ""
      buffer.append(mSeparator).append(data)
    } else {
      flushToUDP(buffer.toString())
      buffer.clear()
      buffer.append(data)
    }
  }

  private def fitsOnBuffer(data: String): Boolean =
    (buffer.length + data.length) <= maxPacketSizeInBytes

  private def flushToUDP(data: String): Unit = {
    println(s"""echo "$data" | nc -u -w1 $hostname $port""")
  }

  override def flush(): Unit = {
    flushToUDP(buffer.toString)
    buffer.clear()
  }
}
