package com.example.todolist.ui.adapter

import com.example.todolist.database.model.Task

interface AddTaskListener {
    fun addTask(task: Task)
}