package com.example.studentmanagement

import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.core.os.bundleOf
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
        binding.recyclerView.adapter = StudentAdapter {
            findNavController().navigate(ListStudentFragmentDirections.actionListStudentFragmentToDetailStudentFragment(it))
        }
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
                    else -> false
                }
            }
        }, viewLifecycleOwner, Lifecycle.State.RESUMED)

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}