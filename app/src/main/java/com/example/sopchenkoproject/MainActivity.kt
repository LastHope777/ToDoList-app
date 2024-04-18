package com.example.sopchenkoproject

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import android.widget.ListView
import android.app.AlertDialog
import android.content.DialogInterface


class MainActivity : AppCompatActivity() {
    private lateinit var db: DatabaseHelper
    private lateinit var adapter: ArrayAdapter<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        db = DatabaseHelper(this)

        val imageButton = findViewById<ImageButton>(R.id.imageButton)
        val editText = findViewById<EditText>(R.id.editTextText)
        val addButton = findViewById<Button>(R.id.button2)
        val listView = findViewById<ListView>(R.id.listView)

        val tasks: ArrayList<String> = ArrayList()

        adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1,tasks)
        listView.adapter = adapter

        addButton.setOnClickListener {
            val task = editText.text.toString()
            db.addTask(task)
            editText.text.clear()
            tasks.clear()
            tasks.add(task)


            updateListView() // Обновляем данные в ListView после добавления новой задачи
        }
        imageButton.setOnClickListener {
            db.clearDatabase()
            tasks.clear()
            updateListView()
        }
        listView.setOnItemClickListener { _, _, position, _ ->
            val selectedItem = adapter.getItem(position)
            if (selectedItem != null) {
                val alertDialogBuilder = AlertDialog.Builder(this)
                alertDialogBuilder.setTitle("Подтверждение удаления")
                alertDialogBuilder.setMessage("Вы уверены, что хотите удалить этот элемент?")
                alertDialogBuilder.setPositiveButton("Да") { _, _ ->
                    db.deleteTask(selectedItem)
                    tasks.remove(selectedItem)
                    updateListView()
                }
                alertDialogBuilder.setNegativeButton("Нет") { _, _ ->
                }

                val alertDialog = alertDialogBuilder.create()
                alertDialog.show()
            }
        }
        updateListView() // Инициализируем ListView при запуске приложения


    }

    private fun updateListView() {
        val tasks = db.readTasks()
        adapter.clear()
        adapter.addAll(tasks)
        adapter.notifyDataSetChanged()
    }
}


