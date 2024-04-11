package com.example.chatup

import android.content.Intent
import android.util.Log
import android.widget.Toast
import com.google.firebase.firestore.FirebaseFirestore
import java.net.URL

class ConversationDao {
    val KEY_ID ="id"
    val KEY_MESSAGES= "messages"
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
    fun createConversation(conversation: Conversation ,users: ArrayList<User> )
    {
        //check if conversation already exists
        isNewConversation(conversation) { notDuplicate ->
            if (notDuplicate) {
                //add users to conversation
                users.forEach()
                { user->
                    conversation.users.add(user)
                }
                // add conversation to database
                val dataToStore = HashMap<String, Any>()
                dataToStore[KEY_ID] = conversation.id as Any
                dataToStore[KEY_MESSAGES] = conversation.messages as Any
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
            else {
                Log.e("Error", "failed to create conversation ")
            }
        }
    }
    fun addMessage(conversation: Conversation, sender: String, message: String)
    {
        var newMessage = Message(sender, message)
        conversation.messages.add(newMessage)

        val userRef = FirebaseFirestore.getInstance().collection("conversations").document(conversation.id)
        val updates: Map<String, Any>  = hashMapOf(
            "messages" to conversation.messages as Any,
            "users" to conversation.users as Any
        )
        userRef.update( updates)
    }
    fun getMessages(conversation: Conversation, activity: MaxTestActivity)
    {
        var results = ArrayList<Message>()

        FirebaseFirestore
            .getInstance()
            .collection("conversations")
            .whereEqualTo("id" , conversation.id)
            .get()
            .addOnSuccessListener { result ->
                for (document in result) {
                    val id = document.getString(KEY_ID)
                    var messages = document.get(KEY_MESSAGES) as ArrayList<Message>

                    results = messages
                }
                Log.i("SUCCSESS", " FETCHED CONVERSATIONS FROM FIRESTORE")
                activity.showMessages(results)
            }.addOnFailureListener { log -> Log.e("ERROR", "Failed to fetch USERS from firestore") }
    }
    fun getConversations(activity: MaxTestActivity)
    {
        val results = ArrayList<Conversation>()

        FirebaseFirestore
            .getInstance()
            .collection("conversations")
            .get()
            .addOnSuccessListener { result ->
                for (document in result) {
                    val id = document.getString(KEY_ID)
                    var messages = document.get(KEY_MESSAGES) as ArrayList<Message>
                    var users = document.get(KEY_USERS) as ArrayList<User>

                    val conversation = Conversation(id!!, messages, users)
                    results.add(conversation)
                }
                Log.i("SUCCSESS", " FETCHED CONVERSATIONS FROM FIRESTORE")
                    activity.showConversations(results)
            }.addOnFailureListener { log -> Log.e("ERROR", "Failed to fetch USERS from firestore") }
    }
}