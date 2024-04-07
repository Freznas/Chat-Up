package com.example.chatup
// Activity to handle finding users and adding friends/Block Users.
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.SearchView
import android.widget.Toast
import com.example.chatup.databinding.ActivitySearchUserBinding



class SearchUserActivity : AppCompatActivity() {
    lateinit var binding: ActivitySearchUserBinding
    lateinit var searchView: SearchView
    lateinit var listView: ListView
    lateinit var userDao: UserDao
    lateinit var adapter: ArrayAdapter<String>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivitySearchUserBinding.inflate(layoutInflater)
        setContentView(binding.root)

        userDao = UserDao()

        searchView = findViewById(R.id.sv_search_user)
        listView = findViewById(R.id.lv_search_user)

        adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1)
        listView.adapter = adapter

        listView.setOnItemClickListener { _, _, position, _ ->
            val selectedUsername = adapter.getItem(position)
            Toast.makeText(this, "Selected user $selectedUsername", Toast.LENGTH_LONG).show()

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
         fun performSearch(query: String) {
            userDao.searchUsers(query) { users ->
                val userNames = users.map { it.name }
                adapter.clear()
                adapter.addAll(userNames)
            }
        }

    }

