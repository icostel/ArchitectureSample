package com.icostel.commons.adapter

import androidx.recyclerview.widget.RecyclerView
import com.icostel.commons.utils.livedata.SingleLiveEvent

abstract class BaseAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    val data = ArrayList<Any>()
    val selectedItem = SingleLiveEvent<Any>()

    override fun getItemCount(): Int = data.size
}