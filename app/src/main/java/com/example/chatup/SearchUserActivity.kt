package com.example.chatup
// Activity to handle finding users and adding friends/Block Users.
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.SearchView
import android.widget.Toast
import com.example.chatup.databinding.ActivitySearchUserBinding

class SearchUserActivity : AppCompatActivity() {
//    Declaring variables
    lateinit var binding: ActivitySearchUserBinding
    lateinit var searchView: SearchView
    lateinit var userDao: UserDao
    lateinit var adapter: SearchUserAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivitySearchUserBinding.inflate(layoutInflater)
        setContentView(binding.root)

        userDao = UserDao()

        searchView = findViewById(R.id.sv_search_user)

        adapter = SearchUserAdapter(this, ArrayList())
        binding.lvSearchUser.adapter = adapter

        binding.lvSearchUser.setOnItemClickListener { _, _, position, _ ->
            val selectedUsername = adapter.getItem(position)
            val intent = Intent(this, ProfileActivity::class.java)
            intent.putExtra("frienduser", selectedUsername)
            intent.putExtra("friendsid", selectedUsername?.id)
            intent.putExtra("name", selectedUsername?.name)
            intent.putExtra("presentation", selectedUsername?.presentation)
            intent.putExtra("profilepicture", selectedUsername?.profilePicture.toString())



            startActivity(intent)
        }



        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                if (newText!!.isNotEmpty()) {
                    performSearch(newText)
                } else {
                    adapter.clear()
                }
                return true

            }
        })
    }

    // Function to search based on query
    fun performSearch(query: String) {
            userDao.searchUsers(query) { users ->
                adapter.clear()
              adapter.addAll(users)
            }
        }

    }

