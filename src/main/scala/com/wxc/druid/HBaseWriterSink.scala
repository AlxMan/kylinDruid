package com.wxc.druid


import org.apache.flink.configuration.Configuration
import org.apache.flink.streaming.api.functions.sink.{RichSinkFunction, SinkFunction}

import org.apache.hadoop.hbase.{TableName}
import org.apache.hadoop.hbase.client.{Connection, Put, Table}

class HBaseWriterSink extends RichSinkFunction[String]{
  var connection : Connection = _
  var hbTable : Table = _
  override def open(parameters: Configuration): Unit = {
    connection = new ConnHBase().connToHbase
    hbTable = connection.getTable(TableName.valueOf("waybill"))
  }

  override def close(): Unit = {
    if(hbTable != null) {
      hbTable.close()
    }
    if (connection != null) {
      connection.close()
    }
  }

  def insertHbase(hbTable: Table, value: String): Unit = {

    val order: Order = JsonUtil.jsonToObject(value)
    val put = new Put(order.orderId.toString.getBytes())
    put.addColumn("info".getBytes(),"waybillJson".getBytes(),value.getBytes())
    hbTable.put(put)
  }

  override def invoke(value: String, context: SinkFunction.Context[_]): Unit = {
//    println(value)
    insertHbase(hbTable,value)
  }

}
















