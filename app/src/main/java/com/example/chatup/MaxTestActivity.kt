package com.example.chatup

import android.R
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.chatup.databinding.ActivityMainBinding
import com.example.chatup.databinding.ActivityMaxTestBinding
import java.lang.Exception
import java.util.UUID

class MaxTestActivity : AppCompatActivity() {
    lateinit var binding: ActivityMaxTestBinding
    var userDao = UserDao()

    var selectedItemPosition = -1
    var currentItem: User? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMaxTestBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.maxLvUsers.onItemClickListener= AdapterView.OnItemClickListener { parent, view, position, id ->
            if(selectedItemPosition != -1)
            {
                val tempPos = parent.getChildAt(selectedItemPosition)
                tempPos.setBackgroundColor(Color.TRANSPARENT)
            }

            selectedItemPosition = position
            currentItem = parent.getItemAtPosition(position) as User
            view.setBackgroundColor(Color.GREEN)
        }

 binding.maxBtnAdd.setOnClickListener {
      addUser()
  }
  binding.maxBtnGet.setOnClickListener {
      userDao.getAllUsers(this)
  }

        binding.maxBtnUpdate.setOnClickListener {
            updateUser()
            userDao.getAllUsers(this)
        }

  binding.maxLvUsers.onItemLongClickListener =
  AdapterView.OnItemLongClickListener { parent, view, position, id ->
      val selectedUser = parent.getItemAtPosition(position) as User
      userDao.deleteUser(selectedUser)
      true
  }
}

fun showUsers(users: ArrayList<User>) {
  val adapter = ArrayAdapter(this, R.layout.simple_list_item_1, users)
  binding.maxLvUsers.adapter = adapter
}

    fun updateUser()
    {
        val name = binding.maxEtName.text.toString()
        val password = binding.maxEtPassword.text.toString()
        val email = binding.maxEtEmail.text.toString()
        val user = User(currentItem!!.id, name, password, email  )
        println(user.id)
        userDao.updateUser(user)
        userDao.getAllUsers(this)
    }
fun addUser() {

  try {

      val name = binding.maxEtName.text.toString()
      val password = binding.maxEtPassword.text.toString()
      val email = binding.maxEtEmail.text.toString()
      val user = User(UUID.randomUUID().toString(), name, password, email )
      userDao.addUser(user)
  } catch (e: Exception) {
      Log.e("Error", e.message.toString())
  }
}
  }
