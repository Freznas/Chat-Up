package com.example.chatup

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter


class MessagesReceivedAdapter(context: Context, private val messages: List<String>) :
    ArrayAdapter<String>(context, 0, messages) {
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
                .inflate(R.layout.listview_received_messages, parent, false)
        }

        return itemView!!


    }


}