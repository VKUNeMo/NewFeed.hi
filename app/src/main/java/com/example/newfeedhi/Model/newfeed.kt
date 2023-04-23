package com.example.newfeedhi.Model

class newfeed {
    var id:String = ""
    var caption:String = ""
    var timestamp:Long = 0
    var image: String = ""
    var numberOfLike:Int = 0
    var uid: String =""
    constructor()
    constructor(
        id: String,
        caption: String,
        timestamp: Long,
        imageUrl: String,
        numberOfLike: Int,
        uid: String
    ) {
        this.id = id
        this.caption = caption
        this.timestamp = timestamp
        this.image = imageUrl
        this.numberOfLike = numberOfLike
        this.uid = uid
    }
}