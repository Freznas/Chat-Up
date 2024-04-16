package com.example.chatup

import java.io.Serializable

data class Message(
    var id: String,
    var sender: String,
    var text: String
):Serializable {
    override fun toString(): String {
        return "$sender:\n$text"
    }
}