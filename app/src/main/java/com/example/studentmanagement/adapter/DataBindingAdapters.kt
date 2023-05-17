package com.example.studentmanagement.adapter

import android.net.Uri
import android.view.View
import android.widget.ImageView
import android.widget.ProgressBar
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.studentmanagement.R
import com.example.studentmanagement.model.Student
import com.example.studentmanagement.network.ApiStatus
import com.example.studentmanagement.network.BASE_URL

@BindingAdapter(value = ["imageUri", "defaultUrl"], requireAll = false)
fun setImageUri(view: ImageView, imageUri: Uri?, defaultImageUrl: String?) {
    if (imageUri == null) {
        if (defaultImageUrl == null) {
            view.setImageResource(R.drawable.ic_baseline_person_24)
        } else {
            bindImage(view, defaultImageUrl)
        }
    } else {
        view.setImageURI(imageUri)
    }
}

@BindingAdapter("imageUrl")
fun bindImage(view: ImageView, imageUrl: String?) {
    imageUrl?.let {
        view.load(BASE_URL + "image/$imageUrl") {
            placeholder(R.drawable.loading_animation)
            error(R.drawable.ic_broken_image)
        }
    }
}

@BindingAdapter("listData")
fun bindRecycleView(recyclerView: RecyclerView, data: List<Student>?) {
    val adapter = recyclerView.adapter as StudentAdapter
    adapter.submitList(data)
}


@BindingAdapter("status")
fun bindStatus(progressBar: ProgressBar, status: ApiStatus?) {
    status?.let {
        when (status) {
            ApiStatus.LOADING -> {
                progressBar.visibility = View.VISIBLE
            }

            ApiStatus.DONE -> {
                progressBar.visibility = View.GONE
            }

            ApiStatus.ERROR -> {
                progressBar.visibility = View.VISIBLE
            }
        }
    }
}