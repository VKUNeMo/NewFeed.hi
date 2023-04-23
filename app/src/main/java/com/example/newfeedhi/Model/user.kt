package com.example.newfeedhi.Model

class user {
    var id:String = ""
    var name:String = ""
    var email: String= ""
    var address: String= ""
    var phone: String= ""
    var timestamp:Long = 0
    var imgAvt: String = ""
    var uid: String =""
    constructor()
    constructor(
        id: String,
        name: String,
        address: String,
        phone: String,
        timestamp: Long,
        email: String,
        imgAvt: String,
        uid: String
    ) {
        this.id = id
        this.name = name
        this.phone = phone
        this.address = address
        this.email=email
        this.timestamp = timestamp
        this.imgAvt = imgAvt
        this.uid = uid
    }
}