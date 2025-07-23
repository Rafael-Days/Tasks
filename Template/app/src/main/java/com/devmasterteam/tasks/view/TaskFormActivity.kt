package com.devmasterteam.tasks.view

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import android.app.DatePickerDialog
import android.content.Intent
import android.view.View
import android.widget.ArrayAdapter
import android.widget.DatePicker
import android.widget.Toast
import androidx.activity.viewModels
import androidx.lifecycle.viewModelScope
import com.devmasterteam.tasks.R
import com.devmasterteam.tasks.databinding.ActivityTaskFormBinding
import com.devmasterteam.tasks.service.constants.TaskConstants
import com.devmasterteam.tasks.service.model.PriorityModel
import com.devmasterteam.tasks.service.model.TaskModel
import com.devmasterteam.tasks.viewmodel.TaskFormViewModel
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*

class TaskFormActivity : AppCompatActivity(), View.OnClickListener,
    DatePickerDialog.OnDateSetListener {

    private val viewModel: TaskFormViewModel by viewModels()
    private val binding: ActivityTaskFormBinding by lazy {
        ActivityTaskFormBinding.inflate(
            layoutInflater
        )
    }
    private val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
    private var listPriority: List<PriorityModel> = mutableListOf()
    private var taskId = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        supportActionBar?.hide()
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(
                systemBars.left + binding.root.paddingStart,
                systemBars.top,
                systemBars.right + binding.root.paddingEnd,
                systemBars.bottom
            )
            insets
        }

        // Eventos
        binding.buttonSave.setOnClickListener(this)
        binding.buttonDate.setOnClickListener(this)

        loadDataFromActivity()

        observe()
    }

    override fun onClick(v: View) {
        if (v.id == R.id.button_date) {
            handleDate()
        } else if (v.id == R.id.button_save) {
            handleSave()
        }
    }

    override fun onDateSet(v: DatePicker, year: Int, month: Int, dayOfMonth: Int) {
        val calendar = Calendar.getInstance()
        calendar.set(year, month, dayOfMonth)

        val dueDate = dateFormat.format(calendar.time)
        binding.buttonDate.text = dueDate
    }

    private fun observe() {
        viewModel.priorityList.observe(this) {
            listPriority = it
            val list = mutableListOf<String>()
            for (item in it) {
                list.add(item.description)
            }

            val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, list)

            binding.spinnerPriority.adapter = adapter
        }

        viewModel.taskSaved.observe(this) {
            if (it.status()) {
                if(taskId == 0){
                    toast(getString(R.string.msg_task_created))
                }else{
                    toast(getString(R.string.msg_task_updated))
                }
                finish()
            } else {
                toast(it.message())
            }
        }

        viewModel.task.observe(this){
            binding.editDescription.setText(it.description)
            binding.checkComplete.isChecked = it.complete
            val date = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).parse(it.dueDate)
            binding.buttonDate.text = dateFormat.format(date!!)
            binding.buttonSave.text = getString(R.string.button_update_task)
            binding.spinnerPriority.setSelection(getIndex(it.priorityId))
        }

        viewModel.taskLoad.observe(this){
            if(!it.status()){
                toast(it.message())
            }
        }
    }

    private fun getIndex(priorityId: Int): Int{
        var index = 0
        for(p in listPriority){
            if (p.id == priorityId){
                break
            }
            index++
        }
        return index
    }

    private fun toast(str: String) {
        Toast.makeText(applicationContext, str, Toast.LENGTH_SHORT).show()
    }

    private fun loadDataFromActivity(){
        val bundle = intent.extras
        if (bundle != null){
            taskId = bundle.getInt(TaskConstants.BUNDLE.TASKID)
            viewModel.load(taskId)
        }
    }

    private fun handleSave() {
        val description = binding.editDescription.text.toString()
        val priorityId = listPriority[binding.spinnerPriority.selectedItemPosition].id
        val completed = binding.checkComplete.isChecked
        val dueDate = binding.buttonDate.text.toString()

        val task = TaskModel(taskId, priorityId, description, dueDate, completed)
        viewModel.save(task)
    }

    private fun handleDate() {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)
        DatePickerDialog(this, this, year, month, day).show()
    }
}