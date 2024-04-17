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
//        val sentMessages = listOf("sent message 1 ", "sent message 2 ", "sent message 3 ")
//        val receivedMessages = listOf("Received message 1 ", "Received message 2")


//        createDummyConvo()

       binding.btnSend.setOnClickListener {
           val message = binding.etChatMessage.text.toString()
           sendMessage( message)
       }

    }

    private fun sendMessage( message:String) {

        var people = ArrayList<User>()
        // User
        val prefs = getSharedPreferences("com.example.com.example.pong_extreme.prefs", MODE_PRIVATE)
        val json = prefs.getString("user","")
        val gson = Gson()
        val user = gson.fromJson(json, User::class.java)
        people.add(user)
//        var dennis = User("30e3f59b-8921-4b86-8cef-3788ccbae23f","dennis", "123","aklds@gmail.com")
//        people.add(dennis)
        // dummy conversation
//        var converstion = Conversation(UUID.randomUUID().toString(), ArrayList<Message>(), people)
//        conversationDao.createConversation(converstion)
        // thae dude user watns to chat with

        var reciever = intent.getStringExtra("reciver") as String
        var userDao  = UserDao()
         userDao.getUserByUserName(reciever){ dude ->
             if(dude !=null)
             {
            people.add(dude)
             }
             // get conversation
             conversationDao.getConversation(user.name!!,reciever,this){ conversation->
                    //return -1 if there is no active conversation
                 if(conversation.id!= "-1")
                 {
                     println("\n\nFound Converasation!")
                     println(conversation)
                    conversationDao.addMessage(conversation, user.name!!, message)
                 }
                 else if(conversation.id == "-1")
                 {
                     // create new convo
                     println("We ballin")
                     var msgs = ArrayList<Message>()
                     msgs.add(Message(UUID.randomUUID().toString(),user.name!!, message))
                     var newConversation = Conversation(UUID.randomUUID().toString(), msgs , people)
                     conversationDao.createConversation(newConversation)
                 }
             }
    }
    }

    private fun fetchMessages(converstion: Conversation) {
        conversationDao.getMessages(converstion,this)
    }

    fun showConversations(results: ArrayList<Conversation>) {
           var a = ArrayList<Message>()
            for(result in results)
            {
                a = result.messages
            }
        showMessages(a)
    }
    fun showMessages(results: ArrayList<Message>){

//        val user  = intent.getSerializableExtra("user") as User
        val prefs = getSharedPreferences("com.example.com.example.pong_extreme.prefs", MODE_PRIVATE)
        val json = prefs.getString("user","")
        val gson = Gson()
        val user = gson.fromJson(json, User::class.java)
       println(user)
        if(user !=null) {
            val sentAdapter = MessagesSentAdapter(this, results, user. name!!)
            binding.lvChatSent.adapter = sentAdapter
        }
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

       fetchMessages(dummyConvo)
    }
    //#endregion
    companion object {
        private const val TAG = "ChatActivity"
    }
}
