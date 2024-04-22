package com.example.chatup

import android.content.Context

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView


class MessagesSentAdapter(context: Context, messages: List<Message>,var user: String) :
    ArrayAdapter<Message>(context, 0, messages) {

    override fun getViewTypeCount(): Int {
        return 1
    }

    override fun getItemViewType(position: Int): Int {
        return 0
    }
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        var itemView = convertView
        val viewHolder : ViewHolder
        val message =  getItem(position) as Message
        if (itemView == null) {
            val layoutId = if (message.sender == user) {
                R.layout.listview_sent_messages
            } else {
                R.layout.listview_received_messages
            }
           itemView = LayoutInflater.from(context)
               .inflate(layoutId, parent, false)
            viewHolder = ViewHolder()
            viewHolder.messageTextView = itemView.findViewById(R.id.tv_message)
            itemView.tag = viewHolder
//            inflate(R.layout.listview_sent_messages, parent, false)
        } else {
            viewHolder = itemView.tag as ViewHolder
        }
        // Set message content
        viewHolder.messageTextView.text = message.text
        return itemView!!


    }
    private class ViewHolder {
        lateinit var messageTextView: TextView
    }
}
