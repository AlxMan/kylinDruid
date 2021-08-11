package zye


object DataInfoClass {
  case class AreaDetail(id: Int, name:String, pid: Int)
  /**
    *
    * @param areaId 区的id
    * @param aname 区的名字
    * @param cid 城市的id
    * @param city 城市的名字
    * @param proId 省份的id
    * @param province 省份的名字
    */
  case class DimArea(areaId:Int,aname:String,cid:Int,city:String,proId:Int,province:String)

  case class TradeOrder(orderId:Int,orderNo:String,userId:Int,status:Int,totalMoney:Double,areaId:Int)

  case class AdClick(area: String, uid:String ,productId: String,timestamp:Long)

  case class AreaInfo(
                       id: String,
                       name: String,
                       pid: String,
                       sname: String,
                       level: String,
                       citycode: String,
                       yzcode: String,
                       mername: String,
                       Lng: String,
                       Lat: String,
                       pinyin: String
                     )

  case class BlackUser(userId: String, aid:String,count:Long)

  case class ChanalDetail(chanal: String, uid : String)

  case class CityOrder(city:String, province:String, totalMoney:Double, totalCount:Int)

  case class CountByChannal(chanal:String,count: Long)

  case class CountByProductAd(windowEnd:String,productId:String,cont:Long)

  case class DataInfo(
                       modifiedTime: String,
                       orderNo: String,
                       isPay: String,
                       orderId: String,
                       tradeSrc: String,
                       payTime: String,
                       productMoney: String,
                       totalMoney: String,
                       dataFlag: String,
                       userId: String,
                       areaId: String,
                       createTime: String,
                       payMethod: String,
                       isRefund: String,
                       tradeType: String,
                       status: String
                     )

  case class OrderDetail(orderId:String, status:String, orderCreateTime:String, price:Double)

  case class TableObject (database:String, tableName:String, typeInfo: String, dataInfo: String) extends Serializable

}
