package com.example.chatup

class Conversation (  val id: String,
                      val messages: ArrayList<Message>,
                      var users: ArrayList<User>){
    override fun toString(): String {
        return "Conversation(id='$id', messages=$messages, users=$users)"
    }
}