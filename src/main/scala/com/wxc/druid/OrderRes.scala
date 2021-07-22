package com.wxc.druid

import scala.beans.BeanProperty

/**
 * 合并订单和产品的类
 * @param ts
 * @param orderId
 * @param user
 * @param orderStatusId
 * @param orderStatus
 * @param payModeId
 * @param payMode
 * @param payment
 * @param productId
 * @param productName
 * @param price
 * @param productNum
 * @param categoryid
 * @param catname1
 * @param catname2
 * @param catname3
 */
case class OrderRes(@BeanProperty var ts: Long,
               @BeanProperty var orderId: Long,
               @BeanProperty var user: Long,
               @BeanProperty var orderStatusId: Int,
               @BeanProperty var orderStatus: String,
               @BeanProperty var payModeId: Int,
               @BeanProperty var payMode: String,
               @BeanProperty var payment: Double,
               @BeanProperty var productId: Long,
               @BeanProperty var productName: String,
               @BeanProperty var price: Double,
               @BeanProperty var productNum: Int,
               @BeanProperty var categoryid: Int,
               @BeanProperty var catname1: String,
               @BeanProperty var catname2: String,
               @BeanProperty var catname3: String ) {


}
