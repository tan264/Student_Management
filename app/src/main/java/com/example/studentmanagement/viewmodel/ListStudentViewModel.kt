package com.example.studentmanagement.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.studentmanagement.model.Student
import com.example.studentmanagement.network.Api
import com.example.studentmanagement.network.ApiStatus
import kotlinx.coroutines.launch

class ListStudentViewModel : ViewModel() {

    private val _status = MutableLiveData<ApiStatus>()
    val status: LiveData<ApiStatus> = _status

    private val _listStudents = MutableLiveData<List<Student>>()
    val listStudent: LiveData<List<Student>> = _listStudents

    init {
        getListStudents()
    }

    private fun getListStudents() {
        _status.value = ApiStatus.LOADING
        viewModelScope.launch {
            try {
                _listStudents.value = Api.retrofitService.getListStudents()
                _status.value = ApiStatus.DONE
            } catch (e: Exception) {
                _status.value = ApiStatus.ERROR
                Log.d("DEBUG", e.toString())
                _listStudents.value = listOf()
            }
        }
    }
}