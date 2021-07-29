package com.abgp.colorcloud.ui.auth

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.abgp.colorcloud.R
import com.abgp.colorcloud.models.User
import com.abgp.colorcloud.services.SharedPrefServices

class UserListAdapter(
    context: Context,
    private val userList: List<User>,
    private val onItemClicked: (position: Int) -> Unit) : RecyclerView.Adapter<UserViewHolder>() {
    private val sharedPrefServices = SharedPrefServices(context)
    private val users = sharedPrefServices.getAllUsers()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        val context = parent.context
        val inflater = LayoutInflater.from(context)
        val view = inflater.inflate(R.layout.user_item_row, parent, false)
        return UserViewHolder(view, onItemClicked)
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        holder.bind(userList[position].name)
    }

    override fun getItemCount(): Int {
        return userList.size
    }
}
