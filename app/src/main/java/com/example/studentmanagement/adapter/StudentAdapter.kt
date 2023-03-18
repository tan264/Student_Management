package com.example.studentmanagement.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.studentmanagement.databinding.RowStudentItemBinding
import com.example.studentmanagement.model.Student

class StudentAdapter(
    private val click: (Student) -> Unit,
    private val longClick: (Student, View) -> Unit
) : ListAdapter<Student, StudentAdapter.StudentViewHolder>(DiffCallback) {

    companion object DiffCallback : DiffUtil.ItemCallback<Student>() {
        override fun areItemsTheSame(oldItem: Student, newItem: Student): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Student, newItem: Student): Boolean {
            return oldItem.fullName == newItem.fullName
                    && oldItem.nickName == newItem.nickName
                    && oldItem.age == newItem.age
                    && oldItem.classStudent == newItem.classStudent
                    && oldItem.gender == newItem.gender
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StudentViewHolder {
        return StudentViewHolder(
            RowStudentItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: StudentViewHolder, position: Int) {
        val student = getItem(position)
        holder.bind(student)
        holder.itemView.setOnClickListener {
            click(student)
        }
        holder.itemView.setOnLongClickListener {
            longClick(student, it)
            true
        }
        holder.textViewPotion.setOnClickListener {
            longClick(student, it)
        }
    }

    class StudentViewHolder(private var binding: RowStudentItemBinding) : ViewHolder(binding.root) {
        val textViewPotion = binding.textViewOption
        fun bind(student: Student) {
            binding.student = student
            binding.executePendingBindings()
        }
    }
}