package com.example.chatup

class Message(
    var sender: String , var text: String) {
    override fun toString(): String {
        return "$sender:\n$text"
    }
}