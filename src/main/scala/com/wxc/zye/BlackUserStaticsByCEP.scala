package zye
import java.util
import java.util.concurrent.TimeUnit

import base.DataInfoClass.AdClick
import com.alibaba.fastjson.{JSON, JSONArray, JSONObject}
import myutils.SourceKafka
import org.apache.flink.api.scala._
import org.apache.flink.cep.PatternSelectFunction
import org.apache.flink.cep.scala.pattern.Pattern
import org.apache.flink.cep.scala.{CEP, PatternStream}
import org.apache.flink.streaming.api.functions.timestamps.BoundedOutOfOrdernessTimestampExtractor
import org.apache.flink.streaming.api.scala.{DataStream, KeyedStream, StreamExecutionEnvironment}
import org.apache.flink.streaming.api.windowing.time.Time
import org.apache.flink.streaming.connectors.kafka.{FlinkKafkaConsumer, FlinkKafkaConsumerBase}

object BlackUserStaticsByCEP {

  def main(args: Array[String]): Unit = {
    val env: StreamExecutionEnvironment = StreamExecutionEnvironment.getExecutionEnvironment
    env.setParallelism(1)

    val kafkaConsumer: FlinkKafkaConsumer[String] = new SourceKafka().getKafkaSource("eventlog")
    val consume: FlinkKafkaConsumerBase[String] = kafkaConsumer.setStartFromEarliest()
    val data: DataStream[String] = env.addSource(consume)

    val adClickStream: DataStream[AdClick] = data.map(x => {
      val adJsonObject: JSONObject = JSON.parseObject(x)
      val attrObject: JSONObject = adJsonObject.getJSONObject("attr")
      val area: String = attrObject.get("area").toString
      val uid: String = attrObject.get("uid").toString
      var productId: String = null
      var timestamp: Long = 0L
      val array: JSONArray = adJsonObject.getJSONArray("lagou_event")
      array.forEach(x => {
        val nObject: JSONObject = JSON.parseObject(x.toString)
        if (nObject.get("name").equals("ad")) {
          val adObject: JSONObject = nObject.getJSONObject("json")
          productId = adObject.get("product_id").toString
          timestamp = TimeUnit.MICROSECONDS.toSeconds(nObject.get("time").toString.toLong)
        }
      })
      AdClick(area, uid, productId, timestamp)
    })

    val waterMarkStream = adClickStream.assignTimestampsAndWatermarks(new BoundedOutOfOrdernessTimestampExtractor[AdClick](Time.seconds(5)) {
            override def extractTimestamp(element: AdClick): Long = {
              element.timestamp * 1000L
            }
          })
    val waterMarkStreams = adClickStream.assignAscendingTimestamps(x=>x.timestamp*1000L)

    val keyBy: KeyedStream[AdClick, (String, String)] = waterMarkStream.keyBy(x => (x.uid, x.productId))
//    keyBy.print("===value==")
    val pattern = Pattern.begin[AdClick]("blackUser")
        .where(x=>x.uid != null)
          .times(10)
        .within(Time.seconds(10))

    val patternStream: PatternStream[AdClick] = CEP.pattern(keyBy, pattern)

    val result: DataStream[AdClick] = patternStream.select(new UserPatternFunc)
    result.print("result---")
    env.execute("ts")
  }

  class UserPatternFunc extends PatternSelectFunction[AdClick, AdClick] {

    override def select(map: util.Map[String, util.List[AdClick]]): AdClick = {
        map.get("blackUser").iterator().next()
    }
  }


}














