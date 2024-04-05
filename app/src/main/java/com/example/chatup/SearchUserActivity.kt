package com.example.chatup
// Activity to handle finding users and adding friends/Block Users.
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.chatup.databinding.ActivitySearchUserBinding
class SearchUserActivity : AppCompatActivity() {
    lateinit var binding: ActivitySearchUserBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivitySearchUserBinding.inflate(layoutInflater)
        setContentView(binding.root)


    }
}