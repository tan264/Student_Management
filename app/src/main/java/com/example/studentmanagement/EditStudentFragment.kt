package com.example.studentmanagement

import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.studentmanagement.databinding.FragmentEditStudentBinding
import com.example.studentmanagement.model.Student
import com.example.studentmanagement.viewmodel.EditStudentViewModel
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import java.io.FileNotFoundException

class EditStudentFragment : Fragment() {
    private var _binding: FragmentEditStudentBinding ?= null
    val binding get() = _binding!!

    private val viewModel: EditStudentViewModel by viewModels()
    private val args: EditStudentFragmentArgs by navArgs()

    private val getContent = registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
        // Handle the returned Uri
        try {
            uri?.let {
                viewModel.setImage(it)
                Log.d("DEBUG", uri.toString())
                MaterialAlertDialogBuilder(requireContext()).setTitle(R.string.are_you_sure_to_choose_this_image)
                    .setMessage(R.string.message_upload_image).setCancelable(false)
                    .setNegativeButton(R.string.no) { _, _ ->
                        binding.pickAvatar.setImageResource(R.drawable.ic_baseline_person_24)
                    }
                    .setPositiveButton(R.string.yes) { dialog, _ ->
                        val type = requireContext().contentResolver.getType(uri)?.split("/")?.get(1)
                        if (type != null) {
                            viewModel.uploadImage(type, ContentUriRequestBody(requireContext().contentResolver, uri))
                        }
                        dialog.dismiss()
                    }.show()
            }
        } catch (e: FileNotFoundException) {
            e.printStackTrace()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentEditStudentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val student = args.student
        viewModel.student = student
        binding.apply {
            viewModel = this@EditStudentFragment.viewModel
            lifecycleOwner = this@EditStudentFragment.viewLifecycleOwner
        }
        viewModel.config()

        viewModel.statusEdit.observe(viewLifecycleOwner) { status ->
            status?.let {
                if (status) {
                    Toast.makeText(context, getString(R.string.edit_data_ok), Toast.LENGTH_LONG)
                        .show()
                    findNavController().navigate(EditStudentFragmentDirections.actionEditStudentFragmentToDetailStudentFragment(
                        student
                    ))
                } else {
                    Toast.makeText(context, getString(R.string.insert_data_fail), Toast.LENGTH_LONG)
                        .show()
                }
            }
        }

        binding.buttonCancel.setOnClickListener {
            findNavController().navigate(EditStudentFragmentDirections.actionEditStudentFragmentToDetailStudentFragment(student))
        }

        binding.buttonConfirm.setOnClickListener {
            if (binding.textInputEditTextName.text?.length!! > 0 && binding.textInputEditTextBirthday.text?.length!! > 0) {
                viewModel.updateData()
            } else {
                Toast.makeText(
                    context,
                    getString(R.string.enter_enough_information),
                    Toast.LENGTH_LONG
                ).show()
            }
        }

        binding.textInputEditTextBirthday.setOnClickListener {
            val dialog = DatePickerFragment(viewModel)
            dialog.show(parentFragmentManager, "datePicker")
        }

        binding.pickAvatar.setOnClickListener {
            getContent.launch("image/*")
        }

        super.onViewCreated(view, savedInstanceState)
    }

    override fun onDestroy() {
        _binding = null
        super.onDestroy()
    }
}