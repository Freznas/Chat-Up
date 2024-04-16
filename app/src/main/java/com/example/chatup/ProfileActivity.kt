package com.example.chatup

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.chatup.databinding.ActivityProfileBinding

//Activity to display you profile, update with a profile picture and information about you.
class ProfileActivity : AppCompatActivity() {
    lateinit var binding: ActivityProfileBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val userName = intent.getStringExtra("name")
        val userPresentation = intent.getStringExtra("presentation")
        //Way to handle picture
        val userProfilePicture = intent.getStringExtra("profilepicture")

        binding.profileUsername.text = userName
        binding.profileDescription.text = userPresentation

        binding.button3.setOnClickListener {
            val intent = Intent(this, ChatActivity::class.java)
            intent.putExtra("reciver", userName)
            startActivity(intent)
        }
    }
}