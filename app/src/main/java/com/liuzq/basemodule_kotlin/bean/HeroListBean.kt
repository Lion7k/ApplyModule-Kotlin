package com.liuzq.basemodule_kotlin.bean

/**
 * Created by liuzq on 2018/10/18.
 */

class HeroListBean {

    /**
     * total_pages : 26
     * version : 2016-04-12
     * limit : 5
     * total : 130
     * data : [{"en_name":"Annie","name":"安妮 黑暗之女","img":"","money":"2000.0","newmoney":"1000","newhero":"false","coin":"4800.0","id":"1"},{"en_name":"Olaf","name":"奥拉夫 狂战士","img":"","money":"1500.0","newmoney":"750","newhero":"false","coin":"3150.0","id":"2"},{"en_name":"Galio","name":"加里奥 哨兵之殇","img":"","money":"2000.0","newmoney":"","newhero":"false","coin":"3150.0","id":"3"},{"en_name":"TwistedFate","name":"崔斯特 卡牌大师","img":"","money":"3000.0","newmoney":"1500","newhero":"false","coin":"4800.0","id":"4"},{"en_name":"XinZhao","name":"赵信 德邦总管","img":"","money":"2500.0","newmoney":"","newhero":"false","coin":"3150.0","id":"5"}]
     * page : 1
     */

    var total_pages: Int = 0
    var version: String? = null
    var limit: Int = 0
    var total: Int = 0
    var page: Int = 0
    var data: List<DataBean>? = null

    class DataBean {
        /**
         * en_name : Annie
         * name : 安妮 黑暗之女
         * img :
         * money : 2000.0
         * newmoney : 1000
         * newhero : false
         * coin : 4800.0
         * id : 1
         */

        var en_name: String? = null
        var name: String? = null
        var img: String? = null
        var money: String? = null
        var newmoney: String? = null
        var newhero: String? = null
        var coin: String? = null
        var id: String? = null

        override fun toString(): String {
            return "DataBean{" +
                    "en_name='" + en_name + '\''.toString() +
                    ", name='" + name + '\''.toString() +
                    ", img='" + img + '\''.toString() +
                    ", money='" + money + '\''.toString() +
                    ", newmoney='" + newmoney + '\''.toString() +
                    ", newhero='" + newhero + '\''.toString() +
                    ", coin='" + coin + '\''.toString() +
                    ", id='" + id + '\''.toString() +
                    '}'.toString()
        }
    }
}
