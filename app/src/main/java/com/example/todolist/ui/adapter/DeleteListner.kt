package com.example.todolist.ui.adapter

interface DeleteListner {
    fun delete(position:Int)
    fun deleteAll(userId:Int)

}