package com.example.chatup
// Activity to Display the active Chat between two or more users.
import android.R
import android.os.Bundle
import android.widget.ArrayAdapter
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

        val sentMsgsArray = ArrayList<Message>()
        var msg = Message(UUID.randomUUID().toString(),"max", "sent message 1")
        sentMsgsArray.add(0, msg)
        msg = Message(UUID.randomUUID().toString(),"max", "sent message 1")
        sentMsgsArray.add(1, msg)
        msg = Message(UUID.randomUUID().toString(),"max", "sent message 1")
        sentMsgsArray.add(2, msg)

        var users: ArrayList<User> = ArrayList<User>()
        users.add(User("1","1","1","1"))
        users.add(User("1","1","1","1"))

        var dummyConvo = Conversation("e397a4d8-03ca-4f57-9f04-391f221016dc", sentMsgsArray , users )

       // conversationDao.createConversation(dummyConvo) funkar
//        conversationDao.addMessage(dummyConvo, "max", "maxmamxamxmamx") funkar
       conversationDao.getMessages( dummyConvo, this)
        sentMesssagesAdapter = MessagesSentAdapter(this, sentMessages)
//        receievedMessagesAdapter = MessagesReceivedAdapter(this, receivedMessages)

//        binding.lvChatReceived.adapter = receievedMessagesAdapter
        binding.lvChatSent.adapter = sentMesssagesAdapter

//        sentMesssagesAdapter.addAll(sentMessages)
//        receievedMessagesAdapter.addAll(receivedMessages)

        fetchMessages(dummyConvo)
    }

    private fun fetchMessages(converstion: Conversation) {
        conversationDao.getMessages(converstion,this)
     /*  firestoreDB.collection("conversations")
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
//                receievedMessagesAdapter.addAll(receivedMessages)
            }
            .addOnFailureListener { exception ->
                Log.e(TAG, "Error getting received messages", exception)
            }*/
    }

    fun showConversations(results: ArrayList<Conversation>) {

    }
    fun showMessages(results: ArrayList<Message>){
        TODO("split results into recived and sent messages")
        /*   bortkommenterad del är för att dela upp i sent och recived
        funkar inte helt då results får med massa hashmaps måste ändra i daon
        var recivedMesseges = ArrayList<Message>()
        var sentMesseges = ArrayList<Message>()
        var MEEEE = intent.getStringExtra("user")
//        println("user ${MEEEE}")
//        println("user ${results}")


     for (i in 0 until results.size) {
            System.out.println(results.get(i))
            if(results[i].sender != MEEEE)
            {
                recivedMesseges.add(results[i])
            }
            else
            {
                sentMesseges.add(results[i])
            }
        }
                val sentAdapter = ArrayAdapter(this, R.layout.simple_list_item_1, sentMesseges)
                binding.lvChatSent.adapter = sentAdapter

                val recivedAdapter = ArrayAdapter(this, R.layout.simple_list_item_1, recivedMesseges)
                binding.lvChatSent.adapter = recivedAdapter
        */
               val recivedAdapter = ArrayAdapter(this, R.layout.simple_list_item_1, results)
                binding.lvChatSent.adapter = recivedAdapter
    }

    companion object {
        private const val TAG = "ChatActivity"
    }
}
