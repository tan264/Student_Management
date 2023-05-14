package com.example.studentmanagement.fragment

import android.app.DatePickerDialog
import android.app.Dialog
import android.os.Bundle
import android.widget.DatePicker
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModel
import com.example.studentmanagement.viewmodel.AddStudentViewModel
import com.example.studentmanagement.viewmodel.EditStudentViewModel
import java.util.Calendar

class DatePickerFragment(private val viewModel: ViewModel) : DialogFragment(),
    DatePickerDialog.OnDateSetListener {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        // Create a new instance of DatePickerDialog and return it

        if (viewModel is AddStudentViewModel) {
            viewModel.birthday.value?.apply {
                val s = this.split("/")
                return DatePickerDialog(
                    requireContext(),
                    this@DatePickerFragment,
                    s[2].toInt(),
                    s[1].toInt() - 1,
                    s[0].toInt()
                )
            }
        } else if (viewModel is EditStudentViewModel) {
            viewModel.birthday.value?.apply {
                val s = this.split("/")
                return DatePickerDialog(
                    requireContext(),
                    this@DatePickerFragment,
                    s[2].toInt(),
                    s[1].toInt() - 1,
                    s[0].toInt()
                )
            }
        }
        return DatePickerDialog(
            requireContext(),
            this,
            Calendar.getInstance().get(Calendar.YEAR) - 2,
            3,
            26
        )

    }

    override fun onDateSet(view: DatePicker, year: Int, month: Int, day: Int) {
        // Do something with the date chosen by the user
        if (viewModel is AddStudentViewModel) {
            viewModel.setBirthday(year, month, day)
            viewModel.setAge(year, month, day)
        } else if (viewModel is EditStudentViewModel) {
            viewModel.setBirthday(year, month, day)
            viewModel.setAge(year, month, day)
        }
    }
}