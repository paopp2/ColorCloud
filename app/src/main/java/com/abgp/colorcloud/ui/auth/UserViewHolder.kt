package com.abgp.colorcloud.ui.auth

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.abgp.colorcloud.R

class UserViewHolder(
    view: View,
    private val onItemClicked: (position: Int) -> Unit
) : RecyclerView.ViewHolder(view), View.OnClickListener {
    private val tvUsername = view.findViewById<TextView>(R.id.tvUsername)

    init {
        view.setOnClickListener(this)
    }

    override fun onClick(v: View) {
        val position = adapterPosition
        onItemClicked(position)
    }

    fun bind(name: String) {
        tvUsername.text = name
    }
}
