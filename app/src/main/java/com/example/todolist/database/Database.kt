package com.example.todolist.database

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log
import com.example.todolist.database.model.Task
import com.example.todolist.database.model.User

class  Database(var context: Context) : SQLiteOpenHelper(context, DB_NAME,null, DB_VERSION){

     companion object{
         private const val DB_NAME: String = "users_db"
        private const val DB_VERSION: Int = 11
        // private var instance:Database?=null

         private const val TABLE_USER = "user"
         private const val USER_ID = "id"
         private const val USER_NAME = "name"
         private const val USER_EMAIL = "email"
         private const val USER_PASSWORD = "pass"

         ///Task table
         private const val TABLE_TASK = "task"
         private const val TASK_NAME = "taskName"
         private const val TASK_ID = "taskId"
         private const val TASK_STATUS = "status"
         private const val TASK_ID_USER = "user_id"


         //fun getInstance(context: Context)= instance ?: Database(context)
     }



     override fun onCreate(db: SQLiteDatabase?) {
         // USER TABLE
         db?.execSQL("create table "+ TABLE_USER +" ("+ USER_ID +" integer primary key autoIncrement, "+ USER_NAME +" TEXT , " +
                 ""+ USER_EMAIL +" TEXT unique, "+ USER_PASSWORD +" TEXT)")

         //TASK TABLE
         db?.execSQL("create table $TABLE_TASK ($TASK_ID integer primary key autoIncrement, $TASK_NAME TEXT, $TASK_STATUS Integer default 0, $TASK_ID_USER INTEGER References $TABLE_USER ($USER_ID) on delete cascade)")


     }

     override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
         db?.execSQL("drop table if exists "+ TABLE_USER)
         db?.execSQL("drop table if exists "+ TABLE_TASK)
         onCreate(db);
     }

  fun insertUser(user: User):Boolean{
    val db: SQLiteDatabase= writableDatabase
     var values = ContentValues()

      values.put(USER_NAME,user.name)
      values.put(USER_EMAIL,user.email)
      values.put(USER_PASSWORD,user.password)
      var res= db.insert(TABLE_USER,null,values)

      return res != -1L
  }

      @SuppressLint("Range")
      fun getUser(email:String, pass:String): Int {
         var found=0;
         val db= getReadableDatabase()
          var userId=0
          val args = arrayOf(email,pass)
          var cursor= db.rawQuery("select * from "+ TABLE_USER +" where "+ USER_EMAIL +"=? AND "+ USER_PASSWORD +"=?",args )
         if(cursor.moveToFirst() && cursor!=null){
             do {
                 @SuppressLint("Range") val  email_db =cursor.getString(cursor.getColumnIndex(
                     USER_EMAIL
                 ))
                 @SuppressLint("Range") val pass_db =cursor.getString(cursor.getColumnIndex(
                     USER_PASSWORD
                 ))
                 if(email_db== email && pass_db== pass){
                     found=1;
                   userId =cursor.getInt(cursor.getColumnIndex(USER_ID))
                     break;
                 }
             }while (cursor.moveToNext());
             cursor.close();
         }
          return if(found==1 && userId!=0){
              userId
          } else{
              -1
          }

     }

     fun updateUser(user: User): Boolean {
         val db = writableDatabase
         val values = ContentValues()
         values.put(USER_NAME,user.name)
         values.put(USER_EMAIL,user.email)
         values.put(USER_PASSWORD,user.password)
         val res = db.update(
             TABLE_USER,
             values,
             USER_EMAIL + "= ? AND " + USER_EMAIL + " = ?",
             arrayOf<String>(user.email + "", user.password + "")
         )
         return res > 0
     }

    fun getAllUsers(): ArrayList<User>? {
        val db = readableDatabase
        val users = ArrayList<User>()
        val cursor =
            db.rawQuery("select * from " + TABLE_USER, null)
        if (cursor!!.moveToFirst()) {
            do {
                //@SuppressLint("Range") int id =cursor.getInt(cursor.getColumnIndex(USER_ID)) ;
                @SuppressLint("Range") val name =
                    cursor.getString(cursor.getColumnIndex(USER_NAME))
                @SuppressLint("Range") val email =
                    cursor.getString(cursor.getColumnIndex(USER_EMAIL))
                @SuppressLint("Range") val pass =
                    cursor.getString(cursor.getColumnIndex(USER_PASSWORD))
                val user = User(email, name, pass)
                users.add(user)
            } while (cursor.moveToNext())
            cursor.close()
        }
        return users
    }



// task functions
fun insertTask(task: Task):Boolean{
    val db: SQLiteDatabase= writableDatabase
    var values = ContentValues()

    values.put(TASK_NAME,task.taskName);
    values.put(TASK_ID_USER,task.userId);
    values.put(TASK_STATUS,task.status);
    val res= db.insert(TABLE_TASK,null,values);
    return res != -1L
}

    fun updateTask(task: Task, taskName:String): Boolean {
        val db = writableDatabase
        val values = ContentValues()
        values.put(TASK_NAME,taskName);
        val res = db.update(
            TABLE_TASK,
            values,
            "$TASK_ID= ? ",
            arrayOf<String>(task.id.toString())
        )

        if( res > 0){
            task.taskName= taskName

        }
        return res>0
    }

    fun getAllTasks(user_id:Int): ArrayList<Task>? {
        val db = readableDatabase
        val tasks = ArrayList<Task>()
        val args = arrayOf(user_id.toString())

        val cursor =
            db.rawQuery("select * from $TABLE_TASK where $TASK_ID_USER = ?", args)
        if (cursor!!.moveToFirst()) {
            do {
                //@SuppressLint("Range") int id =cursor.getInt(cursor.getColumnIndex(USER_ID)) ;
                @SuppressLint("Range") val task_name =
                    cursor.getString(cursor.getColumnIndex(TASK_NAME))
                @SuppressLint("Range") val id =
                    cursor.getInt(cursor.getColumnIndex(TASK_ID))
                @SuppressLint("Range") val userId =
                    cursor.getInt(cursor.getColumnIndex(TASK_ID_USER))
                @SuppressLint("Range") val status =
                    cursor.getInt(cursor.getColumnIndex(TASK_STATUS))

                val task = Task(id,task_name,userId,status)
                tasks.add(task)
            } while (cursor.moveToNext())
            cursor.close()
        }
        return tasks
    }

    fun getTask(id:Int): Task? {
        val db = readableDatabase
        var task: Task?=null
        val args = arrayOf(id.toString())

        val cursor =
            db.rawQuery("select * from " + TABLE_TASK +" where "+ TASK_ID +"= ?",args)
        if (cursor!!.moveToFirst()) {
            do {
                //@SuppressLint("Range") int id =cursor.getInt(cursor.getColumnIndex(USER_ID)) ;
                @SuppressLint("Range") val task_name =
                    cursor.getString(cursor.getColumnIndex(TASK_NAME))
                @SuppressLint("Range") val id =
                    cursor.getInt(cursor.getColumnIndex(TASK_ID))

                 task = Task(id,task_name)
            } while (cursor.moveToNext())
            cursor.close()
        }
        if(task==null){
            Log.d("tt","not nullll")

        }
        return task
    }



    fun getLastTaskId():Int{
        val db = readableDatabase
       val cursor: Cursor =db.rawQuery("select MAX($TASK_ID) from $TABLE_TASK",null)
        if (cursor.moveToFirst()) {
            val lastId= cursor.getInt(0)
            return lastId
        }else{
            Log.d("id error","error in cursor")
            return 0;
        }
    }

    @SuppressLint("Range")
    fun getLastTaskUserId():Int{
        val db = readableDatabase
        val cursor: Cursor =db.rawQuery("select MAX($TASK_ID_USER) from $TABLE_TASK",null)
        if (cursor!!.moveToFirst()) {
            val lastId= cursor.getInt(cursor.getColumnIndex(TASK_ID_USER))
            return lastId
        }else{
            Log.d("id error","error in cursor")
            return 0;
        }
    }

    fun deleteTask(id: Int):Boolean{
        val db = writableDatabase

        val args = arrayOf<String>(id.toString())
        val res = db.delete(
            TABLE_TASK,
            TASK_ID + "= ? ",
            args
        )

        return res > 0

    }


    fun deleteAllTasks(user_id: Int):Boolean{
        val db = writableDatabase
        val args = arrayOf<String>(user_id.toString())
        val res = db.delete(
            TABLE_TASK,
            TASK_ID_USER + "= ? ",args)

        return res > 0

    }

    fun updateStatus(taskId:Int,status:Int):Boolean{
        val db = writableDatabase
        val values = ContentValues()
        values.put(TASK_STATUS,status);
        val res = db.update(
            TABLE_TASK,
            values,
            "$TASK_ID= ? ",
            arrayOf<String>(taskId.toString())
        )


        return res>0
    }

    fun getCompletedTasks(user_id: Int): ArrayList<Task> {
        val db = readableDatabase
        val tasks = ArrayList<Task>()
        val args = arrayOf(user_id.toString())

        val cursor =
            db.rawQuery("select * from $TABLE_TASK where $TASK_ID_USER = ? and $TASK_STATUS= 1", args)
        if (cursor!!.moveToFirst()) {
            do {
                //@SuppressLint("Range") int id =cursor.getInt(cursor.getColumnIndex(USER_ID)) ;
                @SuppressLint("Range") val task_name =
                    cursor.getString(cursor.getColumnIndex(TASK_NAME))
                @SuppressLint("Range") val id =
                    cursor.getInt(cursor.getColumnIndex(TASK_ID))
                @SuppressLint("Range") val userId =
                    cursor.getInt(cursor.getColumnIndex(TASK_ID_USER))
                @SuppressLint("Range") val status =
                    cursor.getInt(cursor.getColumnIndex(TASK_STATUS))

                val task = Task(id,task_name,userId,status)
                tasks.add(task)
            } while (cursor.moveToNext())
            cursor.close()
        }
        return tasks
    }

}