package com.example.studentmanagement.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.studentmanagement.network.Api
import com.example.studentmanagement.network.ApiStatus
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginViewModel : ViewModel() {

    val user = MutableLiveData("")

    val password = MutableLiveData("")

//    private val _status = MutableLiveData<Boolean?>()
//    val status: LiveData<Boolean?> = _status

    private val _status = MutableLiveData<ApiStatus>()
    val status: LiveData<ApiStatus> = _status

    private val _result = MutableLiveData<String>()
    val result: LiveData<String> = _result

    fun login() {
        _status.value = ApiStatus.LOADING
        Log.d("DEBUG", "${user.value} - ${password.value}")
        val callback = Api.retrofitService.login(user.value!!, password.value!!)
        callback.enqueue(object : Callback<String> {
            override fun onResponse(call: Call<String>, response: Response<String>) {
                val result = response.body()
                result?.let {
                    _status.value = ApiStatus.DONE
                    this@LoginViewModel._result.value = it
                }
            }

            override fun onFailure(call: Call<String>, t: Throwable) {
                Log.d("DEBUG", t.toString())
                _status.value = ApiStatus.ERROR
            }
        })

    }
}