package com.example.todolist.database.model

class Task(var id:Int, var taskName:String,var userId:Int,var status:Int) {
    constructor(taskName: String) : this(0,taskName,0,0)
    constructor(id: Int,taskName: String) : this(id,taskName,0,0)
}