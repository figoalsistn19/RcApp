package com.appbygox.rcapp.ui.`in`.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.appbygox.rcapp.R
import com.appbygox.rcapp.data.model.Item

class ItemAdapter(private var itemList: List<Item>, private val onClick: (Item) -> Unit) :
    RecyclerView.Adapter<ItemAdapter.ItemViewHolder>() {

    fun updateData(newItemList: List<Item>) {
        itemList = newItemList
        notifyDataSetChanged()
    }

    inner class ItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val textView = itemView.findViewById<TextView>(android.R.id.text1)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(android.R.layout.simple_list_item_1, parent, false)
        return ItemViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val currentItem = itemList[position]
        holder.textView.text = currentItem.namaItem
        holder.textView.setOnClickListener { onClick.invoke(currentItem) }
    }

    override fun getItemCount(): Int {
        return itemList.size
    }
}