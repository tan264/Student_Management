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
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ListStudentViewModel : ViewModel() {

    private val _status = MutableLiveData<ApiStatus>()
    val status: LiveData<ApiStatus> = _status

    private val _isDeleteSuccess = MutableLiveData(0)
    val isDeleteSuccess: LiveData<Int> = _isDeleteSuccess

    private val _listStudents = MutableLiveData<List<Student>>()
    val listStudent: LiveData<List<Student>> = _listStudents

    private lateinit var originalList: List<Student>

    init {
        getListStudents()
    }

    fun deleteStudent(idStudent: Int) {
        Log.d("DEBUG", "in delete")
        try {
            Log.d("DEBUG", "in scope")
            val callback = Api.retrofitService.deleteStudent(idStudent)
            callback.enqueue(object : Callback<String> {
                override fun onResponse(call: Call<String>, response: Response<String>) {
                    val result = response.body()
                    result?.let {
                        Log.d("DEBUG a", it)
                        if (it == "delete_success") {
                            _isDeleteSuccess.value = 1
                            _isDeleteSuccess.value = 0
                        } else {
                            _isDeleteSuccess.value = -1
                            _isDeleteSuccess.value = 0
                        }
                    }
                }

                override fun onFailure(call: Call<String>, t: Throwable) {
                    Log.d("DEBUG b", t.toString())
                    _isDeleteSuccess.value = -1
                    _isDeleteSuccess.value = 0
                }
            })

        } catch (e: Exception) {
            Log.d("DEBUG c", e.toString())
        }
    }

    fun getListStudents() {
        _status.value = ApiStatus.LOADING
        viewModelScope.launch {
            try {
                originalList = Api.retrofitService.getListStudents()
                _listStudents.value = originalList
                _status.value = ApiStatus.DONE
            } catch (e: Exception) {
                _status.value = ApiStatus.ERROR
                Log.d("DEBUG", e.toString())
                _listStudents.value = listOf()
            }
        }
    }

    fun changeListStudents(students: List<Student>) {
        _listStudents.value = students
    }

    fun resetToOriginalList() {
        _listStudents.value = originalList
    }

}