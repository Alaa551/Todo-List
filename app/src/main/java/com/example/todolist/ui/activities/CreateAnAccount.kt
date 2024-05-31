package com.example.todolist.ui.adapter.activities

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.example.todolist.UserController
import com.example.todolist.database.model.User
import com.example.todolist.databinding.ActivityCreateAnAccountBinding


class CreateAnAccount : AppCompatActivity() {
    //define variables
    lateinit var binding: ActivityCreateAnAccountBinding
    lateinit var  validation: _root_ide_package_.com.example.todolist.validation.Validation
    lateinit var  controller: UserController
companion object{
    var emailStr: String? = null
    var nameStr: String? =null
    var passwordStr: String? =null
    var confirmPass: String? = null

}
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCreateAnAccountBinding.inflate(layoutInflater)
        setContentView(binding.root)


        //when click on create an account
        binding.buttonSignUp.setOnClickListener(View.OnClickListener {
             emailStr = binding.editTextTextEmailAddressSignUp.getText().toString()
             nameStr = binding.editTextNameSignUp.getText().toString()
             passwordStr = binding.editTextPassSignUp.getText().toString()
             confirmPass = binding.editTextConfirmPasswordSignUp.getText().toString()

            validation = _root_ide_package_.com.example.todolist.validation.Validation()

            //check that the user fill all fields
            controller = UserController(this@CreateAnAccount)
            val user = User(emailStr!!, nameStr!!, passwordStr!!)
            if (validation.checkAllFields(user) && !confirmPass(confirmPass!!, passwordStr!!)) {
                if (controller.signUp(user)) {
                    val intent = Intent(this@CreateAnAccount, SignIn::class.java)
                    startActivity(intent)
                } else {
                    binding.editTextTextEmailAddressSignUp.setError("This email already exists!")
                }
            } else {
                vaidationMesseges(nameStr!!, emailStr!!, passwordStr!!)
            }
        })

    }

    fun vaidationMesseges(
        nameStr: String,
        emailStr: String,
        passwordStr: String,
    ) {

        checkName(nameStr)
        checkEmail(emailStr)
        checkPassword(passwordStr)
        confirmPassMessege()
    }

    fun checkName(nameStr: String) {
        if (nameStr.isEmpty()) {
            binding.editTextNameSignUp.setError("This field is required")
        } else if (!validation.validName(nameStr)) {
            binding.editTextNameSignUp.setError("Invalid name")
        }
    }

    fun checkEmail(emailStr: String) {
        if (emailStr.isEmpty()) {
            binding.editTextTextEmailAddressSignUp.setError("This field is required")
        } else if (!validation.validEmail(emailStr)) {
            binding.editTextTextEmailAddressSignUp.setError("Invalid email")
        }
    }



    fun checkPassword(passwordStr: String) {
        if (passwordStr.isEmpty()) {
            binding.editTextPassSignUp.setError("This field is required")
        } else if (!validation.validPassword(passwordStr)) {
            binding.editTextPassSignUp.setError("Invalid password")
        }
    }

    fun confirmPass(confirmPass:String,passwordStr: String) = confirmPass!=passwordStr

    fun confirmPassMessege(){
        if(confirmPass?.isEmpty() == true){
            binding.editTextConfirmPasswordSignUp.setError("This field is required")

        }
        else if(confirmPass(confirmPass!!, passwordStr!!)){
            binding.editTextConfirmPasswordSignUp.setError("incorrect password")
        }

    }




}