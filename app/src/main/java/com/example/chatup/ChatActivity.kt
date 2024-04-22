package com.example.chatup
// Activity to Display the active Chat between two or more users.
import android.os.Bundle
import android.util.Log
import android.widget.ArrayAdapter

import androidx.appcompat.app.AppCompatActivity
import com.example.chatup.databinding.ActivityChatBinding
import com.google.firebase.firestore.FirebaseFirestore
import com.google.gson.Gson
import java.util.UUID

class ChatActivity : AppCompatActivity() {
    private lateinit var binding: ActivityChatBinding
    private val firestoreDB = FirebaseFirestore.getInstance()
    var conversationDao = ConversationDao()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChatBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val docref = firestoreDB.collection("conversations")
        docref.addSnapshotListener { snapshot, e ->
            if (e != null) {
                Log.w(TAG, "Listen failed.", e)
                return@addSnapshotListener
            }

            if (snapshot != null) {
                Log.d(TAG, "Current data: ${snapshot.documents}")
                println("${snapshot.documents}")
                fetchMessages()

            } else {
                Log.d(TAG, "Current data: null")
            }
        }
           binding.btnSend.setOnClickListener {
           val message = binding.etChatMessage.text.toString()
           sendMessage( message)
       }
        binding.btnSend.setOnClickListener {
            val message = binding.etChatMessage.text.toString()
            sendMessage(message)
            binding.etChatMessage.text.clear()
        }
    }
    private fun sendMessage(message: String) {
        var users = ArrayList<User>()
        val user = getUser()
        users.add(user)
        // get other chat participant
        var receiver = intent.getStringExtra("receiver") as String
        var userDao = UserDao()
        userDao.getUserByUserName(receiver) { dude ->
            if (dude != null) {
                users.add(dude)
            }
            // check if conversation exists returns id == -1 if there is no active conversation
            conversationDao.getConversation(user.name!!, receiver, this) { conversation ->
                if (conversation.id != "-1") {
                    conversationDao.addMessage(conversation, user.name!!, message)
                } else if (conversation.id == "-1") {
                    var msgs = ArrayList<Message>()
                    msgs.add(Message(UUID.randomUUID().toString(), user.name!!, message))
                    var newConversation = Conversation(UUID.randomUUID().toString(), msgs, users)
                    conversationDao.createConversation(newConversation)
                }
            }
        }
    }

    private fun fetchMessages() {
        val user = getUser()
        var receiver = intent.getStringExtra("receiver") as String
        conversationDao.getConversation(user.name!!, receiver, this) { conversation ->

            conversationDao.getMessages(conversation, this)
        }
    }

    fun showMessages(results: ArrayList<Message>) {
        val user = getUser()
        if (user != null) {
            val sentAdapter = MessagesSentAdapter(this, results, user.name!!)
            binding.lvChatSent.adapter = sentAdapter
            binding.lvChatSent.post {
                binding.lvChatSent.setSelection(binding.lvChatSent.adapter.count - 1)
            }
             }
    }


    fun getUser(): User
    {
        val prefs = getSharedPreferences("com.example.chatup", MODE_PRIVATE)
        val json = prefs.getString("user", "")
        val gson = Gson()
        val user = gson.fromJson(json, User::class.java)
        return user
    }

    companion object {
        private const val TAG = "ChatActivity"
    }
}

