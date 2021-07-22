package com.wxc.druid

import java.util.Properties

import org.apache.flink.api.common.serialization.SimpleStringSchema
import org.apache.flink.streaming.api.datastream.DataStream
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaConsumer

object FlinkSinkToHbase {
    def main(args: Array[String]): Unit = {
        //创建flink流环境
        val env: StreamExecutionEnvironment = StreamExecutionEnvironment.getExecutionEnvironment
        //设置并行度
        env.setParallelism(1)
        //kafka配置
        val properties = new Properties();
        //集群地址
        properties.setProperty("bootstrap.servers", "linux121:9092,linux122:9092,linux123:9092")
        //消费原始数据的主题
        val consumer: FlinkKafkaConsumer[String] = new FlinkKafkaConsumer[String]("sinkHbase", new SimpleStringSchema(), properties)
        consumer.setStartFromEarliest();
        //获取原始数据流
        val stream: DataStream[String] = env.addSource(consumer)

        stream.addSink(new HBaseWriterSink)
        env.execute()
    }
}
