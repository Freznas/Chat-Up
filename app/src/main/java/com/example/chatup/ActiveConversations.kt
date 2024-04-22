package com.example.chatup

import java.io.Serializable

data class ActiveConversations(
    var id: String,
    var sender: String,
    var receiver: String,
    var text: String
): Serializable {
    override fun toString(): String {
        return "$sender:\n$text,$receiver:\n$text"
    }
}