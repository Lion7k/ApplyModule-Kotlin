package com.liuzq.basemodule_kotlin.bean

/**
 * Created by liuzq on 2018/10/18.
 *
 *
 * 服务器列表
 */

class BookBean {

    /**
     * rating : {"max":10,"numRaters":364,"average":"7.1","min":0}
     * subtitle :
     * author : ["[日] 片山恭一"]
     * pubdate : 2005-1
     * tags : [{"count":143,"name":"片山恭一","title":"片山恭一"},{"count":69,"name":"日本","title":"日本"},{"count":65,"name":"日本文学","title":"日本文学"},{"count":41,"name":"小说","title":"小说"},{"count":33,"name":"满月之夜白鲸现","title":"满月之夜白鲸现"},{"count":16,"name":"爱情","title":"爱情"},{"count":10,"name":"純愛","title":"純愛"},{"count":9,"name":"外国文学","title":"外国文学"}]
     * origin_title :
     * image : https://img3.doubanio.com/mpic/s1747553.jpg
     * binding : 平装
     * translator : ["豫人"]
     * catalog :
     *
     *
     * pages : 180
     * images : {"small":"https://img3.doubanio.com/spic/s1747553.jpg","large":"https://img3.doubanio.com/lpic/s1747553.jpg","medium":"https://img3.doubanio.com/mpic/s1747553.jpg"}
     * alt : https://book.douban.com/subject/1220562/
     * id : 1220562
     * publisher : 青岛出版社
     * isbn10 : 7543632608
     * isbn13 : 9787543632608
     * title : 满月之夜白鲸现
     * url : https://api.douban.com/v2/book/1220562
     * alt_title :
     * author_intro :
     * summary : 那一年，是听莫扎特、钓鲈鱼和家庭破裂的一年。说到家庭破裂，母亲怪自己当初没有找到好男人，父亲则认为当时是被狐狸精迷住了眼，失常的是母亲，但出问题的是父亲……。
     * price : 15.00元
     */

    var rating: RatingBean? = null
    var subtitle: String? = null
    var pubdate: String? = null
    var origin_title: String? = null
    var image: String? = null
    var binding: String? = null
    var catalog: String? = null
    var pages: String? = null
    var images: ImagesBean? = null
    var alt: String? = null
    var id: String? = null
    var publisher: String? = null
    var isbn10: String? = null
    var isbn13: String? = null
    var title: String? = null
    var url: String? = null
    var alt_title: String? = null
    var author_intro: String? = null
    var summary: String? = null
    var price: String? = null
    var author: List<String>? = null
    var tags: List<TagsBean>? = null
    var translator: List<String>? = null

    override fun toString(): String {
        return "BookBean{" +
                "rating=" + rating +
                ", subtitle='" + subtitle + '\''.toString() +
                ", pubdate='" + pubdate + '\''.toString() +
                ", origin_title='" + origin_title + '\''.toString() +
                ", image='" + image + '\''.toString() +
                ", binding='" + binding + '\''.toString() +
                ", catalog='" + catalog + '\''.toString() +
                ", pages='" + pages + '\''.toString() +
                ", images=" + images +
                ", alt='" + alt + '\''.toString() +
                ", id='" + id + '\''.toString() +
                ", publisher='" + publisher + '\''.toString() +
                ", isbn10='" + isbn10 + '\''.toString() +
                ", isbn13='" + isbn13 + '\''.toString() +
                ", title='" + title + '\''.toString() +
                ", url='" + url + '\''.toString() +
                ", alt_title='" + alt_title + '\''.toString() +
                ", author_intro='" + author_intro + '\''.toString() +
                ", summary='" + summary + '\''.toString() +
                ", price='" + price + '\''.toString() +
                ", author=" + author +
                ", tags=" + tags +
                ", translator=" + translator +
                '}'.toString()
    }

    class RatingBean {
        /**
         * max : 10
         * numRaters : 364
         * average : 7.1
         * min : 0
         */

        var max: Int = 0
        var numRaters: Int = 0
        var average: String? = null
        var min: Int = 0
    }

    class ImagesBean {
        /**
         * small : https://img3.doubanio.com/spic/s1747553.jpg
         * large : https://img3.doubanio.com/lpic/s1747553.jpg
         * medium : https://img3.doubanio.com/mpic/s1747553.jpg
         */

        var small: String? = null
        var large: String? = null
        var medium: String? = null
    }

    class TagsBean {
        /**
         * count : 143
         * name : 片山恭一
         * title : 片山恭一
         */

        var count: Int = 0
        var name: String? = null
        var title: String? = null
    }
}
