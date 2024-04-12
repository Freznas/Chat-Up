package com.example.chatup
// Chat Up ! Chattapplication with Achievments, Build with MVC Structure.
// Actvity to sign in
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.chatup.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding
    var userDao = UserDao()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnRegister.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }
        binding.btnSignin.setOnClickListener {
            login()
        }

    }

    fun login() {
        val username = binding.username.text.toString()
        val password = binding.password.text.toString()

        userDao.checkUserCredentials(username, password) { validCredentials ->
            if (validCredentials) {
                val intent = Intent(this, ConversationsActivity::class.java)

                startActivity(intent)
            } else {
                Toast.makeText(this, "invalid username or password! ", Toast.LENGTH_SHORT).show()
            }
        }
    }
}