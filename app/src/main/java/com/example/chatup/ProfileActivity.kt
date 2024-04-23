package com.example.chatup

import android.R
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

import android.widget.Toast
import androidx.core.view.isGone

import com.example.chatup.databinding.ActivityProfileBinding
import com.google.gson.Gson


//Activity to display you profile, update with a profile picture and information about you.
class ProfileActivity : AppCompatActivity() {
    lateinit var binding: ActivityProfileBinding
    var userDao = UserDao()
    lateinit var currentUser: User
    lateinit var soundManager: SoundManager
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        soundManager = SoundManager(this)


        currentUser = getUser()

        val profileUserId = intent.getStringExtra("userId")

        //If you are looking at your own profile
        if (profileUserId == currentUser.id) {
            hideAllButtons()
        }
        // Get data from intent
       val (userName, userPresentation) = extractUserData(intent)
        val user = getUser()
        loadUser()

        binding.btnUpdate.setOnClickListener {
            updateUser()
        }
        binding.btnDelete.setOnClickListener {
            deleteUser()
        }
        binding.btnChatUp.setOnClickListener {
            val intent = Intent(this, ChatActivity::class.java)
            intent.putExtra("receiver", userName)
            soundManager.chatUpSound()
            startActivity(intent)
        }
        binding.btnAddFriend.setOnClickListener {
            addFriend()
        }
        binding.btnRemove.setOnClickListener {
            removeFriend()
        }
    }


    fun loadUser()
    {
        val user = getUser()
        binding.profileUsername.text = user.name
        val  presentation = binding.profileDescription
        presentation.setText(user.presentation)
    }
    override fun onResume() {
        super.onResume()
        loadUser()
    }
    private fun updateUser() {
        var user = getUser()
        user.presentation = binding.profileDescription.text.toString()
        userDao.updateUser(user)
        Toast.makeText(this, "User updated succesfully!", Toast.LENGTH_SHORT).show()
        val prefs = getSharedPreferences("com.example.chatup", MODE_PRIVATE)
        val editor: SharedPreferences.Editor = prefs.edit()
        val gson = Gson()
        val json: String = gson.toJson(user)
        editor.putString("user", json)
        editor.apply()
    }
    private fun deleteUser()
    {
        val user = getUser()
        userDao.deleteUser(user)
        Toast.makeText(this, "User deleted succesfully!", Toast.LENGTH_SHORT).show()

        val intent = Intent(this, MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
        startActivity(intent)
        finish()

    override fun onDestroy() {
        super.onDestroy()
        soundManager.release()
    }

    private fun hideAllButtons() {
        binding.btnAddFriend.visibility = View.GONE
        binding.btnRemove.visibility = View.GONE
        binding.btnChatUp.visibility = View.GONE

    }

    //Function to load relevant information to profile
    fun extractUserData(intent: Intent): Triple<String?, String?, String?> {
        return Triple(
            intent.getStringExtra("name"),
            intent.getStringExtra("presentation"),
            intent.getStringExtra("profilepicture")
        )
    }
    private fun addFriend() {
        val currentUser = getUser()
        val friendUser = intent.getSerializableExtra("frienduser") as? User

        if (currentUser != null && friendUser != null) {
            // Kontrollera om vän redan finns i listan

            userDao.addFriend(currentUser, friendUser) { success ->
                if (success) {
                    Toast.makeText(this, "Friend added", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this, "Failed! Friend is already added", Toast.LENGTH_SHORT).show()
                }
            }
        } else {
            Toast.makeText(this, "User or friend data is missing", Toast.LENGTH_SHORT).show()
        }
    }

    private fun removeFriend() {
        val currentUser = getUser()
        val friendUser = intent.getSerializableExtra("frienduser") as? User

        if (currentUser != null && friendUser != null) {
            userDao.removeFriend(currentUser, friendUser) { success ->
                if (success) {
                    Toast.makeText(this, "Friend removed", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this, "Failed! Friend is already removed", Toast.LENGTH_SHORT).show()
                }
            }
        } else {
            Toast.makeText(this, "User or friend data is missing", Toast.LENGTH_SHORT).show()
        }
    }
    fun getUser(): User
    {
        val prefs = getSharedPreferences("com.example.chatup", MODE_PRIVATE)
        val json = prefs.getString("user", "")
        val gson = Gson()
        val user = gson.fromJson(json, User::class.java)
        return  user
    }

}