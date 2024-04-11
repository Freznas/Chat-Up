package com.example.chatup

import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore
import java.util.UUID


class ConversationDao {
    val KEY_ID ="id"
//    val KEY_MESSAGES= "messages"
    val KEY_SENT= "sent"
    val KEY_RECIVED= "recived"
    val KEY_USERS = "users"

    fun isNewConversation(conversation: Conversation, callback: (Boolean) -> Unit)
    {
        // Only works until you restart app for some reason
        val conversationsCollection = FirebaseFirestore.getInstance().collection("conversations")

        conversationsCollection
            .whereEqualTo("users", conversation.users )
            .get().addOnSuccessListener { querySnapshot ->
                if (querySnapshot.isEmpty) { // found no duplicates
                    callback(true)
                } else { // Found duplicate
                    callback(false)
                }
            }
            .addOnFailureListener {exception ->
                Log.e("Error"," Query failed", exception)
            }
    }
    fun createConversation(conversation: Conversation  )
    {
//        val conversationCollection = FirebaseFirestore.getInstance().collection("conversations")

//        conversationCollection
                // add conversation to database
                val dataToStore = HashMap<String, Any>()
                dataToStore[KEY_ID] = conversation.id as Any
                dataToStore[KEY_SENT] = conversation.sentMessages as Any
                dataToStore[KEY_RECIVED] = conversation.recivedMessages as Any
                dataToStore[KEY_USERS] = conversation.users as Any
                FirebaseFirestore.getInstance()
                    .document("conversations/${conversation.id}")
                    .set(dataToStore).addOnSuccessListener {
                        Log.w("Success", "DocumentSnapshot successfully written!")
                    }
                    .addOnFailureListener { exception ->
                        Log.w("Failure", "Error writing document", exception)
                    }
    }
    fun addMessage(conversation: Conversation, sender: String, msg: String, isSender: Boolean)
    {

        val userRef = FirebaseFirestore.getInstance().collection("conversations").document(conversation.id)
        val updates: Map<String, Any>
        var newMessage = Message(UUID.randomUUID().toString(), sender, msg )
        if(isSender) {
            conversation.sentMessages.add(newMessage)
            updates = hashMapOf(
                "messages" to conversation.sentMessages as Any,
                "users" to conversation.users as Any
            )
        }
        else
        {
            conversation.recivedMessages.add(newMessage)
             updates = hashMapOf(
                "messages" to conversation.sentMessages as Any,
                "users" to conversation.users as Any
            )
        }
        userRef.update( updates)
    }
    fun getMessages(conversation: Conversation, activity: ChatActivity)
    {
        var resultsSent = ArrayList<Message>()
        var resultsRecived = ArrayList<Message>()
        FirebaseFirestore
            .getInstance()
            .collection("conversations")
            .whereEqualTo("id" , conversation.id)
            .get()
            .addOnSuccessListener { result ->

                for (document in result) {
                    println(document)
                    var sentmessages = document.get(KEY_SENT) as ArrayList<Message>
                    var recivedmsg = document.get(KEY_RECIVED)  as ArrayList<Message>

                    resultsSent = sentmessages
                    resultsRecived = recivedmsg
                }
                Log.i("SUCCSESS", " FETCHED CONVERSATIONS FROM FIRESTORE")
             activity.showMessages(resultsSent , resultsRecived)
            }.addOnFailureListener { log -> Log.e("ERROR", "Failed to fetch USERS from firestore") }
    }
    fun getConversations(activity: ChatActivity)
    {
        val results = ArrayList<Conversation>()

        FirebaseFirestore
            .getInstance()
            .collection("conversations")
            .get()
            .addOnSuccessListener { result ->
                for (document in result) {
                    val id = document.getString(KEY_ID)
//                    var messages = document.get(KEY_MESSAGES) as ArrayList<Message>
                    var users = document.get(KEY_USERS) as ArrayList<User>

//                    val conversation = Conversation(id!!, messages, users)
//                    results.add(conversation)
                }
                Log.i("SUCCSESS", " FETCHED CONVERSATIONS FROM FIRESTORE")
                   activity.showConversations(results)
            }.addOnFailureListener { log -> Log.e("ERROR", "Failed to fetch USERS from firestore") }
    }
}