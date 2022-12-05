package com.example.listadetarefas.ui.gallery

import android.app.Application
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.get
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.listadetarefas.DataBase.DbViewModel.DbViewModel
import com.example.listadetarefas.databinding.FragmentWeektasksBinding
import com.example.listadetarefas.ui.Adapters.Days.MondayAdapter
import com.example.listadetarefas.ui.Adapters.TaskListAdapter

class WeekTasksFragment : Fragment() {

    private val dbViewModel by lazy { ViewModelProvider(this).get(DbViewModel::class.java) }
    private val mondayAdapter by lazy { TaskListAdapter(requireContext(), dbViewModel){

    } }

    lateinit var weekDay: String

    private var _binding: FragmentWeektasksBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentWeektasksBinding.inflate(inflater, container, false)
        val root: View = binding.root

        weekDay = "segunda-feira"

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpRecyclerView()

        dbViewModel.taskDb.observe(viewLifecycleOwner, Observer {
            mondayAdapter.setData(it)
            mondayAdapter.setCurrentData(weekDay)
            if (binding.recyclerDay.adapter!!.itemCount == 0){
                binding.imageViewNoTask.isVisible = true
                binding.textNoTasksToday2.isVisible = true
            }else{
                binding.imageViewNoTask.isVisible = false
                binding.textNoTasksToday2.isVisible = false
            }
        })

    }

    override fun onStart() {
        super.onStart()

        binding.checkedTextViewSeg.setOnClickListener {
            when {
                binding.checkedTextViewSeg.isChecked -> {
                    binding.checkedTextViewTer.isChecked = false
                    binding.checkedTextViewQua.isChecked = false
                    binding.checkedTextViewQui.isChecked = false
                    binding.checkedTextViewSex.isChecked = false
                    binding.checkedTextViewSab.isChecked = false
                    binding.checkedTextViewDom.isChecked = false
                    weekDay = "segunda-feira"
                    observeDbViewModel()
                }
                else -> {
                    weekDay = "n/a"
                    observeDbViewModel()
                }
            }
        }
        binding.checkedTextViewTer.setOnClickListener {
            when {
                binding.checkedTextViewTer.isChecked -> {
                    binding.checkedTextViewSeg.isChecked = false
                    binding.checkedTextViewQua.isChecked = false
                    binding.checkedTextViewQui.isChecked = false
                    binding.checkedTextViewSex.isChecked = false
                    binding.checkedTextViewSab.isChecked = false
                    binding.checkedTextViewDom.isChecked = false
                    weekDay = "terça-feira"
                    observeDbViewModel()
                }
                else -> {
                    weekDay = "n/a"
                    observeDbViewModel()
                }
            }
        }
        binding.checkedTextViewQua.setOnClickListener {
            when {
                binding.checkedTextViewQua.isChecked -> {
                    binding.checkedTextViewTer.isChecked = false
                    binding.checkedTextViewSeg.isChecked = false
                    binding.checkedTextViewQui.isChecked = false
                    binding.checkedTextViewSex.isChecked = false
                    binding.checkedTextViewSab.isChecked = false
                    binding.checkedTextViewDom.isChecked = false
                    weekDay = "quarta-feira"
                    observeDbViewModel()
                }
                else -> {
                    weekDay = "n/a"
                    observeDbViewModel()
                }
            }
        }
        binding.checkedTextViewQui.setOnClickListener {
            when {
                binding.checkedTextViewQui.isChecked -> {
                    binding.checkedTextViewTer.isChecked = false
                    binding.checkedTextViewQua.isChecked = false
                    binding.checkedTextViewSeg.isChecked = false
                    binding.checkedTextViewSex.isChecked = false
                    binding.checkedTextViewSab.isChecked = false
                    binding.checkedTextViewDom.isChecked = false
                    weekDay = "quinta-feira"
                    observeDbViewModel()
                }
                else -> {
                    weekDay = "n/a"
                    observeDbViewModel()
                }
            }
        }
        binding.checkedTextViewSex.setOnClickListener {
            when {
                binding.checkedTextViewSex.isChecked -> {
                    binding.checkedTextViewTer.isChecked = false
                    binding.checkedTextViewQua.isChecked = false
                    binding.checkedTextViewQui.isChecked = false
                    binding.checkedTextViewSeg.isChecked = false
                    binding.checkedTextViewSab.isChecked = false
                    binding.checkedTextViewDom.isChecked = false
                    weekDay = "sexta-feira"
                    observeDbViewModel()
                }
                else -> {
                    weekDay = "n/a"
                    observeDbViewModel()
                }
            }
        }
        binding.checkedTextViewSab.setOnClickListener {
            when {
                binding.checkedTextViewSab.isChecked -> {
                    binding.checkedTextViewTer.isChecked = false
                    binding.checkedTextViewQua.isChecked = false
                    binding.checkedTextViewQui.isChecked = false
                    binding.checkedTextViewSex.isChecked = false
                    binding.checkedTextViewSeg.isChecked = false
                    binding.checkedTextViewDom.isChecked = false
                    weekDay = "sábado"
                    observeDbViewModel()
                }
                else -> {
                    weekDay = "n/a"
                    observeDbViewModel()
                }
            }
        }
        binding.checkedTextViewDom.setOnClickListener {
            when {
                binding.checkedTextViewDom.isChecked -> {
                    binding.checkedTextViewTer.isChecked = false
                    binding.checkedTextViewQua.isChecked = false
                    binding.checkedTextViewQui.isChecked = false
                    binding.checkedTextViewSex.isChecked = false
                    binding.checkedTextViewSab.isChecked = false
                    binding.checkedTextViewSeg.isChecked = false
                    weekDay = "domingo"
                    observeDbViewModel()
                }
                else -> {
                    weekDay = "n/a"
                    observeDbViewModel()
                }
            }
        }

    }

    fun setUpRecyclerView(){

        binding.recyclerDay.adapter = mondayAdapter
        binding.recyclerDay.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerDay.hasFixedSize()

    }

    fun observeDbViewModel(){
        dbViewModel.taskDb.observe(viewLifecycleOwner, Observer {
            mondayAdapter.setData(it)
            mondayAdapter.setCurrentData(weekDay)
            if (binding.recyclerDay.adapter!!.itemCount == 0){
                binding.imageViewNoTask.isVisible = true
                binding.textNoTasksToday2.isVisible = true
            }else{
                binding.imageViewNoTask.isVisible = false
                binding.textNoTasksToday2.isVisible = false
            }
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}