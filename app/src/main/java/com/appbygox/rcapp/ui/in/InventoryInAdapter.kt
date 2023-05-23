package com.appbygox.rcapp.ui.`in`

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.appbygox.rcapp.data.model.InventoryIn
import com.appbygox.rcapp.databinding.ItemListInBinding
import com.appbygox.rcapp.toFormatDate

class InventoryInAdapter constructor (
    private val data: MutableList<InventoryIn> = mutableListOf()
) : RecyclerView.Adapter<InventoryInAdapter.FileViewHolder>() {

    fun setData(data: MutableList<InventoryIn>) {
        this.data.clear()
        this.data.addAll(data)
        notifyDataSetChanged()
    }

    inner class FileViewHolder(private val binding: ItemListInBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(data: InventoryIn) {
            with(binding){
                itemIn.text = "Item : " + data.namaItem
                supplierIn.text = "Supplier : " + data.namaSupplier
                quantityIn.text = "Jumlah Item : " + data.jumlahItem + " " + data.tipeQuantity
                expedisiIn.text = "Ekspedisi : " + data.namaPengirim
                platMobilIn.text = "Plat Mobil : " + data.platMobilPengirim
                returIn.text = "Retur / Exp : " + data.returnItem
                noNotaIn.text = "No. Nota : " + data.noNota
                nameUser.text = "Diinput oleh : " + data.createBy
                timeInputIn.text = data.createAt.toFormatDate()
                ketIn.text = data.keterangan
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): InventoryInAdapter.FileViewHolder =
        FileViewHolder(
            ItemListInBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )

    override fun onBindViewHolder(holder: InventoryInAdapter.FileViewHolder, position: Int) {
        val item = data[position]
        holder.bind(item)
    }

    override fun getItemCount(): Int = data.size
}