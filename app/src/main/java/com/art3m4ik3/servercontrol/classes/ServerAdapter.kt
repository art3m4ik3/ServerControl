package com.art3m4ik3.servercontrol.classes

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import com.art3m4ik3.servercontrol.R

class ServerAdapter(private val context: Context, private val servers: MutableList<Server>) : BaseAdapter() {
    override fun getCount(): Int = servers.size

    override fun getItem(position: Int): Server = servers[position]

    override fun getItemId(position: Int): Long = position.toLong()

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view: View = convertView ?: LayoutInflater.from(context).inflate(R.layout.server_list_item, parent, false)
        val server = getItem(position)

        val ipTextView = view.findViewById<TextView>(R.id.ipTextView)
        ipTextView.text = server.ip

        return view
    }
}