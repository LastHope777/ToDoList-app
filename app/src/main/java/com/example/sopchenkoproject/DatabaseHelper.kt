package com.example.sopchenkoproject

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.database.Cursor
import android.content.ContentValues

class DatabaseHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_NAME = "TodoDatabase"
        private const val DATABASE_VERSION = 1
        private const val TABLE_NAME = "todo_items"
        private const val COLUMN_ID = "_id"
        private const val COLUMN_TASK = "task"
    }

    override fun onCreate(db: SQLiteDatabase) {
        val CREATE_TABLE = ("CREATE TABLE $TABLE_NAME ($COLUMN_ID INTEGER PRIMARY KEY, $COLUMN_TASK TEXT)")
        db.execSQL(CREATE_TABLE)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
        onCreate(db)
    }
    fun addTask(task: String) {
        val values = ContentValues()
        values.put(COLUMN_TASK, task)

        val db = this.writableDatabase
        db.insert(TABLE_NAME, null, values)
        db.close()
    }

    fun readTasks(): List<String> {
        val tasks = mutableListOf<String>()
        val db = this.readableDatabase
        val cursor: Cursor = db.rawQuery("SELECT * FROM $TABLE_NAME", null)

        if (cursor.moveToFirst()) {
            do {
                val task = cursor.getString(cursor.getColumnIndex(COLUMN_TASK))
                tasks.add(task)
            } while (cursor.moveToNext())
        }
        cursor.close()
        db.close()
        return tasks
    }
    fun clearDatabase()
    {
        val db = this.writableDatabase
        db.delete(TABLE_NAME, null, null)
        db.close()
    }
    fun deleteTask(task: String)
    {
        val db = writableDatabase
        db.delete(TABLE_NAME, "$COLUMN_TASK=?", arrayOf(task))
        db.close()
    }
}
