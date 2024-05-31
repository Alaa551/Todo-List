package com.example.todolist

import android.content.Context
import com.example.todolist.database.Database
import com.example.todolist.database.model.User

class UserController(context: Context) {
    val db: Database = Database(context)


    fun signUp(user: User)= if(isEmailExist(user.email)){ false}
    else {db.insertUser(user) }


    private fun isEmailExist(email:String): Boolean {
        val users: ArrayList<User>? = db.getAllUsers()
        if (users != null) {
            for (u in users) {
                if (u.email== email) {
                    return true
                }
            }
        }
        return false

    }

    fun signIn(email: String,pass:String):Int{
        return db.getUser(email, pass)

    }


    }



