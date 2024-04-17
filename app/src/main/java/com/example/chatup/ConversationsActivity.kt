package com.example.chatup
//Activity to display the different conversations active by the logged in user
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.chatup.databinding.ActivityConversationsBinding

class ConversationsActivity : AppCompatActivity() {
    lateinit var binding: ActivityConversationsBinding
    var currentUser: User? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityConversationsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        currentUser = intent.getSerializableExtra("user") as? User

        binding.btnMyProfile.setOnClickListener {
            navigateToProfile(currentUser!!)

        }

        binding.btnSearchUser.setOnClickListener {
            val intent = Intent(this, SearchUserActivity::class.java)
            startActivity(intent)
        }


    }
    fun navigateToProfile(user: User) {
        val intent = Intent(this, ProfileActivity::class.java)
        intent.putExtra("userId", user.id)
        intent.putExtra("name", user.name)
        intent.putExtra("presentation", user.presentation)
        intent.putExtra("profilepicture", user.profilePicture)
        startActivity(intent)
    }
}