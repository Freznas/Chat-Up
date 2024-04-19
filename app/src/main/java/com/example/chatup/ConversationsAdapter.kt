package com.example.chatup

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView

class ConversationsAdapter(
    context: Context,
    conversations: MutableList<Conversation>,
    var currentUser: User?
//    var otherUser: String,
//    var conversationIDs: List<String>
) :
    ArrayAdapter<Conversation>(context, 0, conversations) {


    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        var itemView = convertView
        val viewHolder: ViewHolder

        if (itemView == null) {
            itemView = LayoutInflater.from(context)
                .inflate(R.layout.listview_conversations, parent, false)
            viewHolder = ViewHolder(itemView)
            itemView.tag = viewHolder
        } else {
            viewHolder = itemView.tag as ViewHolder
        }
        val textviewUsername = viewHolder.textviewUsername

        val conversation = getItem(position)
//        val textviewUsername = convertView!!.findViewById<TextView>(R.id.tv_username)

        val result = conversation?.users?.find { it.name!= currentUser?.name }
        val user = conversation?.users?.get(position)


        textviewUsername.text = result?.name

        Log.i("user", " ${user?.name} fetched")
        viewHolder.bind(conversation)
        return itemView!!

    }


    private class ViewHolder(itemView: View) {
        val textviewUsername: TextView = itemView.findViewById(R.id.tv_username)

        fun bind(conversation: Conversation?) {
            conversation?.let {

            }

        }
    }
}