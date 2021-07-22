package com.wxc.druid

import java.util


import org.apache.flink.configuration
import org.apache.flink.streaming.api.functions.source.{RichSourceFunction, SourceFunction}
import org.apache.hadoop.conf.Configuration
import org.apache.hadoop.hbase.{Cell, HBaseConfiguration, HConstants, TableName}
import org.apache.hadoop.hbase.client.{Connection, ConnectionFactory, Result, ResultScanner, Scan, Table}
import org.apache.hadoop.hbase.util.Bytes

import scala.collection.JavaConverters._

/**

 */
class HBaseReader extends RichSourceFunction[(String,String)]{
  private var conn : Connection = null
  private var table : Table = null;
  private var scan : Scan = null;


  override def open(parameters: configuration.Configuration): Unit = {
    conn = new ConnHBase().connToHbase


    val tableName: TableName = TableName.valueOf("waybill")
    val cf1: String = "info"
    table = conn.getTable(tableName)
    scan = new Scan()

    scan.addFamily(Bytes.toBytes(cf1))
  }


  override def run(ctx: SourceFunction.SourceContext[(String, String)]): Unit = {
    val rs: ResultScanner = table.getScanner(scan)
    val iterator: util.Iterator[Result] = rs.iterator()
    while(iterator.hasNext) {
      val result: Result = iterator.next()
      val rowKey: String = Bytes.toString(result.getRow)
      val buffer: StringBuffer = new StringBuffer()
      for(cell: Cell <- result.listCells().asScala) {
        val value: String = Bytes.toString(cell.getValueArray, cell.getValueOffset, cell.getValueLength)
        buffer.append(value).append("-")
      }

      val valueString: String = buffer.replace(buffer.length() - 1, buffer.length(), "").toString
      ctx.collect((rowKey,valueString))

    }

  }

  override def cancel(): Unit = {

  }

  override def close(): Unit = {
    try {
      if(table != null) {
        table.close()
      }
      if(conn != null) {
        conn.close()
      }
    } catch {
      case e:Exception => println(e.getMessage)
    }
  }
}
