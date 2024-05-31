package com.example.todolist.ui.adapter

import java.text.FieldPosition

interface UpdateListner {
    fun updateTask(position: Int,taskName: String)
    fun updateStatus(taskId:Int,status:Int,position: Int)
}