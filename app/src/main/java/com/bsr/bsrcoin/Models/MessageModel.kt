package com.bsr.bsrcoin.ViewModels

class MessageModel()
{
    var message: String = ""
        get() = field
        set(message){field = message}
    var senderid: String = ""
        get() = field
        set(senderid){field = senderid}
    var receiverid: String = ""
        get() = field
        set(receiverid){field = receiverid}
    var messageid: String = ""
        get() = field
        set(messageid){field = messageid}
    var sentby: String = ""
        get() = field
        set(messageid){field = messageid}
    var time : Long = 0
        get() = field
        set(time) {field = time}

    constructor(message: String, senderid: String,receiverid: String,messageid: String,time: Long,sentby : String) : this()
    {
        this.message = message
        this.senderid = senderid
        this.receiverid = receiverid
        this.messageid = messageid
        this.time = time
        this.sentby = sentby
    }

    constructor(message: String, senderid: String,messageid: String,time: Long) : this()
    {
        this.message = message
        this.senderid = senderid
        this.messageid = messageid
        this.time = time
    }
}