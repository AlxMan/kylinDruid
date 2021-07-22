package com.wxc.druid

import scala.beans.BeanProperty

/**
 * 原始数据类
 * @param ts 时间
 * @param orderId 订单id
 * @param userId 用户id
 * @param orderStatusId
 * @param orderStatus
 * @param payModeId
 * @param payMode
 * @param payment
 * @param products
 */
case class Order(@BeanProperty var ts: Long,
            @BeanProperty var orderId: Long,
            @BeanProperty var userId: Long,
            @BeanProperty var orderStatusId: Int,
            @BeanProperty var orderStatus: String,
            @BeanProperty var payModeId: Int,
            @BeanProperty var payMode: String,
            @BeanProperty var payment: Double,
            @BeanProperty var products:java.util.List[Product]) {


    //override def toString = s"Order($ts, $orderId, $user, $orderStatusId, $orderStatus, $payModeId, $payMode, $payment, $product)"
}

