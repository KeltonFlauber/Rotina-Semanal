package com.example.listadetarefas.DataBase.DbViewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.example.listadetarefas.DataBase.AppDataBase
import com.example.listadetarefas.DataBase.Dao.TaskDao
import com.example.listadetarefas.DataBase.ModelDb.TaskEntity
import com.example.listadetarefas.Repository.MainRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class DbViewModel(application: Application): AndroidViewModel(application) {

    val taskDb: LiveData<List<TaskEntity>>
    private val repository: MainRepository

    init {
        val taskDao = AppDataBase.getDataBase(application).taskDao()
        repository = MainRepository(taskDao)
        taskDb = repository.tasks
    }

    fun insert(taskEntity: List<TaskEntity>){
        viewModelScope.launch(Dispatchers.IO) {
            repository.insert(taskEntity)
        }
    }

    fun deleteTask(uid: Long){
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteTask(uid)
        }
    }

}