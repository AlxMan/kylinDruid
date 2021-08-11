package zye

import base.EventLogInfo.{UserBrowseLog, UserClickLog}
import org.apache.flink.streaming.api.TimeCharacteristic
import org.apache.flink.streaming.api.functions.co.ProcessJoinFunction
import org.apache.flink.streaming.api.scala.StreamExecutionEnvironment
import org.apache.flink.streaming.api.windowing.time.Time
import org.apache.flink.util.Collector
import org.apache.flink.api.scala._

object TimeJoin {


  def main(args: Array[String]): Unit = {
    val env: StreamExecutionEnvironment = StreamExecutionEnvironment.getExecutionEnvironment
    env.setStreamTimeCharacteristic(TimeCharacteristic.EventTime)
    env.setParallelism(1)

    val clickStream = env.fromElements(
      UserClickLog("user_1", "1500", "click", "page_1"),
      UserClickLog("user_1", "2000", "click", "page_1")
    )
      .assignAscendingTimestamps(_.eventTime.toLong*1000)
      .keyBy(x=>x.userId)

    val browseStream = env.fromElements(
      UserBrowseLog("user_1", "1000", "product_1", "10"),
      UserBrowseLog("user_1", "1500", "product_1", "10"),
      UserBrowseLog("user_1", "1501", "product_1", "10"),
      UserBrowseLog("user_1", "1502", "product_1", "10")
    )
      .assignAscendingTimestamps(_.eventTime.toLong*1000)
      .keyBy(x=>x.userId)

    clickStream.intervalJoin(browseStream)
      .between(Time.minutes(-10),Time.seconds(0)) //定义开始时间和结束时间的规则
      .process(new IntervalJoinFunc)
//      .print()

    env.execute()

  }

  /**
    * 指定泛型，分别对应流和输出类型
    */
  class IntervalJoinFunc extends ProcessJoinFunction[UserClickLog,UserBrowseLog,String] {
    override def processElement(left: UserClickLog, right: UserBrowseLog, ctx: ProcessJoinFunction[UserClickLog, UserBrowseLog, String]#Context, out: Collector[String]): Unit = {
      println(left+"=====>"+right)
      out.collect(left + "--->" + right)
    }
  }

}
