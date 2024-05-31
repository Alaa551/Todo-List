package com.example.todolist.ui.adapter.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.View.OnClickListener
import android.widget.Toast
import com.example.todolist.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity(), OnClickListener{
    lateinit var binding : ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.button2.setOnClickListener(this)
        binding.button.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when(v){
            binding.button -> startActivity(Intent(this, CreateAnAccount::class.java))
            binding.button2 -> startActivity(Intent(this, SignIn::class.java))
            else -> Toast.makeText(this,"error",Toast.LENGTH_LONG).show()

        }
    }
}