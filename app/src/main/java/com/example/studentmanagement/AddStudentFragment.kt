package com.example.studentmanagement

import android.app.DatePickerDialog
import android.app.Dialog
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.DatePicker
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts.GetContent
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.studentmanagement.databinding.FragmentAddStudentBinding
import com.example.studentmanagement.viewmodel.AddStudentViewModel
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import java.io.FileNotFoundException
import java.util.*

class AddStudentFragment : Fragment() {
    private var _binding: FragmentAddStudentBinding? = null
    private val binding get() = _binding!!

    private val viewModel: AddStudentViewModel by viewModels()

    private val getContent = registerForActivityResult(GetContent()) { uri: Uri? ->
        // Handle the returned Uri
        try {
            uri?.let {
                viewModel.setImage(uri)
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
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAddStudentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.apply {
            viewModel = this@AddStudentFragment.viewModel
            lifecycleOwner = this@AddStudentFragment.viewLifecycleOwner
        }
        binding.textInputEditTextBirthday.setOnClickListener {
            val dialog = DatePickerFragment(viewModel)
            dialog.show(parentFragmentManager, "datePicker")
        }

        binding.pickAvatar.setOnClickListener {
            getContent.launch("image/*")
        }

        binding.buttonConfirm.setOnClickListener {
            if (binding.textInputEditTextName.text?.length!! > 0 && binding.textInputEditTextBirthday.text?.length!! > 0) {
                viewModel.insertData()
            } else {
                Toast.makeText(
                    context,
                    getString(R.string.enter_enough_information),
                    Toast.LENGTH_LONG
                ).show()
            }
        }

        binding.buttonCancel.setOnClickListener {
            findNavController().navigate(AddStudentFragmentDirections.actionAddStudentFragmentToListStudentFragment())
        }

        viewModel.statusInsert.observe(viewLifecycleOwner) { status ->
            status?.let {
                if (status) {
                    Toast.makeText(context, getString(R.string.insert_data_ok), Toast.LENGTH_LONG)
                        .show()
                findNavController().navigate(AddStudentFragmentDirections.actionAddStudentFragmentToListStudentFragment())
                } else {
                    Toast.makeText(context, getString(R.string.insert_data_fail), Toast.LENGTH_LONG)
                        .show()
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
