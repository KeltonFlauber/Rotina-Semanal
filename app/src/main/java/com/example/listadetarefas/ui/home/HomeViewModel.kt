package com.example.listadetarefas.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.listadetarefas.Model.MyTask

class HomeViewModel : ViewModel() {

    private val _text = MutableLiveData<String>()

    val text: LiveData<String> = _text

    fun addToTextView(text: String){

        _text.value = text

    }
}