package com.example.chatup

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.chatup.databinding.ActivityProfileBinding
//Activity to display you profile, update with a profile picture and information about you.
class ProfileActivity : AppCompatActivity() {
    lateinit var binding: ActivityProfileBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

//        val selectedUser = intent.getSerializableExtra("selectedUser") as User


        binding.btnAddFriend.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }
        binding.btnChatUp.setOnClickListener {

            val intent = Intent(this, ChatActivity::class.java)
//            intent.putExtra("selectedUser", selectedUser)
            startActivity(intent)
        }
        binding.btnBlock.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }
        binding.btnRemove.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }



        val userName = intent.getStringExtra("name")
        val userPresentation = intent.getStringExtra("presentation")
        //Way to handle picture
        val userProfilePicture = intent.getStringExtra("profilepicture")

        binding.profileUsername.text = userName
        binding.profileDescription.text = userPresentation


    }
}