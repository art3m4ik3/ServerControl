package com.art3m4ik3.servercontrol

import android.os.AsyncTask
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.art3m4ik3.servercontrol.classes.Server
import com.art3m4ik3.servercontrol.databinding.ActivityConsoleBinding
import com.jcraft.jsch.*
import java.io.InputStream

class ConsoleActivity : AppCompatActivity() {
    private lateinit var binding: ActivityConsoleBinding
    private lateinit var server: Server

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityConsoleBinding.inflate(layoutInflater)
        setContentView(binding.root)

        server = intent.getSerializableExtra("server") as Server

        binding.executeButton.setOnClickListener {
            val command = binding.commandEditText.text.toString()
            if (command.isNotEmpty()) {
                ExecuteCommandTask(server, command) { output ->
                    binding.outputTextView.text = output
                }.execute()
            }
        }
    }

    private class ExecuteCommandTask(
        private val server: Server,
        private val command: String,
        private val callback: (String) -> Unit
    ) : AsyncTask<Void, Void, String>() {

        override fun doInBackground(vararg params: Void?): String {
            return try {
                val jsch = JSch()
                val session: Session = jsch.getSession(server.username, server.ip, server.port)
                session.setPassword(server.password)

                val config = java.util.Properties()
                config["StrictHostKeyChecking"] = "no"
                session.setConfig(config)

                session.connect()

                val channel = session.openChannel("exec") as ChannelExec
                channel.setCommand(command)

                val input: InputStream = channel.inputStream
                channel.connect()

                val output = input.bufferedReader().use { it.readText() }

                channel.disconnect()
                session.disconnect()

                output
            } catch (e: Exception) {
                e.message ?: "Ошибка при выполнении команды"
            }
        }

        override fun onPostExecute(result: String) {
            super.onPostExecute(result)
            callback(result)
        }
    }
}