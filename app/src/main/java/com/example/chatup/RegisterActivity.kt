package com.example.chatup
// Activity to handle input of user information to create a new User.
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.example.chatup.databinding.ActivityRegisterBinding
import java.util.UUID

class RegisterActivity : AppCompatActivity() {
    var userDao = UserDao()

    lateinit var binding: ActivityRegisterBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)


        binding.btnRegUser.setOnClickListener {
            registerUser()

        }
    }

    private fun registerUser() {
        try {
            val name = binding.etRegName.text.toString()
            val password1 = binding.etRegPassword1.text.toString()
            val password2 = binding.etRegPassword2.text.toString()
            val email = binding.etRegEmail.text.toString()


            //Forcing user to fill every field
            if (name.isEmpty() || password1.isEmpty() || password2.isEmpty() || email.isEmpty()) {
                Toast.makeText(this, "All fields are required", Toast.LENGTH_SHORT).show()
                return
            }
            //Forcing user to use the correct pattern for email
            val emailPattern = Regex("[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+")
            if (!email.matches(emailPattern)) {
                Toast.makeText(this, "Invalid email format", Toast.LENGTH_SHORT).show()
                return
            }
            //Checks if the 2 filled passwords match
            if (password1 != password2) {
                Toast.makeText(this, "The passwords doesnt match", Toast.LENGTH_SHORT).show()
                return
            }

            //Regs user
            val user = User(UUID.randomUUID().toString(), name, password1, email)
            userDao.addUser(user) { success ->
                //If callback is true create user, if not give the information to the user
                if (success) {
                    Toast.makeText(this, "User registered", Toast.LENGTH_SHORT).show()
                    //Closes activity
                    finish()
                } else {
                    Toast.makeText(this, "Username is already used, try another one", Toast.LENGTH_SHORT).show()
                }
            }
        }catch (e: Exception){
            Log.e("Error", "Failed to add user ${e.message.toString()}")
        }
    }

}