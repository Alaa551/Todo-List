package com.example.todolist.ui.adapter.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.todolist.databinding.ActivityLoadingPageBinding

class LoadingPage : AppCompatActivity() {
    lateinit var binding: ActivityLoadingPageBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoadingPageBinding.inflate(layoutInflater)
        setContentView(binding.root)
     setProgressValue()
    }

    fun setProgressValue(){




        val thread = Thread(Runnable {
            try {
                for(i in 0..100) {
                    val progress = i * 10
                    runOnUiThread {
                        binding.progressBar.progress = progress
                        if (progress==100){
                            intent= Intent(this, MainActivity::class.java)
                            startActivity(intent)
                        }

                    }
                    Thread.sleep(200)
                }
            }
            catch (e:InterruptedException){
                e.printStackTrace()
            }

        }).start()
    }


}