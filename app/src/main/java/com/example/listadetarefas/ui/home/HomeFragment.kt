package com.example.listadetarefas.ui.home

import android.app.AlarmManager
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Context.ALARM_SERVICE
import android.content.Intent
import android.icu.text.SimpleDateFormat
import android.os.Build
import android.os.Bundle
import android.provider.AlarmClock
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat.getSystemService
import androidx.core.view.isNotEmpty
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.listadetarefas.Alarm.AlarmReceiver
import com.example.listadetarefas.DataBase.DbViewModel.DbViewModel
import com.example.listadetarefas.DataBase.ModelDb.TaskEntity
import com.example.listadetarefas.R
import com.example.listadetarefas.databinding.FragmentHomeBinding
import com.example.listadetarefas.ui.Adapters.TaskLTomorrowAdapter
import com.example.listadetarefas.ui.Adapters.TaskLWeekAdapter
import com.example.listadetarefas.ui.Adapters.TaskListAdapter
import com.google.android.material.floatingactionbutton.FloatingActionButton
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.OffsetDateTime
import java.time.temporal.ChronoUnit
import java.util.*
import kotlin.time.Duration.Companion.days


class HomeFragment : Fragment() {

    lateinit var binding: FragmentHomeBinding
    lateinit var dialog: AlertDialog
    lateinit var dialogDeleteTask: AlertDialog
    lateinit var alarmManager: AlarmManager
    lateinit var pendingIntent: PendingIntent

    private val taskList = arrayListOf<TaskEntity>()

    private val tlAdapter by lazy {
        TaskListAdapter(requireContext(), dbViewModel) {

        }
    }
    private val tlTomorrowAdapter by lazy {
        TaskLTomorrowAdapter(requireContext() ,dbViewModel) {

        }
    }
    private val tlWeekAdapter by lazy {
        TaskLWeekAdapter(requireContext(), dbViewModel) {

        }
    }

    private val dbViewModel by lazy { ViewModelProvider(this).get(DbViewModel::class.java) }
    lateinit var calendar: Calendar

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentHomeBinding.inflate(inflater, container, false)

        alarmManager = requireContext().getSystemService(Context.ALARM_SERVICE) as AlarmManager
        calendar = Calendar.getInstance()

        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setUpRecyclerView()
        showDeleteDialog()

        binding.textNoTasksToday.text = "Sem Tarefas..."
        binding.textViewToday.visibility = View.GONE
        binding.imageView2.visibility = View.VISIBLE
        binding.textViewTomorrow.visibility = View.GONE
        binding.textViewWeek.visibility = View.GONE

    }

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onStart() {
        super.onStart()

        dbViewModel.taskDb.observe(viewLifecycleOwner, Observer {

            val sdf = SimpleDateFormat("EEEE")
            val today = Calendar.getInstance()
            val tomorrow = Calendar.getInstance()
            tomorrow.add(Calendar.DAY_OF_WEEK, 1)
            val day3 = Calendar.getInstance()
            day3.add(Calendar.DAY_OF_WEEK, 2)
            val day4 = Calendar.getInstance()
            day4.add(Calendar.DAY_OF_WEEK, 3)
            val day5 = Calendar.getInstance()
            day5.add(Calendar.DAY_OF_WEEK, 4)
            val day6 = Calendar.getInstance()
            day6.add(Calendar.DAY_OF_WEEK, 5)
            val day7 = Calendar.getInstance()
            day7.add(Calendar.DAY_OF_WEEK, 6)
            val dayOfTheWeekToday: String = sdf.format(today.time)
            val dayOfTheWeekTomorrow: String = sdf.format(tomorrow.time)
            val day3OfTheWeek: String = sdf.format(day3.time)
            val day4OfTheWeek: String = sdf.format(day4.time)
            val day5OfTheWeek: String = sdf.format(day5.time)
            val day6OfTheWeek: String = sdf.format(day6.time)
            val day7OfTheWeek: String = sdf.format(day7.time)

            tlAdapter.setData(it)
            tlAdapter.setCurrentData(dayOfTheWeekToday)
            tlTomorrowAdapter.setData(it)
            tlTomorrowAdapter.setCurrentData(dayOfTheWeekTomorrow)
            tlWeekAdapter.setData(it)
            tlWeekAdapter.setCurrentData(day3OfTheWeek, day4OfTheWeek,
                day5OfTheWeek, day6OfTheWeek, day7OfTheWeek)
            Log.e("Date", day3OfTheWeek)

            if (binding.recyclerHome.adapter!!.itemCount == 0 &&
                binding.recyclerTomorrow.adapter!!.itemCount == 0 &&
                binding.recyclerWeek.adapter!!.itemCount == 0 ){

                binding.textNoTasksToday.text = "Sem Tarefas..."
                binding.textViewToday.visibility = View.GONE
                binding.textViewTomorrow.visibility = View.GONE
                binding.textViewWeek.visibility = View.GONE
                binding.imageView2.visibility = View.VISIBLE

            }else{
                binding.textNoTasksToday.text = ""
                binding.imageView2.visibility = View.GONE
            }

            //today
            if (binding.recyclerHome.adapter!!.itemCount != 0) {

                binding.textViewToday.visibility = View.VISIBLE

            }else{
                binding.textViewToday.visibility = View.GONE
            }


            //tomorrow
            if (binding.recyclerTomorrow.adapter!!.itemCount != 0) {

                binding.textViewTomorrow.visibility = View.VISIBLE

            }else{

                binding.textViewTomorrow.visibility = View.GONE

            }

            //week
            if (binding.recyclerWeek.adapter!!.itemCount != 0) {

                binding.textViewWeek.visibility = View.VISIBLE

            }else{

                binding.textViewWeek.visibility = View.GONE

            }

        })

        binding.floatingActionButton3.setOnClickListener {
            showAlertDialog()
        }

    }

    fun createWorkManager(task: TaskEntity){

        //val timeTilFuture = ChronoUnit.MILLIS.between(OffsetDateTime.now(),)

    }

    private fun setUpRecyclerView() {

        binding.recyclerHome.adapter = tlAdapter
        binding.recyclerHome.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerHome.hasFixedSize()

        binding.recyclerTomorrow.adapter = tlTomorrowAdapter
        binding.recyclerTomorrow.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerTomorrow.hasFixedSize()

        binding.recyclerWeek.adapter = tlWeekAdapter
        binding.recyclerWeek.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerWeek.hasFixedSize()

    }

    fun showDeleteDialog(){

        val myView = layoutInflater.inflate(R.layout.delete_dialog, null)
        val builder  = AlertDialog.Builder(requireContext(), R.style.ThemeCustomDialog)

        val yesButton: ImageView = myView.findViewById(R.id.imageButtonYes)
        val notButton: ImageView = myView.findViewById(R.id.imageButtonNot)

        builder.setView(myView)

        dialogDeleteTask = builder.create()
    }

    fun setAlarm(timeMillis: Long){

        val intentBroad = Intent(this@HomeFragment.requireContext(), AlarmReceiver::class.java)

        pendingIntent = PendingIntent.getBroadcast(
            this@HomeFragment.requireContext(),
            0, intentBroad, PendingIntent.FLAG_IMMUTABLE
        )

        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP,
            timeMillis, 24 * 7 * 60 * 60 * 1000, pendingIntent)
    }

    fun cancelAlarm(){

        val intentBroad = Intent(this@HomeFragment.requireContext(), AlarmReceiver::class.java)

        pendingIntent = PendingIntent.getBroadcast(
            requireContext(),
            0, intentBroad, PendingIntent.FLAG_IMMUTABLE
        )

        alarmManager.cancel(pendingIntent)

    }

    @RequiresApi(Build.VERSION_CODES.M)
    fun showAlertDialog() {

        val myView = layoutInflater.inflate(R.layout.new_task_dialog, null)
        val builder = AlertDialog.Builder(requireContext(), R.style.ThemeCustomDialog)
        val hour = myView.findViewById<TimePicker>(R.id.timePicker)
        var subTask: String? = null
        hour.setIs24HourView(true)

        val spinner: Spinner = myView.findViewById(R.id.spinner)
        val weekDaysInt = arrayListOf<Int>()

        //Spinner setUp
        val weekDays: ArrayList<String> = arrayListOf(
            "segunda-feira", "terça-feira", "quarta-feira",
            "quinta-feira", "sexta-feira", "sábado", "domingo"
        )
        val spinnerList = ArrayList<String>()
        for (days in weekDays) {
            spinnerList.add(days)
        }
        val spinnerAdapter = ArrayAdapter(
            requireContext(),
            com.google.android.material.R.layout.support_simple_spinner_dropdown_item, weekDays
        )

        spinner.adapter = spinnerAdapter

        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                Toast.makeText(
                    this@HomeFragment.requireContext(),
                    spinner.selectedItem.toString()
                            + " selecionado!", Toast.LENGTH_SHORT
                ).show()
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
                Toast.makeText(
                    this@HomeFragment.requireContext(),
                    spinner.selectedItem.toString()
                            + " selecionado!", Toast.LENGTH_SHORT
                )
            }

        }
        //\\

        val fBtnCheck: FloatingActionButton = myView.findViewById(R.id.floatingActionButton)
        val fBtnClose: FloatingActionButton = myView.findViewById(R.id.floatingActionButton2)
        val taskName: EditText = myView.findViewById(R.id.editTxt_taskName)
        val taskDesc: EditText = myView.findViewById(R.id.editTxt_task_description)
        val toggle: ToggleButton = myView.findViewById(R.id.toggleButton)


        toggle.setOnClickListener {

            weekDaysInt.clear()

            when (spinner.selectedItem) {
                "segunda-feira" -> weekDaysInt.add(Calendar.MONDAY)
                "terça-feira" -> weekDaysInt.add(Calendar.TUESDAY)
                "quarta-feira" -> weekDaysInt.add(Calendar.WEDNESDAY)
                "quinta-feira" -> weekDaysInt.add(Calendar.THURSDAY)
                "sexta-feira" -> weekDaysInt.add(Calendar.FRIDAY)
                "sábado" -> weekDaysInt.add(Calendar.SATURDAY)
                "domingo" -> weekDaysInt.add(Calendar.SUNDAY)
            }

            fun addDay(): Int {
                var dow = calendar[Calendar.DAY_OF_WEEK] - weekDaysInt[0]
                if (dow<=1){
                    dow*-1
                }
                return dow
            }

            calendar.timeInMillis = System.currentTimeMillis()
            calendar.add(Calendar.DAY_OF_WEEK, addDay())
            calendar[Calendar.HOUR] = hour.hour
            calendar[Calendar.MINUTE] = hour.minute
            calendar[Calendar.SECOND] = 0
            calendar[Calendar.MILLISECOND] = 0

            Log.e("DAY", calendar[Calendar.DAY_OF_WEEK].toString())

            val intent = Intent(AlarmClock.ACTION_SET_ALARM)

            if (toggle.isChecked) {
                /*intent.putExtra(AlarmClock.EXTRA_HOUR, hour.hour)
                intent.putExtra(AlarmClock.EXTRA_MINUTES, hour.minute)
                intent.putExtra(AlarmClock.EXTRA_DAYS, weekDaysInt)
                intent.putExtra(AlarmClock.EXTRA_MESSAGE, taskName.text.toString()
                startActivity(intent)*/
                //if (intent.resolveActivity(manager))
                Toast.makeText(
                    requireContext(),
                    "Alarme ativado para ${hour.hour}:${hour.minute}", Toast.LENGTH_SHORT
                ).show()

                subTask = "alarm"

            } else {

                Toast.makeText(requireContext(),
                "Alarme desativado", Toast.LENGTH_SHORT).show()
                subTask = null

            }


            /*if (toggle.isChecked){
                Toast.makeText(requireContext(),
                    "Alarme ativado para ${hour.hour}:${hour.minute}", Toast.LENGTH_SHORT).show()
            }else{
                Toast.makeText(requireContext(),
                    "Alarme desativado", Toast.LENGTH_SHORT).show()
            }*/
        }


        fBtnCheck.setOnClickListener {
            //add Alarm
            weekDaysInt.clear()
            when (spinner.selectedItem) {
                "segunda-feira" -> weekDaysInt.add(Calendar.MONDAY)
                "terça-feira" -> weekDaysInt.add(Calendar.TUESDAY)
                "quarta-feira" -> weekDaysInt.add(Calendar.WEDNESDAY)
                "quinta-feira" -> weekDaysInt.add(Calendar.THURSDAY)
                "sexta-feira" -> weekDaysInt.add(Calendar.FRIDAY)
                "sábado" -> weekDaysInt.add(Calendar.SATURDAY)
                "domingo" -> weekDaysInt.add(Calendar.SUNDAY)
            }

            if (toggle.isChecked) {
                /*val intent = Intent(AlarmClock.ACTION_SET_ALARM)
                intent.putExtra(AlarmClock.EXTRA_HOUR, hour.hour)
                intent.putExtra(AlarmClock.EXTRA_MINUTES, hour.minute)
                intent.putExtra(AlarmClock.EXTRA_DAYS, weekDaysInt)
                intent.putExtra(AlarmClock.EXTRA_MESSAGE, taskName.text.toString())*/
                //if (intent.resolveActivity(manager))
                setAlarm(calendar.timeInMillis)
                Toast.makeText(
                    requireContext(),
                    "Alarme ativado para ${hour.hour}:${hour.minute}", Toast.LENGTH_SHORT
                ).show()
            } else {
                /*val intent = Intent(AlarmClock.ACTION_DISMISS_ALARM)
                pendingIntent = PendingIntent.getBroadcast(requireContext(),0, intent, 0)
                alarmManager.cancel(pendingIntent)*/
                cancelAlarm()
                Toast.makeText(
                    requireContext(),
                    "Alarme desativado", Toast.LENGTH_SHORT
                ).show()
            }

            //add Task
            taskList.clear()
            taskList.add(
                TaskEntity(
                    taskName.text.toString(), taskDesc.text.toString(),
                    spinner.selectedItem.toString(), hour.hour, hour.minute, subTask
                )
            )
            dbViewModel.insert(taskList)
            dialog.dismiss()
        }

        fBtnClose.setOnClickListener { dialog.dismiss() }

        builder.setView(myView)
        dialog = builder.create()
        dialog.show()

    }
}