package com.example.chatup
// Activity to handle input of user information to create a new User.
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.chatup.databinding.ActivityRegisterBinding

class RegisterActivity : AppCompatActivity() {

    lateinit var binding: ActivityRegisterBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}