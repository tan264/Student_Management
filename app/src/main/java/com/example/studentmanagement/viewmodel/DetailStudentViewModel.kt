package com.example.studentmanagement.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.studentmanagement.model.Student
import com.example.studentmanagement.network.Api
import kotlinx.coroutines.launch

class DetailStudentViewModel : ViewModel() {
    private val _student = MutableLiveData<Student>()
    val student: LiveData<Student> = _student

    fun myInit(student: Student) {
        _student.value = student
    }

    fun getStudent(id: Int) {
        viewModelScope.launch {
            try {
                _student.value = Api.retrofitService.getStudent(id)
                Log.d("DEBUG", student.value.toString())
            } catch (e: Exception) {
                Log.d("DEBUG", e.toString())
            }
        }
    }
}