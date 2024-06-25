package com.art3m4ik3.servercontrol

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.art3m4ik3.servercontrol.classes.Server
import com.art3m4ik3.servercontrol.classes.ServerAdapter
import com.art3m4ik3.servercontrol.databinding.ActivityMainBinding
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var serverAdapter: ServerAdapter
    private val servers = mutableListOf<Server>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        loadServers()
        serverAdapter = ServerAdapter(this, servers)
        binding.serverListView.adapter = serverAdapter

        binding.addServerButton.setOnClickListener {
            val intent = Intent(this, AddServerActivity::class.java)
            startActivityForResult(intent, ADD_SERVER_REQUEST)
        }

        binding.serverListView.setOnItemClickListener { _, _, position, _ ->
            val server = serverAdapter.getItem(position)
            server?.let {
                val intent = Intent(this, ConsoleActivity::class.java)
                intent.putExtra("server", it)
                startActivity(intent)
            }
        }

        binding.serverListView.setOnItemLongClickListener { _, _, position, _ ->
            showDeleteConfirmationDialog(position)
            true
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == ADD_SERVER_REQUEST && resultCode == RESULT_OK) {
            data?.getSerializableExtra("server")?.let {
                val server = it as Server
                servers.add(server)
                serverAdapter.notifyDataSetChanged()
                saveServers()
            }
        }
    }

    private fun saveServers() {
        val sharedPreferences = getSharedPreferences(PREFS_NAME, MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        val gson = Gson()
        val json = gson.toJson(servers)
        editor.putString(SERVERS_KEY, json)
        editor.apply()
    }

    private fun loadServers() {
        val sharedPreferences = getSharedPreferences(PREFS_NAME, MODE_PRIVATE)
        val gson = Gson()
        val json = sharedPreferences.getString(SERVERS_KEY, null)
        if (json != null) {
            val type = object : TypeToken<MutableList<Server>>() {}.type
            val savedServers: MutableList<Server> = gson.fromJson(json, type)
            servers.addAll(savedServers)
        }
    }

    private fun showDeleteConfirmationDialog(position: Int) {
        AlertDialog.Builder(this)
            .setTitle("Удалить сервер")
            .setMessage("Уверены что хотите удалить этот сервер?")
            .setPositiveButton("Удалить") { dialog, _ ->
                servers.removeAt(position)
                serverAdapter.notifyDataSetChanged()
                saveServers()
                dialog.dismiss()
            }
            .setNegativeButton("Отмена") { dialog, _ ->
                dialog.dismiss()
            }
            .show()
    }

    companion object {
        const val ADD_SERVER_REQUEST = 1
        const val PREFS_NAME = "servers_prefs"
        const val SERVERS_KEY = "servers"
    }
}
