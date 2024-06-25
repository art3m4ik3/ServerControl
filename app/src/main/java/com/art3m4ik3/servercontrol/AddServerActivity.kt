package com.art3m4ik3.servercontrol

import android.app.Activity
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.art3m4ik3.servercontrol.classes.Server
import com.art3m4ik3.servercontrol.databinding.ActivityAddServerBinding

class AddServerActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAddServerBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddServerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.saveButton.setOnClickListener {
            val server = Server(
                binding.ipEditText.text.toString(),
                binding.userEditText.text.toString(),
                binding.passwordEditText.text.toString(),
                binding.portEditText.text.toString().toInt()
            )
            val resultIntent = intent
            resultIntent.putExtra("server", server)
            setResult(Activity.RESULT_OK, resultIntent)
            finish()
        }
    }
}