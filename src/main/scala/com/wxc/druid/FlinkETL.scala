package com.wxc.druid


import java.util.Properties

import org.apache.flink.api.common.functions.FlatMapFunction
import org.apache.flink.api.common.serialization.SimpleStringSchema
import org.apache.flink.streaming.connectors.kafka.{FlinkKafkaConsumer, FlinkKafkaProducer}
import org.apache.flink.streaming.api.datastream.{DataStream, SingleOutputStreamOperator}
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment
import org.apache.flink.util.Collector

import scala.collection.JavaConverters._
import scala.collection.mutable


/**
 * 使用flink对kafka中的数据进行拆分，将products对象中的数据做压平
 */
object FlinkETL {
    def main(args: Array[String]): Unit = {
        //创建flink流环境
        val env: StreamExecutionEnvironment = StreamExecutionEnvironment.getExecutionEnvironment
        //设置并行度
        env.setParallelism(1)
        //kafka配置
        val properties = new Properties();
        //集群地址
        properties.setProperty("bootstrap.servers", "linux121:9092,linux122:9092,linux123:9092");
        //消费原始数据的主题
        val consumer: FlinkKafkaConsumer[String] = new FlinkKafkaConsumer[String]("druid1", new SimpleStringSchema(), properties)
        consumer.setStartFromEarliest();
        //获取原始数据流
        val stream: DataStream[String] = env.addSource(consumer)
        //创建生产者
        val producer: FlinkKafkaProducer[String] = new FlinkKafkaProducer[String]( "druid2", new SimpleStringSchema(),properties)
        //对原始数据进行压平
        val res: SingleOutputStreamOperator[String] = stream.flatMap(new FlatMapFunction[String, String] {
            override def flatMap(t: String, collector: Collector[String]): Unit = {
                val order: Order = JsonUtil.jsonToObject(t)
                val products: mutable.Seq[Product] = order.products.asScala
                products.foreach {
                    product =>
                        val str = JsonUtil.objectToJson(order, product)
                        collector.collect(str)
                }

            }
        })
        //kafka发送
        res.addSink(producer)
        env.execute()
    }
}
