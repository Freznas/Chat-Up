package com.example.chatup
// Activity to handle finding users and adding friends/Block Users.
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.SearchView
import android.widget.Toast
import com.example.chatup.databinding.ActivitySearchUserBinding

class SearchUserActivity : AppCompatActivity() {
//    Declaring variables
    lateinit var binding: ActivitySearchUserBinding
    lateinit var searchView: SearchView
    lateinit var listView: ListView
    lateinit var userDao: UserDao
    lateinit var adapter: SearchUserAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivitySearchUserBinding.inflate(layoutInflater)
        setContentView(binding.root)

        userDao = UserDao()

        searchView = findViewById(R.id.sv_search_user)
        listView = findViewById(R.id.lv_search_user)

        adapter = SearchUserAdapter(this, ArrayList())
        listView.adapter = adapter

        listView.setOnItemClickListener { _, _, position, _ ->
            val selectedUsername = adapter.getItem(position)
            Toast.makeText(this, "Selected user $selectedUsername", Toast.LENGTH_LONG).show()
            val intent = Intent(this, ProfileActivity::class.java)
            startActivity(intent)
        }
        // Setting query text listener for SearchView

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
                val userNames = users.map { it.name }
                adapter.clear()
              adapter.addAll(users)
            }
        }

    }

