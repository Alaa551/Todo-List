package com.example.todolist.ui.adapter.activities

import android.content.Intent
import android.os.Bundle
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.todolist.R
import com.example.todolist.UserController
import com.example.todolist.databinding.ActivityScreen2Binding


class SignIn : AppCompatActivity() {
    //define variables
    lateinit var binding: ActivityScreen2Binding
    lateinit var controller: UserController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityScreen2Binding.inflate(layoutInflater)
        setContentView(binding.root)


        // when click on sign in button
        binding.button.setOnClickListener(View.OnClickListener {
            val emailStr: String = binding.editTextTextEmailAddress.getText().toString()
            val passwordStr: String = binding.editTextTextPassword.getText().toString()
            //check that the user fill all fields
            if (emailStr.isEmpty() || passwordStr.isEmpty()) {
                val builder = AlertDialog.Builder(this@SignIn)
                builder.setMessage("Please enter all fields")
                builder.setPositiveButton("OK", null)
                builder.show()
            } else {
                controller = UserController(this@SignIn)
                val res= controller.signIn(emailStr, passwordStr)
                if (res!=-1) {
                    Toast.makeText(this, "welcome back", Toast.LENGTH_SHORT).show()
                    val intent = Intent(this, AllTasks::class.java)
                    intent.putExtra("user_id",res)
                    startActivity(intent)

                } else {
                    val builder = AlertDialog.Builder(this@SignIn)
                    builder.setMessage("incorrect email or password ,please try again")
                    builder.setPositiveButton("OK", null)
                    builder.show()
                }
            }
        })

        binding.imageViewEyeSignIn.setOnClickListener(View.OnClickListener {
            if ( binding.editTextTextPassword.getTransformationMethod() == HideReturnsTransformationMethod.getInstance()) {
                binding.imageViewEyeSignIn.setImageResource(R.drawable.eye)
                binding.editTextTextPassword.setTransformationMethod(PasswordTransformationMethod.getInstance())
            } else {
                binding.imageViewEyeSignIn.setImageResource(R.drawable.hide_eye)
                binding.editTextTextPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance())
            }
        })



    }
}