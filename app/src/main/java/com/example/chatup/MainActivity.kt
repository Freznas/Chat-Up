package com.example.chatup
// Chat Up ! Chattapplication with Achievments, Build with MVC Structure.
// Actvity to sign in
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.example.chatup.databinding.ActivityMainBinding
import com.google.gson.Gson

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
         userDao.checkUserCredentials(username, password){ validCredentials ->
             if (validCredentials) {
                 val intent = Intent(this, ConversationsActivity::class.java)
                 userDao.getUserByUserName(username){ user ->
                       if (user!=null) {
                           println(user.name)
                           val prefs = getSharedPreferences("com.example.chatup.prefs", MODE_PRIVATE)
                           val editor: SharedPreferences.Editor = prefs.edit()
                           val gson = Gson()
                           val json: String = gson.toJson(user)
                           editor.putString("user", json)
                           editor.apply()

                           intent.putExtra("user", user )
                           startActivity(intent)
                       }
                 }
             } else {
                 Toast.makeText(this, "invalid username or password! ", Toast.LENGTH_SHORT).show()
             }
         }
    }
}