package com.example.listadetarefas.Alarm

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.widget.Toast
import androidx.navigation.ActivityNavigator

class AlarmReceiver: BroadcastReceiver() {

    override fun onReceive(p0: Context?, p1: Intent?) {

        Toast.makeText(p0, "AAAAAAA", Toast.LENGTH_LONG).show()

    }
}