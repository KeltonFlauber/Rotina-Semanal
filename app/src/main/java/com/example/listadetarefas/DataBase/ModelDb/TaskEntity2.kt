package com.example.listadetarefas.DataBase.ModelDb

import android.widget.CheckBox
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "task_table2")
data class TaskEntity2(
    val taskName: String,
    val taskDescription: String?,
    val dayOfWeek: String,
    val hour: Int,
    val minutes: Int,
    var checkBoxState: String?
){
    @PrimaryKey(autoGenerate = true)
    var uid: Long = 0
}