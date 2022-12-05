package com.example.listadetarefas.Repository

import androidx.lifecycle.LiveData
import com.example.listadetarefas.DataBase.Dao.TaskDao
import com.example.listadetarefas.DataBase.ModelDb.TaskEntity

class MainRepository(private val taskDao: TaskDao) {

    val tasks: LiveData<List<TaskEntity>> = taskDao.getData()

    suspend fun insert(taskEntity: List<TaskEntity>){
        taskDao.insert(taskEntity)
    }

    suspend fun deleteTask(uid: Long){
        taskDao.deleteTask(uid)
    }
}