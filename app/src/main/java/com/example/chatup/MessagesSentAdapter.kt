package com.example.chatup

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView

class MessagesSentAdapter(context: Context, private val messages: List<Message>) :
    ArrayAdapter<Message>(context, 0, messages) {

    override fun getViewTypeCount(): Int {
        return 1
    }

    override fun getItemViewType(position: Int): Int {
        return 0
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        var itemView = convertView
        if (itemView == null) {
            itemView = LayoutInflater.from(context)
                .inflate(R.layout.listview_sent_messages, parent, false)
        }
        val messageTextView: TextView = itemView!!.findViewById(R.id.tv_sent_message)
//      messageTextView.text = getItem(position)
        return itemView!!


    }
}
