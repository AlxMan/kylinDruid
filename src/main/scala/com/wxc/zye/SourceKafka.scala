package zye

import java.util.Properties
import org.apache.flink.api.common.serialization.SimpleStringSchema
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaConsumer

class SourceKafka {
  def getKafkaSource(topicName: String) : FlinkKafkaConsumer[String] = {
    val props = new Properties()
    props.setProperty("bootstrap.servers","linux121:9092,linux122:9092,linux123:9092");//3,4
    props.setProperty("group.id","consumer-group")
    props.setProperty("key.deserializer","org.apache.kafka.common.serialization.StringDeserializer")
    props.setProperty("value.deserializer","org.apache.kafka.common.serialization.StringDeserializer")
//    props.setProperty("auto.offset.reset","latest")
    props.setProperty("auto.offset.reset","earliest")

    new FlinkKafkaConsumer[String](topicName, new SimpleStringSchema(),props);
  }
}
