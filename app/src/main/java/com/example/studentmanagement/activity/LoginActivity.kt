package com.example.studentmanagement.activity

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.studentmanagement.R
import com.example.studentmanagement.databinding.ActivityLoginBinding
import com.example.studentmanagement.viewmodel.LoginViewModel

class LoginActivity : AppCompatActivity() {

    private val viewModel: LoginViewModel by viewModels()

    private var binding: ActivityLoginBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding!!.root)
    }

    override fun onStart() {
        super.onStart()

        binding!!.apply {
            this.lifecycleOwner = this@LoginActivity
            this.viewModel = this@LoginActivity.viewModel
        }

        viewModel.status.observe(this) {
            it?.let {
                if (it) {
                    Toast.makeText(this, getString(R.string.login_success), Toast.LENGTH_SHORT)
                        .show()
                    startActivity(Intent(this, MainActivity::class.java).apply {
                        flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    })
                } else {
                    Toast.makeText(this, getString(R.string.login_fail), Toast.LENGTH_SHORT)
                        .show()
                }
            }
        }
    }

}