package com.example.listadetarefas.DataBase.ModelDb

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "task_table")
data class TaskEntity(
    val taskName: String,
    val taskDescription: String?,
    val dayOfWeek: String,
    val hour: Int,
    val minutes: Int,
    var subTask: String?
){
    @PrimaryKey(autoGenerate = true)
    var uid: Long = 0
}
