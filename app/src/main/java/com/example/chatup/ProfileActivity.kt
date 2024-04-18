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
    var currentUser: User? = null
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
        //Can only fetch one of them depending on wich activity im coming from
        val currentUser = intent.getSerializableExtra("user") as? User
        val friendUser = intent.getSerializableExtra("frienduser") as? User

        if (currentUser != null && friendUser != null) {
            userDao.addFriend(currentUser, friendUser) { success ->
                if (success) {

                    Toast.makeText(this, "Friend added", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this, "Failed to add friend", Toast.LENGTH_SHORT).show()
                }
            }
        } else {
            Log.i("Damn", "Här har vi den inloggade ${currentUser}")
            Log.i("Damn", "Här har vi kompisen ${friendUser}")
            Toast.makeText(this, "User or friend data is missing", Toast.LENGTH_SHORT).show()
        }
    }

}