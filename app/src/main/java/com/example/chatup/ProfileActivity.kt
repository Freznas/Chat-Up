package com.example.chatup

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.chatup.databinding.ActivityProfileBinding

//Activity to display you profile, update with a profile picture and information about you.
class ProfileActivity : AppCompatActivity() {
    lateinit var binding: ActivityProfileBinding
    var userDao = UserDao()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Get data from intent
        val (userName, userPresentation) = extractUserData(intent)

        binding.profileUsername.text = userName
        binding.profileDescription.text = userPresentation

        binding.button.setOnClickListener {
            addFriend()
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
        val friendId = intent.getStringExtra("friendsid")
        val userId = intent.getStringExtra("userId")
        if (friendId != null) {
            Log.d("ProfileActivity", "UserID: $userId, FriendID: $friendId")
            userDao.addFriend(userId, friendId) { success ->
                if (success) {

                    Toast.makeText(this, "Vän tillagd", Toast.LENGTH_SHORT).show()
                } else {

                    Toast.makeText(this, "Misslyckades med att lägga till vän", Toast.LENGTH_SHORT)
                        .show()
                }
            }

        } else {
            Log.d("ProfileActivity", "UserID: $userId, FriendID: $friendId")
            Toast.makeText(this, "Vän-ID saknas", Toast.LENGTH_SHORT).show()
        }
    }


}