package com.example.chatup
//Activity to display the different conversations active by the logged in user

import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import com.example.chatup.databinding.ActivityConversationsBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class ConversationsActivity : AppCompatActivity() {
    lateinit var binding: ActivityConversationsBinding
    lateinit var currentUser: User
    lateinit var conversationsAdapter: ConversationsAdapter


    var userDao = UserDao()
    var conversationDAO = ConversationDao()
    private lateinit var friendAdapter: ArrayAdapter<String>


    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        binding = ActivityConversationsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        //Spinner adapter
        friendAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item)
        friendAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spinnerFriends.adapter = friendAdapter
        currentUser = intent.getSerializableExtra("user") as User
        conversationDAO.getUserConversations(currentUser.name!!)
        { conversations ->
            this@ConversationsActivity.conversationsAdapter =
                ConversationsAdapter(this, conversations, currentUser)
            binding.lvConversations.adapter = this@ConversationsActivity.conversationsAdapter
        }


        binding.btnMyProfile.setOnClickListener {
            navigateToProfile(currentUser)
        }

        binding.btnSearchUser.setOnClickListener {
            val intent = Intent(this, SearchUserActivity::class.java)
            startActivity(intent)
        }

        binding.lvConversations.setOnItemClickListener { parent, view, position, id ->
            val selectedConversation = conversationsAdapter.getItem(position)

            selectedConversation?.let { conversation ->
                val otherUser = conversation.users.find { user -> user.id != currentUser.id }

                otherUser?.let { user ->
                    val intent = Intent(this, ChatActivity::class.java).apply {

                        putExtra("receiver", otherUser.name)
                    }

                    startActivity(intent)
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        // Update friendslist when returning to activity
        loadCurrentUserFriendsSpinner()
    }

    private fun loadCurrentUserFriendsSpinner() {
        GlobalScope.launch(Dispatchers.Main) {
            // Check if the current user is not null
            currentUser?.let { user ->
                // Get all friends for the current user
                val friends = userDao.getAllFriendsForUser(user.id)
                // Extract the names of the friends
                val friendNames = friends.map { it.name }
                friendAdapter.clear()
                friendAdapter.addAll(friendNames)
                friendAdapter.notifyDataSetChanged()
            }
        }
    }

    fun navigateToProfile(user: User) {
        val intent = Intent(this, ProfileActivity::class.java)
        intent.putExtra("user", user)
        intent.putExtra("userId", user.id)
        intent.putExtra("name", user.name)
        intent.putExtra("presentation", user.presentation)
        intent.putExtra("profilepicture", user.profilePicture)
        startActivity(intent)
    }
}




