package com.example.chatup

data class Message(
    var id: String, var sender: String , var text: String) {
    override fun toString(): String {
        return "$sender:\n$text"
    }
}