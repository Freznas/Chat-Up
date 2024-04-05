package com.example.chatup
//Activity to display the different conversations active by the logged in user
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.chatup.databinding.ActivityConversationBinding

class ConversationActivity : AppCompatActivity() {
    lateinit var binding: ActivityConversationBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityConversationBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}