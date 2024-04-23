package com.example.chatup

import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore
import java.util.UUID

class ConversationDao {
    val KEY_ID = "id"
    val KEY_MESSAGES = "messages"
    val KEY_USERS = "users"

    //#region Conversation
    fun createConversation(conversation: Conversation) {
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

    fun getUserConversations(
        user1: String,
        callback: (MutableList<Conversation>) -> Unit
    ) {
        val activeConversations = mutableListOf<Conversation>()

        FirebaseFirestore
            .getInstance()
            .collection("conversations")
            .get()
            .addOnSuccessListener { result ->
                for (document in result) {
                    if (document != null) {
                        var dbusers = document.get(KEY_USERS) as ArrayList<HashMap<String, Any>>
                        var users = ArrayList<User>()
                        if (dbusers != null && dbusers.isNotEmpty()) {
                            users = mapUsersToArrayList(dbusers)
                        }
                        if (users.find { it.name == user1 } != null) {
//                            var dbConversations = document
                            val id = document.getString(KEY_ID)
                            val dbMessages =
                                document.get(KEY_MESSAGES) as ArrayList<HashMap<String, Any>>
                            if (id != null && dbMessages != null) {

                                var messages = mapMessagesToArrayList(dbMessages)
                                activeConversations.add(Conversation(id, messages, users))
                            }


                        }
                    }
                }
                callback(activeConversations)

            }

    }

    fun getConversation(
        user1: String,
        user2: String,
        activity: ChatActivity,
        callback: (Conversation) -> Unit
    ) {
        var foundConversation = Conversation(
            "-1",
            //if youu remove 'message' and 'User'↓ the friendlist doesnt appear unless you come back from another activity.
            ArrayList<Message>(), ArrayList
            <User>()
        )
        FirebaseFirestore
            .getInstance()
            .collection("conversations")
            .get()
            .addOnSuccessListener { result ->
                // Foreach conversation figure out if user1 & 2 are part of that conversation
                for (document in result) {
                    if (document != null) {
                        var dbusers = document.get(KEY_USERS) as ArrayList<HashMap<String, Any>>
                        var users = ArrayList<User>()
                        if (dbusers != null && dbusers.isNotEmpty()) {
                            users = mapUsersToArrayList(dbusers)
                        }
                        if (users.find { it.name == user1 } != null && users.find { it.name == user2 } != null) {
                            var dbmsgs =
                                document.get(KEY_MESSAGES) as ArrayList<HashMap<String, Any>>
                            var results = ArrayList<Message>()
                            if (dbmsgs != null && dbmsgs.isNotEmpty()) {
                                results = mapMessagesToArrayList(dbmsgs)
                            }
                            foundConversation = Conversation(document.id, results, users)
                            activity.showMessages(results)
                            break
                        }
                    }
                }
                if (foundConversation.id != "-1") {
                    println("id is not -1 im returning ${foundConversation}")
                    callback(foundConversation)
                } else {
                    println("id is  -1 ")
                    callback(Conversation("-1", ArrayList<Message>(), ArrayList<User>()))
                }
                Log.i("SUCCSESS", " FETCHED CONVERSATION FROM FIRESTORE")
            }.addOnFailureListener { log ->
                Log.e("ERROR", "Failed to fetch CONVERSATION from firestore")
            }
    }

    //#endregion
    //#region messages
    fun addMessage(conversation: Conversation, sender: String, msg: String) {

        val userRef =
            FirebaseFirestore.getInstance().collection("conversations").document(conversation.id)
        val updates: Map<String, Any>
        var newMessage = Message(UUID.randomUUID().toString(), sender, msg)
        conversation.messages.add(newMessage)
        updates = hashMapOf(
            "messages" to conversation.messages as Any,
            "users" to conversation.users as Any
        )
        userRef.update(updates)
    }

    fun getMessages(conversation: Conversation, activity: ChatActivity) {
        var results = ArrayList<Message>()
        FirebaseFirestore
            .getInstance()
            .collection("conversations")
            .whereEqualTo("id", conversation.id)
            .get()
            .addOnSuccessListener { result ->
                if (result != null) {
                    for (document in result) {
                        val messagesData =
                            document.get(KEY_MESSAGES) as ArrayList<HashMap<String, Any>>

                        if (messagesData != null && messagesData.isNotEmpty()) {
                            for (messageData in messagesData) {
                                val id = messageData["id"] as String
                                val text = messageData["text"] as String
                                val sender = messageData["sender"] as String

                                val message = Message(id, sender, text)
                                results.add(message)
                            }
                        }
                        Log.i("SUCCSESS", " FETCHED CONVERSATIONS FROM FIRESTORE")
                        activity.showMessages(results)
                    }
                }
            }.addOnFailureListener { log -> Log.e("ERROR", "Failed to fetch USERS from firestore") }
    }

    //#endregion
    //#region mapping
    fun mapUsersToArrayList(hashMap: ArrayList<HashMap<String, Any>>): ArrayList<User> {
        var users = ArrayList<User>()
        for (messageData in hashMap) {
            val id = messageData["id"] as String
            val name = messageData["name"] as? String
            val password = messageData["password"] as? String
            val mail = messageData["email"] as String
            var dude = User(id, name, password, mail)
            users.add(dude)
        }
        return users
    }

    fun mapMessagesToArrayList(hashMap: ArrayList<HashMap<String, Any>>): ArrayList<Message> {
        var messages = ArrayList<Message>()
        for (messageData in hashMap) {
            val id = messageData["id"] as String
            val text = messageData["text"] as String
            val sender = messageData["sender"] as String

            val message = Message(id, sender, text)
            messages.add(message)
        }
        return messages
    }
    //#endregion
}