package com.example.chatup
// Activity to Display the active Chat between two or more users.
import android.content.ContentValues.TAG
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.chatup.databinding.ActivityChatBinding
import com.google.firebase.firestore.FirebaseFirestore

class ChatActivity : AppCompatActivity() {
    private lateinit var binding: ActivityChatBinding
    private lateinit var sentMesssagesAdapter: MessagesSentAdapter
    private lateinit var receievedMessagesAdapter: MessagesReceivedAdapter
    private val firestoreDB = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChatBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val sentMessages = listOf("sent message 1 ", "sent message 2 ", "sent message 3 ")
        val receivedMessages = listOf("Received message 1 ", "Received message 2")
        sentMesssagesAdapter = MessagesSentAdapter(this, sentMessages)
        receievedMessagesAdapter = MessagesReceivedAdapter(this, receivedMessages)

        binding.lvChatReceived.adapter = receievedMessagesAdapter
        binding.lvChatSent.adapter = sentMesssagesAdapter

        sentMesssagesAdapter.addAll(sentMessages)
        receievedMessagesAdapter.addAll(receivedMessages)

        fetchMessages()
    }

    private fun fetchMessages() {
        firestoreDB.collection("conversations")
            .get()
            .addOnSuccessListener { documents ->
                val sentMessages = mutableListOf<String>()
                for (document in documents) {
                    val message = document.getString("message") ?: ""
                    sentMessages.add(message)
                }
                sentMesssagesAdapter.addAll(sentMessages)
            }
            .addOnFailureListener { exception ->
                Log.e(
                    TAG,
                    "Error getting sent messages", exception
                )
            }
        firestoreDB.collection("conversations")
            .get()
            .addOnSuccessListener { documents ->
                val receivedMessages = mutableListOf<String>()
                for (document in documents) {
                    val message = document.getString("message") ?: ""
                    receivedMessages.add(message)
                }
                receievedMessagesAdapter.addAll(receivedMessages)
            }
            .addOnFailureListener { exception ->
                Log.e(TAG, "Error getting received messages", exception)
            }
    }

    companion object {
        private const val TAG = "ChatActivity"
    }
}
