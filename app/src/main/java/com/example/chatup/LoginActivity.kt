package com.example.chatup
// Activity for handling input of login information to log in the correct user.
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.chatup.databinding.ActivityLoginBinding

class LoginActivity : AppCompatActivity() {
    lateinit var binding: ActivityLoginBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}