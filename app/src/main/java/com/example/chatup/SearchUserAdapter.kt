package com.example.chatup

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView

class SearchUserAdapter(context: Context, users:List<User>):ArrayAdapter<User>(context,0, users) {
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        var convertView = convertView
        val user = getItem(position)

        if (convertView == null){
            convertView = LayoutInflater.from(context).inflate(R.layout.listview_search_user, parent, false)
        }
        val nameTextView = convertView!!.findViewById<TextView>(R.id.username)
        nameTextView.text = user?.name


        return convertView
    }

    fun addAll(users: List<User>) {
        for(user in users){
            clear()
            add(user)

        }

    }

}
