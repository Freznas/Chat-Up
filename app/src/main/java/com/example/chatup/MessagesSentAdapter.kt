package com.example.chatup

import android.content.Context


import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView

class MessagesSentAdapter(context: Context,private  val messages: List<Message>,var user: String) :
    ArrayAdapter<Message>(context, 0, messages) {
    private val USER_VIEW_TYPE = 0
    private val OTHER_VIEW_TYPE = 1

    override fun getItemViewType(position: Int): Int {
        val item = messages[position]
        return if (item.sender == user) {
            USER_VIEW_TYPE
        } else {
            OTHER_VIEW_TYPE
        }
    }
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val viewType = getItemViewType(position)
        var itemView = convertView

        if (itemView == null) {
            val inflater = LayoutInflater.from(context)
            itemView = when (viewType) {
                USER_VIEW_TYPE -> inflater.inflate(R.layout.listview_sent_messages, parent, false)
                OTHER_VIEW_TYPE -> inflater.inflate(R.layout.listview_received_messages, parent, false)
                else -> throw IllegalArgumentException("Invalid view type")
            }
        }
        val tv = itemView!!.findViewById<TextView>(R.id.tv_message)
        tv.text = messages[position].text
        return itemView!!
    }
}
