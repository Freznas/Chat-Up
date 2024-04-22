package com.example.chatup

import java.io.Serializable

class Conversation (  val id: String,
                      val messages: ArrayList<Message>,
                      var users: ArrayList<User>):Serializable{
    override fun toString(): String {
        return "Conversation(id='$id', messages=$messages, users=$users)"
    }
}