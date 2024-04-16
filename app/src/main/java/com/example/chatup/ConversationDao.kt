package com.example.chatup

import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore
import java.util.UUID


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
    fun createConversation(conversation: Conversation  )
    {
//        val conversationCollection = FirebaseFirestore.getInstance().collection("conversations")
//        conversationCollection


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
    fun addMessage(conversation: Conversation, sender: String, msg: String)
    {

        val userRef = FirebaseFirestore.getInstance().collection("conversations").document(conversation.id)
        val updates: Map<String, Any>
        var newMessage = Message(UUID.randomUUID().toString(), sender, msg )
        conversation.messages.add(newMessage)
        updates = hashMapOf(
            "messages" to conversation.messages as Any,
            "users" to conversation.users as Any
        )
        userRef.update( updates)
    }
    fun getMessages(conversation: Conversation, activity: ChatActivity)
    {
        var results = ArrayList<Message>()
        FirebaseFirestore
            .getInstance()
            .collection("conversations")
            .whereEqualTo("id" , conversation.id)
            .get()
            .addOnSuccessListener { result ->
               //
                for (document in result) {
                    val messagesData = document.get(KEY_MESSAGES) as ArrayList<HashMap<String, Any>>

                    for (messageData in messagesData) {
                        val id = messageData["id"] as String
                        val text = messageData["text"] as String
                        val sender = messageData["sender"] as String

                        val message = Message(id, sender ,text) // Anpassa konstruktorn efter din Message-klass
                        results.add(message)
                    }
                }
                //
                Log.i("SUCCSESS", " FETCHED CONVERSATIONS FROM FIRESTORE")
             activity.showMessages(results)
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
                    var messages = document.get(KEY_MESSAGES) as ArrayList<Message>
                    var users = document.get(KEY_USERS) as ArrayList<User>

                   val conversation = Conversation(id!!, messages, users)
                    results.add(conversation)
                }
                Log.i("SUCCSESS", " FETCHED CONVERSATIONS FROM FIRESTORE")
                   activity.showConversations(results)
            }.addOnFailureListener { log -> Log.e("ERROR", "Failed to fetch USERS from firestore") }
    }
    fun getConversationsForUser(user: User, callback: (List<Conversation>) -> Unit) {
        val conversationsCollection = FirebaseFirestore.getInstance().collection("conversations")

        conversationsCollection
            .whereArrayContains("users", user) // Filter conversations where the user is present
            .get()
            .addOnSuccessListener { result ->
                val conversations = ArrayList<Conversation>()
                for (document in result) {
                    val id = document.getString(KEY_ID)
                    val messages = document.get(KEY_MESSAGES) as ArrayList<Message>
                    val users = document.get(KEY_USERS) as ArrayList<User>
                    val conversation = Conversation(id!!, messages, users)
                    conversations.add(conversation)
                }
                callback(conversations)
            }
            .addOnFailureListener { exception ->
                Log.e("ERROR", "Failed to fetch conversations", exception)
                callback(emptyList()) // Return an empty list in case of failure
            }
    }
}