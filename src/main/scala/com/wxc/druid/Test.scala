package com.wxc.druid

import java.util

import org.apache.flink.configuration
import org.apache.hadoop.hbase
import org.apache.hadoop.hbase.{Cell, CellUtil, TableName}
import org.apache.hadoop.hbase.client.{Connection, Get, Result, ResultScanner, Scan, Table}
import org.apache.hadoop.hbase.util.Bytes

import scala.collection.JavaConverters.asScalaBufferConverter

object Test {
    def main(args: Array[String]): Unit = {
         val conn : Connection = new ConnHBase().connToHbase
         var table : Table = null;
        val tableName: TableName = TableName.valueOf("waybill")
        val cf1: String = "info"
        table = conn.getTable(tableName)
        val get = new Get("1009430".getBytes())
        val cells = table.get(get).rawCells()
        cells.foreach{
            c => println(Bytes.toString(CellUtil.cloneValue(c)))
        }



    }

}
