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
//    private lateinit var receievedMessagesAdapter: MessagesReceivedAdapter
    private val firestoreDB = FirebaseFirestore.getInstance()
    var conversationDao = ConversationDao()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChatBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val sentMessages = listOf("sent message 1 ", "sent message 2 ", "sent message 3 ")
        val receivedMessages = listOf("Received message 1 ", "Received message 2")


//        createDummyConvo()

 /*       sentMesssagesAdapter = MessagesSentAdapter(this, sentMessages)
        receievedMessagesAdapter = MessagesReceivedAdapter(this, receivedMessages)

        binding.lvChatReceived.adapter = receievedMessagesAdapter
        binding.lvChatSent.adapter = sentMesssagesAdapter

        sentMesssagesAdapter.addAll(sentMessages)
        receievedMessagesAdapter.addAll(receivedMessages)

        fetchMessages(dummyConvo)*/


    }

    private fun fetchMessages(conversation: Conversation) {
//        conversationDao.getMessages(conversation,this)
        showMessages(conversation.messages)

    }

    fun showConversations(results: ArrayList<Conversation>) {

    }
    fun showMessages(results: ArrayList<Message>){

        val user = intent.getSerializableExtra("user") as User
        val sentAdapter = MessagesSentAdapter(this,results, user.name!!)
        binding.lvChatSent.adapter = sentAdapter
    }
//#region TestingDummyData
//    fun createDummyConvo(){
//
//        var users: ArrayList<User> = ArrayList<User>()
//        users.add(User("1","1","1","1"))
//        users.add(User("1","1","1","1"))
//        val messages = ArrayList<Message>()
//        messages.add(Message("1", "Dick", "Hello dude"))
//        messages.add(Message("2", "Stefan", "Hi there!"))
//        var dummyConvo = Conversation("67a056e7-4bcf-4376-9a2a-7447cd925112", ArrayList<Message>() ,  users )
//        //    conversationDao.createConversation(dummyConvo)
//    // create conversation and save it in database (change id to random UUID)
//
//       fetchMessages(dummyConvo)
//    }
    //#endregion
    companion object {
        private const val TAG = "ChatActivity"
    }
}
