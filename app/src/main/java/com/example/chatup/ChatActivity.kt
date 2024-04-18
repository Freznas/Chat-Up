package com.example.chatup
// Activity to Display the active Chat between two or more users.
import android.R
import android.os.Bundle
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import com.example.chatup.databinding.ActivityChatBinding
import com.google.firebase.firestore.FirebaseFirestore
import com.google.gson.Gson
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

           fetchMessages()
           binding.btnSend.setOnClickListener {
           val message = binding.etChatMessage.text.toString()
           sendMessage( message)
       }

    }
    private fun sendMessage( message:String) {
        var users = ArrayList<User>()
        val user = getUser()
        users.add(user)
        // get other chat participant
        var reciever = intent.getStringExtra("reciver") as String
        var userDao  = UserDao()
         userDao.getUserByUserName(reciever){ dude ->
             if(dude !=null)
             {
                users.add(dude)
             }
             // check if conversation exists returns id == -1 if there is no active conversation
             conversationDao.getConversation(user.name!!,reciever, this){ conversation->
                 if(conversation.id!= "-1")
                 {
                    conversationDao.addMessage(conversation, user.name!!, message)
                 }
                 else if(conversation.id == "-1")
                 {
                     var msgs = ArrayList<Message>()
                     msgs.add(Message(UUID.randomUUID().toString(),user.name!!, message))
                     var newConversation = Conversation(UUID.randomUUID().toString(), msgs , users)
                     conversationDao.createConversation(newConversation)
                     fetchMessages()
                 }
//                 fetchMessages()
             }
    }
    }
    private fun fetchMessages() {
        val user = getUser()
        var reciever = intent.getStringExtra("reciver") as String

        conversationDao.getConversation(user.name!!,reciever, this){ conversation->
            conversationDao.getMessages(conversation, this)
            }
    }
    fun showMessages(results: ArrayList<Message>){
        val user = getUser()
        if(user !=null) {
            val sentAdapter = MessagesSentAdapter(this, results, user.name!!)
            binding.lvChatSent.adapter = sentAdapter
        }
    }
    fun getUser(): User
    {
        val prefs = getSharedPreferences("com.example.chatup", MODE_PRIVATE)
        val json = prefs.getString("user", "")
        val gson = Gson()
        val user = gson.fromJson(json, User::class.java)
        return  user
    }
//#region TestingDummyData
    fun createDummyConvo(){

        var users: ArrayList<User> = ArrayList<User>()
        users.add(User("1","1","1","1"))
        users.add(User("1","1","1","1"))

   val sentMessages = ArrayList<Message>() //("sent message 1 ", "sent message 2 ", "sent message 3 ")
    sentMessages.add(Message("qewqeq","God","Lets fuck shit up Lmao"))
    sentMessages.add(Message("qewqeq","Satan","woah dude chill"))
        var dummyConvo = Conversation("a7d997be-5fa6-438f-914e-d2c09171fd6a", sentMessages ,  users )
//            conversationDao.createConversation(dummyConvo) // create conversation and save it in database (change id to random UUID)

//       fetchMessages(dummyConvo)
    }
    //#endregion
    companion object {
        private const val TAG = "ChatActivity"
    }
}
