package com.wxc.druid

import org.apache.hadoop.conf.Configuration
import org.apache.hadoop.hbase.{HBaseConfiguration, HConstants}
import org.apache.hadoop.hbase.client.{Connection, ConnectionFactory}

class ConnHBase {
  def connToHbase:Connection ={
    val conf : Configuration = HBaseConfiguration.create()
    conf.set("hbase.zookeeper.quorum","node01,node02,node03")
    conf.set("hbase.zookeeper.property.clientPort","2181")
    conf.setInt(HConstants.HBASE_CLIENT_OPERATION_TIMEOUT,30000)
    conf.setInt(HConstants.HBASE_CLIENT_SCANNER_TIMEOUT_PERIOD,30000)
    val connection = ConnectionFactory.createConnection(conf)
    connection
  }

}
