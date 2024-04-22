package com.example.chatup

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.core.view.isGone
import com.example.chatup.databinding.ActivityProfileBinding
import com.google.gson.Gson

//Activity to display you profile, update with a profile picture and information about you.
class ProfileActivity : AppCompatActivity() {
    lateinit var binding: ActivityProfileBinding
    var userDao = UserDao()
    lateinit var currentUser: User
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)


        currentUser = getUser()

        val profileUserId = intent.getStringExtra("userId")

        //If you are looking at your own profile
        if (profileUserId == currentUser.id) {
            hideAllButtons()
        }
        // Get data from intent
       val (userName, userPresentation) = extractUserData(intent)
        binding.profileUsername.text = userName
        binding.profileDescription.text = userPresentation

        binding.btnChatUp.setOnClickListener {
            val intent = Intent(this, ChatActivity::class.java)
            intent.putExtra("receiver", userName)
            startActivity(intent)
        }
        binding.btnAddFriend.setOnClickListener {
            addFriend()
        }
        binding.btnRemove.setOnClickListener {
            removeFriend()
        }
    }

    private fun hideAllButtons() {
        binding.btnAddFriend.visibility = View.GONE
        binding.btnRemove.visibility = View.GONE
        binding.btnChatUp.visibility = View.GONE
    }

    //Function to load relevant information to profile
    fun extractUserData(intent: Intent): Triple<String?, String?, String?> {
        return Triple(
            intent.getStringExtra("name"),
            intent.getStringExtra("presentation"),
            intent.getStringExtra("profilepicture")
        )
    }
    private fun addFriend() {
        val currentUser = getUser()
        val friendUser = intent.getSerializableExtra("frienduser") as? User

        if (currentUser != null && friendUser != null) {
            // Kontrollera om vän redan finns i listan

            userDao.addFriend(currentUser, friendUser) { success ->
                if (success) {
                    Toast.makeText(this, "Friend added", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this, "Failed! Friend is already added", Toast.LENGTH_SHORT).show()
                }
            }
        } else {
            Toast.makeText(this, "User or friend data is missing", Toast.LENGTH_SHORT).show()
        }
    }

    private fun removeFriend() {
        val currentUser = getUser()
        val friendUser = intent.getSerializableExtra("frienduser") as? User

        if (currentUser != null && friendUser != null) {
            userDao.removeFriend(currentUser, friendUser) { success ->
                if (success) {
                    Toast.makeText(this, "Friend removed", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this, "Failed! Friend is already removed", Toast.LENGTH_SHORT).show()
                }
            }
        } else {
            Toast.makeText(this, "User or friend data is missing", Toast.LENGTH_SHORT).show()
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

}