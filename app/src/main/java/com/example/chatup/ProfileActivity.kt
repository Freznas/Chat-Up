package com.example.chatup

import android.content.Intent
import android.os.Bundle
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

    }


    //Function to load relevant information to profile
    fun extractUserData(intent: Intent): Triple<String?, String?, String?> {
        val source = intent.getStringExtra("source")
        return if (source == "ConversationsActivity") {
            Triple(
                intent.getStringExtra("name"),
                intent.getStringExtra("presentation"),
                intent.getStringExtra("profilepicture")
            )
        } else {
            Triple(
                intent.getStringExtra("searchedName"),
                intent.getStringExtra("searchedPresentation"),
                intent.getStringExtra("searchedProfilepicture")
            )
        }
    }



}