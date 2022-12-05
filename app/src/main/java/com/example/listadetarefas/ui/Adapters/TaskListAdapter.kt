package com.example.listadetarefas.ui.Adapters

import android.content.Context
import android.content.res.Resources
import android.graphics.Color
import android.icu.text.SimpleDateFormat
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import android.widget.ToggleButton
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.PopupMenu
import androidx.cardview.widget.CardView
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.listadetarefas.DataBase.DbViewModel.DbViewModel
import com.example.listadetarefas.DataBase.ModelDb.TaskEntity
import com.example.listadetarefas.R
import com.example.listadetarefas.databinding.TaskListBinding
import java.util.*
import kotlin.collections.ArrayList

class TaskListAdapter(
    private val context: Context,
    private val viewModel: DbViewModel,
    private val onItemClick: (TaskEntity) -> Unit
) : RecyclerView.Adapter<TaskListAdapter.TaskHolder>() {

    private var taskListEntry = mutableListOf<TaskEntity>()
    private var taskList = taskListEntry

    class TaskHolder(binding: TaskListBinding) : ViewHolder(binding.root) {

        val title: TextView = binding.taskTitle
        val date: TextView = binding.taskDate
        val checkBox: CheckBox = binding.checkbox
        val alarm: ToggleButton = binding.toggleButtonAlarm
        val menu: ImageView = binding.dotMenu

        fun click(myTask: TaskEntity, onItemClick: (TaskEntity) -> Unit) {
            itemView.setOnClickListener { onItemClick(myTask) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskHolder {
        val inflate = LayoutInflater.from(parent.context)
        val _binding = TaskListBinding.inflate(inflate, parent, false)
        return TaskHolder(_binding)
    }

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onBindViewHolder(holder: TaskHolder, position: Int) {

        holder.date.setTextColor(Color.RED)

        if (taskList[position].subTask == "alarm" || taskList[position].subTask == "check & alarm") {
            holder.alarm.isChecked = true
        }

        if (taskList[position].subTask == "check" || taskList[position].subTask == "check & alarm") {
            holder.checkBox.isChecked = true
        }

        holder.click(taskList[position], onItemClick)

        holder.menu.setOnClickListener{popUpMenu(it, taskList[position].uid)}

        holder.checkBox.setOnClickListener {
            if (holder.checkBox.isChecked && holder.alarm.isChecked) {
                taskList[position].subTask = "check & alarm"
                viewModel.insert(taskList)
            } else if (holder.checkBox.isChecked && !holder.alarm.isChecked) {
                taskList[position].subTask = "check"
                viewModel.insert(taskList)
            } else if (!holder.checkBox.isChecked && holder.alarm.isChecked) {
                taskList[position].subTask = "alarm"
                viewModel.insert(taskList)
            } else {
                taskList[position].subTask = null
                viewModel.insert(taskList)
            }
        }

        holder.title.text = taskList[position].taskName
        holder.date.text = StringBuilder(taskList[position].dayOfWeek).append(", ")
            .append(taskList[position].hour).append(":")
            .append(taskList[position].minutes.toString().padStart(2, '0'))

    }

    override fun getItemCount(): Int {
        return taskList.size
    }

    fun popUpMenu(view: View, uid: Long){

        val popupMenu = PopupMenu(context,view)
        popupMenu.inflate(R.menu.main)
        popupMenu.setOnMenuItemClickListener {
            when(it.itemId){
                R.id.delete_menu -> {
                    viewModel.deleteTask(uid)
                    true
                }
                else -> true
            }
        }
        popupMenu.show()
        val popup = PopupMenu::class.java.getDeclaredField("mPopup")
        popup.isAccessible = true
        val menu = popup.get(popupMenu)
        menu.javaClass.getDeclaredMethod("setForceShowIcon", Boolean::class.java)
            .invoke(menu, true)
    }

    fun setCurrentData(query: String){
        taskList.clear()
        taskList.addAll(taskListEntry.filter { it.dayOfWeek.contains(query) })
        taskList.sortBy { it.hour }
        notifyDataSetChanged()

    }

    fun setData(newList: List<TaskEntity>) {
        taskListEntry = newList.toMutableList()
        notifyDataSetChanged()
    }
}