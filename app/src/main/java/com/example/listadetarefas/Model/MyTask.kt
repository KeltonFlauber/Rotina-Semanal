package com.example.listadetarefas.Model

import com.example.listadetarefas.DataBase.ModelDb.TaskEntity


data class MyTask (

    val taskName: String,
    val taskDescription: String?,
    val dayOfWeek: String,
    val hour: Int,
    val minutes: Int,
    var check: String?

        )
