package com.example.listadetarefas.DataBase

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.listadetarefas.DataBase.Dao.TaskDao
import com.example.listadetarefas.DataBase.ModelDb.TaskEntity
import com.example.listadetarefas.DataBase.ModelDb.TaskEntity2

@Database(entities = [TaskEntity::class], version = 1)
abstract class AppDataBase: RoomDatabase() {

    abstract fun taskDao(): TaskDao

    companion object {
        @Volatile
        private var INSTANCE: AppDataBase? = null

        fun getDataBase(context: Context): AppDataBase {

            val tempInstance = INSTANCE
            if (tempInstance != null) {
                return tempInstance
            }
            synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDataBase::class.java,
                    "user_database"
                ).build()
                INSTANCE = instance
                return instance
            }
        }
    }

}