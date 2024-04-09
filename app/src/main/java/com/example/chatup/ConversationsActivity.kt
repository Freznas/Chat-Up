package com.example.chatup
//Activity to display the different conversations active by the logged in user
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.chatup.databinding.ActivityConversationsBinding

class ConversationsActivity : AppCompatActivity() {
    lateinit var binding: ActivityConversationsBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityConversationsBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}