package com.example.chatup

class Conversation (  val id: String,
                      val sentMessages: ArrayList<Message>,
                      val recivedMessages: ArrayList<Message>,
                      var users: ArrayList<User>){
    override fun toString(): String {
        return "Conversation(id='$id', sentMessages=$sentMessages, recivedMessages=$recivedMessages, users=$users)"
    }
}