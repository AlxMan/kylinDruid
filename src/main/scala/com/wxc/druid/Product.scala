package com.wxc.druid

import scala.beans.BeanProperty

/**
 * 产品类
 * @param productId
 * @param productName
 * @param price
 * @param productNum
 * @param categoryid
 * @param catname1
 * @param catname2
 * @param catname3
 */
case class Product(@BeanProperty var productId: Long,
                   @BeanProperty var productName: String,
                   @BeanProperty var price: Double,
                   @BeanProperty var productNum: Int,
                   @BeanProperty var categoryid: Int,
                   @BeanProperty var catname1: String,
                   @BeanProperty var catname2: String,
                   @BeanProperty var catname3: String) {
   // override def toString: String = super.toString
}
