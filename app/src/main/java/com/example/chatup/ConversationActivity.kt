package com.example.chatup
//Activity to display the different conversations active by the logged in user
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class ConversationActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_conversation)
    }
}