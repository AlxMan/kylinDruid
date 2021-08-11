package zye

import java.util

import com.alibaba.fastjson.{JSON, JSONObject}
import modes.TableObject
import myutils.SourceKafka
import org.apache.flink.streaming.api.scala.{DataStream, StreamExecutionEnvironment}
import org.apache.flink.api.scala._

/**
 * 1.从kafka的test这个topic获取数据-----FlinkKafkaConsumer
 * 2.把获取到的json格式的数据进行格式转化-----fastjson
 * type,database, table,data(jsonArray)
 * 3.把转化好的数据保存到HBase中
 */
object KafkaToHBase {

  def main(args: Array[String]): Unit = {
    val env = StreamExecutionEnvironment.getExecutionEnvironment
    val kafkaConsumer = new SourceKafka().getKafkaSource("canal")
    kafkaConsumer.setStartFromLatest()

    val sourceStream = env.addSource(kafkaConsumer)
    //type,database, table,data(jsonArray)
    //
    val mapped: DataStream[util.ArrayList[TableObject]] = sourceStream.map(x => {
      val jsonObj: JSONObject = JSON.parseObject(x)
      val database: AnyRef = jsonObj.get("database")
      val table: AnyRef = jsonObj.get("table")
      val typeInfo: AnyRef = jsonObj.get("type")

      val objects = new util.ArrayList[TableObject]()
      jsonObj.getJSONArray("data").forEach(x => {
        print(database.toString + ".." + table.toString + "..." + typeInfo.toString + ".." + x.toString)
        objects.add(TableObject(database.toString, table.toString, typeInfo.toString, x.toString))
      })
      objects
    })


    /**
     * 将数据下沉到HBase中保存
     * 1.拿到当前的数据
     * 2、addSink（）--- 自定义下沉器SinkHBase
     */
    mapped.addSink(new SinkHBase)

    env.execute()
  }
}
