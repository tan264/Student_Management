package com.example.studentmanagement

import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.PopupMenu
import android.widget.Toast
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.studentmanagement.adapter.StudentAdapter
import com.example.studentmanagement.databinding.FragmentListStudentBinding
import com.example.studentmanagement.model.Student
import com.example.studentmanagement.viewmodel.ListStudentViewModel

class ListStudentFragment : Fragment() {

    private val viewModel: ListStudentViewModel by viewModels()

    private var _binding: FragmentListStudentBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentListStudentBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = this
        binding.viewModel = viewModel
        binding.recyclerView.adapter = StudentAdapter(
            {
                onClick(it)
            },
            { student, view ->
                onOptionClick(student, view)
            }
        )
        binding.recyclerView.addItemDecoration(
            DividerItemDecoration(
                context,
                (binding.recyclerView.layoutManager as LinearLayoutManager).orientation
            )
        )
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val menuHost: MenuHost = requireActivity()

        menuHost.addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.layout_menu, menu)
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                return when (menuItem.itemId) {
                    R.id.item_add -> {
                        findNavController().navigate(ListStudentFragmentDirections.actionListStudentFragmentToAddStudentFragment())
                        true
                    }
                    R.id.item_refresh -> {
                        viewModel.getListStudents()
                        Toast.makeText(requireContext(), "List updated", Toast.LENGTH_SHORT).show()
                        true
                    }
                    else -> false
                }
            }
        }, viewLifecycleOwner, Lifecycle.State.RESUMED)

        viewModel.isDeleteSuccess.observe(viewLifecycleOwner) { isDeleted ->
            if (isDeleted == 1) {
                Log.d("DEBUG", "deleted")
                Toast.makeText(requireContext(), "Deleted", Toast.LENGTH_SHORT).show()
                viewModel.getListStudents()
            } else if (isDeleted == -1) {
                Log.d("DEBUG", "fail")
                Toast.makeText(requireContext(), "Failed to delete", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun onClick(student: Student) {
        findNavController().navigate(
            ListStudentFragmentDirections.actionListStudentFragmentToDetailStudentFragment(
                student
            )
        )
    }

    private fun onOptionClick(student: Student, view: View) {
        val popupMenu = PopupMenu(requireContext(), view)
        popupMenu.inflate(R.menu.layout_context_menu)
        popupMenu.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.menu_delete -> {
                    viewModel.deleteStudent(student.id)
                    true
                }

                R.id.menu_edit -> {
                    findNavController().navigate(
                        ListStudentFragmentDirections.actionListStudentFragmentToEditStudentFragment(
                            student
                        )
                    )
                    true
                }
                else -> {
                    Toast.makeText(requireContext(), "nothing", Toast.LENGTH_SHORT).show()
                    false
                }
            }
        }
        popupMenu.show()
    }

    override fun onResume() {
        super.onResume()

        viewModel.getListStudents()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}