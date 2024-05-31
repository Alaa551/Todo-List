package com.example.todolist.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView
import com.example.todolist.R
import com.example.todolist.R.layout.task_item
import com.example.todolist.database.Database
import com.example.todolist.database.model.Task


class RecyclerViewAdapter(var tasks: ArrayList<Task>, var context: Context, var updateListner: UpdateListner, var deleteListner: DeleteListner) :
     RecyclerView.Adapter<RecyclerViewAdapter.TaskViewHolder>() {

     private val db = Database(context)


     override fun onCreateViewHolder(
         parent: ViewGroup,
         viewType: Int
     ): TaskViewHolder {
         val v: View =
             LayoutInflater.from(parent.context).inflate(task_item, null, false)

         return TaskViewHolder(v)
     }

     override fun onBindViewHolder(
         holder: TaskViewHolder,
         position: Int
     ) {
         val c: Task = tasks[position]
         holder.taskName.text = c.taskName
         holder.delete.setOnClickListener(View.OnClickListener {
             val builder = AlertDialog.Builder(context)
             builder.setMessage("Are you sure to delete this task?")
             builder.setPositiveButton("Sure") { dialog, _ ->
                 deleteListner.delete(position)
                 Toast.makeText(context, "Task deleted successfully", Toast.LENGTH_SHORT).show()
                 dialog.dismiss()

             }
             builder.setNegativeButton("Cancel") { dialog, _ ->
                 dialog.dismiss()
             }
             builder.show()
         })

         holder.update.setOnClickListener(View.OnClickListener {
             updateInfoDialog(position)
         })

         holder.checkBox.setOnCheckedChangeListener(CompoundButton.OnCheckedChangeListener { buttonView, isChecked ->
             if (isChecked) {
                 updateListner.updateStatus(tasks[position].id, 1,position)
             }
             else {
                 updateListner.updateStatus(tasks[position].id, 0,position)
             }
         })
         updateStatus(holder.checkBox,position)

     }

     override fun getItemCount(): Int {
         return tasks.size
     }

     // holder class for recycler view
     public inner class TaskViewHolder(itemView: View) :
         RecyclerView.ViewHolder(itemView) {
         var taskName: CheckBox = itemView.findViewById(R.id.checkBox)
         var delete: ImageView = itemView.findViewById(R.id.deleteTask)
         var update: ImageView = itemView.findViewById(R.id.updateTask)
         var checkBox: CheckBox = itemView.findViewById(R.id.checkBox)


     }


     fun deleteTask(position: Int) {
         var item = tasks[position]
         db.deleteTask(item.id)
         tasks.removeAt(position)
         notifyItemRemoved(position)
     }


     fun addTask(task: Task) {
         db.insertTask(task)
         task.id = db.getLastTaskId()

         tasks.add(task)
         notifyItemInserted(tasks.size - 1)
     }

     fun updateTask(position: Int, tasName: String) {
         var item = tasks.get(position)
         if (db.updateTask(item, tasName)) {
             tasks[position] = item
             notifyItemChanged(position)
         }

     }

     fun deleteAllTasks(userId: Int) {
         db.deleteAllTasks(userId)
         tasks.clear()
         notifyDataSetChanged()
     }


     fun updateInfoDialog(position: Int) {
         val inflater = LayoutInflater.from(context)
         val v = inflater.inflate(R.layout.update_task, null)
         val taskNameView: EditText? = v.findViewById(R.id.taskName)
         val nowTextName = db.getTask(tasks[position].id)?.taskName
         if (nowTextName != null) {
             taskNameView?.setText(nowTextName)
         } else {
             Toast.makeText(context, "error in update", Toast.LENGTH_SHORT).show()
         }


         val addDialog = AlertDialog.Builder(context)
         addDialog.setView(v)
         addDialog.setPositiveButton("Save") { dialog, _ ->
             val taskName: String = taskNameView!!.text.toString()
             if (taskName != nowTextName) {
                 updateListner.updateTask(position, taskName)
                 Toast.makeText(context, "Task updated successfully", Toast.LENGTH_SHORT).show()
             }
             dialog.dismiss()

         }

         addDialog.setNegativeButton("Cancel") { dialog, _ ->
             dialog.dismiss()
         }


         addDialog.create().show()


     }

     fun updateStatus(taskId: Int, status: Int,position: Int) {
         if (status == 1) {
             db.updateStatus(taskId, status)
             tasks[position].status=1
         } else {
             db.updateStatus(taskId, status)
             tasks[position].status=0

         }
     }

  private fun updateStatus(checkBox: CheckBox, position: Int){
    if (tasks[position].status==1){
        checkBox.isChecked=true
    }
    else
        checkBox.isChecked=false

}

 }





