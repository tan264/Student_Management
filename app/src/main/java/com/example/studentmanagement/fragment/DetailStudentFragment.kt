package com.example.studentmanagement.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.example.studentmanagement.databinding.FragmentDetailStudentBinding
import com.example.studentmanagement.model.Student
import com.example.studentmanagement.viewmodel.DetailStudentViewModel

class DetailStudentFragment : Fragment() {

    private var _binding: FragmentDetailStudentBinding? = null
    private val binding: FragmentDetailStudentBinding get() = _binding!!

    private val viewModel: DetailStudentViewModel by viewModels()
    private val args: DetailStudentFragmentArgs by navArgs()
    lateinit var student: Student

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentDetailStudentBinding.inflate(inflater, container, false)
        binding.apply {
            viewmodel = viewModel
            lifecycleOwner = viewLifecycleOwner
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        student = args.student!!
        viewModel.myInit(student)
        super.onViewCreated(view, savedInstanceState)
    }

    override fun onResume() {
        super.onResume()
        viewModel.getStudent(student.id)
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}