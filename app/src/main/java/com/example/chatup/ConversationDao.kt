package com.example.chatup

import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore
import java.net.MalformedURLException
import java.net.URL
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
                if(result!=null) {
                    for (document in result) {
                            val messagesData =
                                document.get(KEY_MESSAGES) as ArrayList<HashMap<String, Any>>

                        if (messagesData!=null && messagesData.isNotEmpty()) {
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
    // provide arrarylist of users, fun checks if list matches list in db
    fun getConversation(user1:String, user2: String, activity: ChatActivity,   callback: (Conversation) -> Unit)
    {
        var returnMe = Conversation("-1",
            ArrayList<Message>(), ArrayList
        <User>())
        FirebaseFirestore
            .getInstance()
            .collection("conversations")
            .get()
            .addOnSuccessListener { result ->
                // Foreach conversation figure out if input users are part of that conversation
               for( document in result)
               {
                if(document!=null ) {

                    var dbusers = document.get(KEY_USERS) as ArrayList<HashMap<String, Any>>
//                    println(users +"\n" + iusers)
                    // map hashmap to arraylist
                    var users = ArrayList<User>()
                    if (dbusers!=null && dbusers.isNotEmpty()) {
                        for (messageData in dbusers) {
                            val id = messageData["id"] as String
                            val name = messageData["name"] as? String
                            val password = messageData["password"] as? String
                            val mail = messageData["email"] as String
                            var dude = User(id, name, password, mail)
                            users.add(dude)
                        }
                    }
                    // check if input users are in users
                    // if(users.contains(user1) && users.contains(user2)) <- have to map everything on user for this to work I suspect
                    if(users.find{ it.name == user1} !=null && users.find{it.name == user2}!=null)
                    {
                        // found conversation
                        val id = document.getString(KEY_ID)
                        var dbmsgs = document.get(KEY_MESSAGES) as ArrayList<HashMap<String, Any>>
                        // map msgs to Arary List
                        var results = ArrayList<Message>()
                        if (dbmsgs!=null && dbmsgs.isNotEmpty()) {
                            for (messageData in dbmsgs) {
                                val id = messageData["id"] as String
                                val text = messageData["text"] as String
                                val sender = messageData["sender"] as String

                                val message = Message(id, sender, text)
                                results.add(message)
                                break
                            }
                        }
                        // Send back conversation and show messages
                        val conversation = Conversation(id!!, results, users)
                        returnMe = conversation
                        activity.showMessages(results)
                        break
                    }
                }
               }
                if(returnMe.id != "-1")
                {
                    callback(returnMe)
                }
                else {
                    callback(Conversation("-1", ArrayList<Message>(), ArrayList<User>()))
                }
                Log.i("SUCCSESS", " FETCHED CONVERSATION FROM FIRESTORE")
            }.addOnFailureListener { log -> Log.e("ERROR", "Failed to fetch USERS from firestore")
            callback(Conversation("-1",ArrayList<Message>(), ArrayList<User>()))}
    }
}