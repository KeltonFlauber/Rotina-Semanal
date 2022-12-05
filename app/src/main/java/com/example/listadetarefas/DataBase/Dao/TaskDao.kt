package com.example.listadetarefas.DataBase.Dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.listadetarefas.DataBase.ModelDb.TaskEntity
import com.example.listadetarefas.Model.MyTask

@Dao
interface TaskDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(task: List<TaskEntity>)

    @Query("SELECT * FROM task_table")
    fun getData(): LiveData<List<TaskEntity>>

    @Query("DELETE FROM task_table WHERE uid = :uid")
    suspend fun deleteTask(uid: Long)

}