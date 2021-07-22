package com.wxc.druid

import java.util

import com.alibaba.fastjson.serializer.{SerializeFilter, SerializerFeature}
import com.alibaba.fastjson.JSON



import scala.language.implicitConversions

object JsonUtil {
    //json转为Order对象
    def jsonToObject(jsonStr: String): Order = {
        val order: Order = JSON.parseObject(jsonStr, classOf[Order])
        order
    }
    //订单信息和product信息合成一个新的orderRes对象 转为json字符串
    def objectToJson(order:Order,product:Product):String={
        val orderRes = OrderRes.apply(order.ts,
            order.orderId,
            order.userId,
            order.orderStatusId,
            order.orderStatus,
            order.payModeId,
            order.payMode,
            order.payment,
            product.productId,
            product.productName,
            product.price,
            product.productNum,
            product.categoryid,
            product.catname1,
            product.catname2,
            product.catname3
        )
        JSON.toJSONString(orderRes,null.asInstanceOf[Array[SerializeFilter]])
    }


}
