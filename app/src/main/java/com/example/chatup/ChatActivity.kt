package com.example.chatup
// Activity to Display the active Chat between two or more users.
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.chatup.databinding.ActivityChatBinding
import com.google.firebase.firestore.FirebaseFirestore
import java.util.UUID

class ChatActivity : AppCompatActivity() {
    private lateinit var binding: ActivityChatBinding
    private lateinit var sentMesssagesAdapter: MessagesSentAdapter
    private val firestoreDB = FirebaseFirestore.getInstance()
    var conversationDao = ConversationDao()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChatBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val sentMessages = listOf(
            Message("1", "User 1", "sent message 1"),
            Message("2", "User 1", "sent message 2"),
            Message("3", "User 2", "sent message 3")
        )
        val receievedMessages = listOf(
            Message("1", "User 2", "receieved message 1"),
            Message("2", "User 2", "receieved message 2"),
            Message("3", "User 1", "receieved message 3")
        )

        val allMessages = sentMessages + receievedMessages
        binding.btnSend.setOnClickListener {
            val message =
                "Your message" // Get the message from your input field or wherever it's stored
            val sender = "User 1" // Replace with the actual sender name or ID
            sendMessageToDatabase(
                message,
                sender
            ) // Call the function to send the message to the database
            val user = intent.getSerializableExtra("selectedUser") as User
            fetchConversationsForUser(user)

        }


        sentMesssagesAdapter = MessagesSentAdapter(this, allMessages,"user 1")



        binding.lvChatSent.adapter = sentMesssagesAdapter

//        sentMesssagesAdapter.addAll(sentMessages)


    }

    private fun sendMessageToDatabase(message: String, sender: String) {
        val conversationId =
            "67a056e7-4bcf-4376-9a2a-7447cd925112" // Replace with your conversation ID
        val conversation =
            Conversation(conversationId, ArrayList(), ArrayList()) // Create a dummy conversation
        val conversationDao = ConversationDao() // Initialize ConversationDao

        // Add the message to the conversation using ConversationDao
        conversationDao.addMessage(conversation, sender, message)
    }

    private fun fetchConversationsForUser(user: User) {
        conversationDao.getConversationsForUser(user) { conversations ->
            // Once you have the conversations, you can display them or process them as needed
            // For example, you can update your UI with the fetched conversations
            // Here, you can call a method to display the conversations in your UI
        }
    }

    private fun fetchMessages(conversation: Conversation) {
//        conversationDao.getMessages(conversation,this)
        showMessages(conversation.messages)

    }

    fun showConversations(results: ArrayList<Conversation>) {

    }

    fun showMessages(results: ArrayList<Message>) {

        val user = intent.getSerializableExtra("user") as User
        val sentAdapter = MessagesSentAdapter(this, results, user.name!!)
        binding.lvChatSent.adapter = sentAdapter
    }

    companion object {
        private const val TAG = "ChatActivity"
    }
}
