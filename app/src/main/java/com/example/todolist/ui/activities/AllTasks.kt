package com.example.todolist.ui.adapter.activities

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.todolist.R
import com.example.todolist.database.Database
import com.example.todolist.database.model.Task
import com.example.todolist.databinding.ActivityTasksBinding
import com.example.todolist.ui.adapter.AddTaskListener
import com.example.todolist.ui.adapter.DeleteListner
import com.example.todolist.ui.adapter.RecyclerViewAdapter
import com.example.todolist.ui.adapter.UpdateListner
import com.google.android.material.button.MaterialButton
import com.google.android.material.floatingactionbutton.FloatingActionButton

class AllTasks : AppCompatActivity(), DeleteListner, AddTaskListener, UpdateListner {
    lateinit var binding: ActivityTasksBinding
   private val db= Database(this)
   lateinit var recyclerViewAdapter: RecyclerViewAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTasksBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setContentView(R.layout.activity_tasks)
        val userId = intent.getIntExtra("user_id", 0)

        var tasks = db.getAllTasks(userId)

        val recyclerView: RecyclerView = findViewById(R.id.recyclerviewXml)

        recyclerViewAdapter = RecyclerViewAdapter(tasks!!, this, this, this)
        if (tasks != null) {

            for (task in tasks) {

            }
            val lm: RecyclerView.LayoutManager = LinearLayoutManager(this)


            // add divider between the items
            val dividerItemDecoration = DividerItemDecoration(this, DividerItemDecoration.VERTICAL)
            dividerItemDecoration.setDrawable(getDrawable(R.drawable.divider)!!)
            recyclerView.addItemDecoration(dividerItemDecoration)

            recyclerView.setHasFixedSize(true)
            recyclerView.layoutManager = lm
            recyclerView.adapter = recyclerViewAdapter

            //add Task
            val addButton: FloatingActionButton = findViewById(R.id.addFab)
            addButton.setOnClickListener(View.OnClickListener {
                val inflater = LayoutInflater.from(this)
                val v = inflater.inflate(R.layout.add_new_task, null)
                val taskNameView: EditText? = v.findViewById(R.id.taskName)
                val addDialog = AlertDialog.Builder(this)
                addDialog.setView(v)
                addDialog.setPositiveButton("Save") { dialog, _ ->
                    val taskName: String = taskNameView!!.text.toString()
                    if (!taskName.isEmpty()) {
                        addTask(Task(0, taskName, userId, 0))

                        Toast.makeText(this@AllTasks, "Task added successfully", Toast.LENGTH_SHORT)
                            .show()
                    }
                    dialog.dismiss()

                }

                addDialog.setNegativeButton("Cancel") { dialog, _ ->
                    dialog.dismiss()
                }


                addDialog.create().show()


            })

            // clear all button
            val clearButton: MaterialButton = findViewById(R.id.clearAll)
            clearButton.setOnClickListener(View.OnClickListener {
                deleteAll(userId)

            })

            val completeTasksArrow: ImageView = findViewById(R.id.completedTasks)
            val tasksArrow: ImageView = findViewById(R.id.tasks)
            val swapTasks= findViewById<TextView>(R.id.swapTasks)
            completeTasksArrow.setOnClickListener(View.OnClickListener {
                 recyclerViewAdapter.tasks=db.getCompletedTasks(userId)!!
                recyclerViewAdapter.notifyDataSetChanged()
                completeTasksArrow.setImageResource(0)
                tasksArrow.setImageResource(R.drawable.arrow_forward)
                swapTasks.text="All Tasks    "



            })


            tasksArrow.setOnClickListener(View.OnClickListener {
                recyclerViewAdapter.tasks=db.getAllTasks(userId)!!
                recyclerViewAdapter.notifyDataSetChanged()
                tasksArrow.setImageResource(0)
                completeTasksArrow.setImageResource(R.drawable.arrow_back)
                swapTasks.text="Completed Tasks"

            })

        }
    }




    override fun delete(position: Int) {
        recyclerViewAdapter.deleteTask(position)
    }

    override fun deleteAll(userId:Int) =
       recyclerViewAdapter.deleteAllTasks(userId)

    override fun addTask(task: Task) {
        recyclerViewAdapter.addTask(task)
    }

    override fun updateTask(position: Int,taskName: String) {
        recyclerViewAdapter.updateTask(position,taskName)
    }

    override fun updateStatus(taskId: Int, status: Int,position: Int) {
        recyclerViewAdapter.updateStatus(taskId, status,position)

    }

}



