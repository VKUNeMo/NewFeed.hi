package com.example.newfeedhi.Model

class notification {
    var id: String = ""
    var content: String = ""
    var timestamp: Long = 0
    var uid: String = ""

    constructor()
    constructor(
        id: String,
        content: String,
        timestamp: Long,
        uid: String
    ) {
        this.id = id
        this.content = content
        this.timestamp = timestamp
        this.uid = uid
    }
}