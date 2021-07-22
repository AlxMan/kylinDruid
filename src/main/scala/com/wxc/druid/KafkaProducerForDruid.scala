package com.wxc.druid

import java.util.Properties

import org.apache.kafka.clients.producer.{KafkaProducer, ProducerConfig, ProducerRecord}
import org.apache.kafka.common.serialization.StringSerializer

import scala.io.BufferedSource

object KafkaProducerForDruid {
    def main(args: Array[String]): Unit = {
        // 定义 kafka 参数
        val brokers = "linux:9092,linux:9092,linux:9092"
        val topic = "sinkHbase"
        val prop = new Properties()
        prop.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, brokers)
        prop.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, classOf[StringSerializer])
        prop.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, classOf[StringSerializer])
        // KafkaProducer
        val producer = new KafkaProducer[String, String](prop)
        val source: BufferedSource = scala.io.Source.fromFile("data/test.json")
        val iter: Iterator[String] = source.getLines()
        iter.foreach{line =>
            val msg = new ProducerRecord[String, String](topic, line)
            producer.send(msg)
            Thread.sleep(5000)
        }
        producer.close()
        source.close()
    }
}
