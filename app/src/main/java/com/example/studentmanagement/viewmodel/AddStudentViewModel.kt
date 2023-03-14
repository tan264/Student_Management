package com.example.studentmanagement.viewmodel

import android.net.Uri
import android.util.Log
import android.widget.RadioGroup
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.studentmanagement.R
import com.example.studentmanagement.network.Api
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*


class AddStudentViewModel : ViewModel() {

    companion object {
        const val DEFAULT_BOY_AVATAR = "default_boy.png"
        const val DEFAULT_GIRL_AVATAR = "default_girl.png"
    }

    private var _statusInsert = MutableLiveData<Boolean?>()
    val statusInsert: LiveData<Boolean?> = _statusInsert

    private val _birthday = MutableLiveData<String>()
    val birthday: LiveData<String> = _birthday

    private val _age = MutableLiveData<Int>()
    val age: LiveData<Int> = _age

    private val _uri = MutableLiveData<Uri>()
    val uri: LiveData<Uri> = _uri

    private val _checked = MutableLiveData<Int>()
    val checked: LiveData<Int> = _checked

    val fullname = MutableLiveData<String>()

    val nickname = MutableLiveData<String>()

    private var avatar: String? = null
    private var gender: String

    init {
        _checked.postValue(R.id.gender_male)
        gender = "Male"
    }

    fun setGender(radioGroup: RadioGroup, id: Int) {
        _checked.value = id
        when (id) {
            R.id.gender_male -> gender = "Male"
            R.id.gender_female -> gender = "Female"
        }
    }

    fun setAge(year: Int, month: Int, day: Int) {
        val calToday = Calendar.getInstance()
        _age.value = calToday.get(Calendar.YEAR) - year
        if (calToday.get(Calendar.MONTH) < month) {
            _age.value = _age.value!! - 1
        } else if (calToday.get(Calendar.MONTH) == month && calToday.get(Calendar.DAY_OF_MONTH) < day) {
            _age.value = _age.value!! - 1
        }
    }

    fun setBirthday(year: Int, month: Int, day: Int) {
        _birthday.value = String.Companion.format("%1\$02d/%2\$02d/%3$4d", day, month + 1, year)
    }

    fun setImage(uri: Uri) {
        _uri.value = uri
    }

    fun uploadImage(type: String, requestBody: RequestBody) {
//        val file = File(realPath)
//        var filePath = file.absolutePath
//        val token = filePath.split(".")
//        filePath = token[0] + System.currentTimeMillis() + "." + token[1]
        val body =
            MultipartBody.Part.createFormData("uploaded_file", System.currentTimeMillis().toString() + "." + type, requestBody)
        val callback = Api.retrofitService.uploadImage(body)
        callback.enqueue(object : Callback<String> {
            override fun onResponse(call: Call<String>, response: Response<String>) {
                response.body()?.let {
                    avatar = it
                    Log.d("UPLOAD IMAGE", "OK - $avatar")
                }
            }

            override fun onFailure(call: Call<String>, t: Throwable) {
                t.message?.let { Log.d("UPLOAD IMAGE", "FAILED - $it") }
            }
        })
    }

    fun insertData() {

        if (avatar == null) {
            avatar = if (gender == "Male") {
                DEFAULT_BOY_AVATAR
            } else {
                DEFAULT_GIRL_AVATAR
            }
        }
        if (nickname.value == null) {
            val token = fullname.value!!.split(" ")
            nickname.value = token[token.size - 1]
        }

        val idClass = if (age.value!! > 4) {
            1
        } else if (age.value!! >= 3) {
            2
        } else {
            3
        }

        var date = ""
        birthday.value?.let {
            val tokens = it.split("/")
            date = tokens[2] + "-" + tokens[1] + "-" + tokens[0]
        }

        val callback = Api.retrofitService.insertData(
            idClass, fullname.value!!, nickname.value!!, date, gender, avatar!!
        )
        callback.enqueue(object : Callback<String> {
            override fun onResponse(call: Call<String>, response: Response<String>) {
                val result = response.body()
                result?.let {
                    _statusInsert.value = result == "insert_success"
                }
            }

            override fun onFailure(call: Call<String>, t: Throwable) {
                Log.d("DEBUG", t.toString())
                _statusInsert.value = false
            }
        })
    }
}