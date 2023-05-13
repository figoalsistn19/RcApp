package com.appbygox.rcapp.ui.stock

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.appbygox.rcapp.data.model.Stock
import com.appbygox.rcapp.databinding.ItemListOutBinding
import com.appbygox.rcapp.databinding.ItemListStockBinding
import com.appbygox.rcapp.toFormatDate
import com.appbygox.rcapp.ui.out.InventoryOutAdapter

class StockAdapter constructor (
    private val data: MutableList<Stock> = mutableListOf()
) : RecyclerView.Adapter<StockAdapter.FileViewHolder>() {

    fun setData(data: MutableList<Stock>) {
        this.data.clear()
        this.data.addAll(data)
        notifyDataSetChanged()
    }

    inner class FileViewHolder(private val binding: ItemListStockBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(data: Stock) {
            with(binding){
                itemIn.text = "Item : " + data.namaItem
                supplierIn.text = "Supplier : " + data.namaSupplier
                quantityIn.text = "Jumlah item : " + data.jumlahItem
                timeInputIn.text = "diperbaharui : " + data.updateAt.toFormatDate()
            }

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StockAdapter.FileViewHolder =
        FileViewHolder(
            ItemListStockBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )

    override fun onBindViewHolder(holder: StockAdapter.FileViewHolder, position: Int) {
        val item = data[position]
        holder.bind(item)
    }

    override fun getItemCount(): Int {
        return data.size
    }
}