package com.example.chatup

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.chatup.databinding.ActivityProfileBinding
import com.google.gson.Gson

//Activity to display you profile, update with a profile picture and information about you.
class ProfileActivity : AppCompatActivity() {
    lateinit var binding: ActivityProfileBinding
    var userDao = UserDao()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)


        val userProfilePicture = intent.getStringExtra("profilepicture")
//        val selectedUser = intent.getSerializableExtra("selectedUser") as User


//        val userName = intent.getStringExtra("name")
//        val userPresentation = intent.getStringExtra("presentation")
        //Way to handle picture
//        val userProfilePicture = intent.getStringExtra("profilepicture")

        // Get data from intent
       val (userName, userPresentation) = extractUserData(intent)
        binding.profileUsername.text = userName
        binding.profileDescription.text = userPresentation


        binding.btnChatUp.setOnClickListener {
            val intent = Intent(this, ChatActivity::class.java)
            intent.putExtra("reciver", userName)
            startActivity(intent)
        }
        binding.btnAddFriend.setOnClickListener {
            addFriend()
        }
        binding.btnRemove.setOnClickListener {
            removeFriend()
        }
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
        val prefs = getSharedPreferences("com.example.com.example.pong_extreme.prefs", MODE_PRIVATE)
        val json = prefs.getString("user", "")
        val gson = Gson()
        val user = gson.fromJson(json, User::class.java)
        return  user
    }

}