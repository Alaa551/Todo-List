package com.example.todolist.validation

import android.util.Patterns
import com.example.todolist.database.model.User
import java.util.regex.Pattern

class Validation {

        fun validName(name: String): Boolean {
            val pattern = "^[A-Za-z]+$"
            return if (name.isEmpty() || !Pattern.matches(pattern, name)) {
                false
            } else true
        }

        fun validEmail(email: String): Boolean {
            return if (email.isEmpty() || !Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                false
            } else true
        }

        fun validPhoneNo(phone: String): Boolean {
            val pattern = "[0-9]{11}"
            return if (phone.isEmpty() || !Pattern.matches(pattern, phone)) {
                false
            } else true
        }

        fun validPassword(pass: String): Boolean {
            val pattern = "^(?=.*[0-9])" +
                    "(?=.[a-z]).{8,20}$"
            return if (pass.isEmpty() || !Pattern.matches(pattern, pass)) {
                false
            } else true
        }


    fun checkAllFields(user: User): Boolean {
            return if (!validName(user.name) || !validEmail(user.email)
                || !validPassword(user.password)
            ) {
                false
            } else true
        }


    }
