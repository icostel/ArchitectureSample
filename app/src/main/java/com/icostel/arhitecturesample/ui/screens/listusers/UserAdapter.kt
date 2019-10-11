package com.icostel.arhitecturesample.ui.screens.listusers

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.icostel.arhitecturesample.R
import com.icostel.arhitecturesample.ui.model.User
import com.icostel.commons.adapter.BaseAdapter

private const val TYPE_USER = 0
private const val TYPE_HEADER = 1

class UserAdapter(private val context: Context) : BaseAdapter() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder = when (viewType) {

        TYPE_HEADER -> object : RecyclerView.ViewHolder(LayoutInflater.from(this.context).inflate(R.layout.item_header, parent, false)) {}
        TYPE_USER -> UserViewHolder(context, LayoutInflater.from(this.context).inflate(R.layout.item_user, parent, false))
        else -> UserViewHolder(context, LayoutInflater.from(this.context).inflate(R.layout.item_user, parent, false))
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is UserViewHolder) {
            val user = data[position]
            holder.bind(user as User)
            holder.rootView.setOnClickListener {
                user.transitionBundle = holder.transitionOptions.toBundle()
                selectedItem.postValue(user)
            }
        }
    }

    fun updateItems(newUserList: List<User>) {
        data.clear()
        data.add(context.getString(R.string.all_users))
        data.addAll(newUserList)
        notifyDataSetChanged()
    }

    override fun getItemViewType(position: Int): Int = when (data[position]) {
        is String -> TYPE_HEADER
        is User -> TYPE_USER
        else -> TYPE_USER
    }
}
