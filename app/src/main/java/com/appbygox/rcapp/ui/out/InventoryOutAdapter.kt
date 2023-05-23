package com.appbygox.rcapp.ui.out

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.appbygox.rcapp.data.model.InventoryOut
import com.appbygox.rcapp.databinding.ItemListOutBinding
import com.appbygox.rcapp.toFormatDate

class InventoryOutAdapter constructor (
    private val data: MutableList<InventoryOut> = mutableListOf()
) : RecyclerView.Adapter<InventoryOutAdapter.FileViewHolder>() {

    fun setData(data: MutableList<InventoryOut>) {
        this.data.clear()
        this.data.addAll(data)
        notifyDataSetChanged()
    }

    inner class FileViewHolder(private val binding: ItemListOutBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(data: InventoryOut) {
            with(binding){
                itemIn.text = "Item : " + data.namaItem
                supplierIn.text = "Toko : " + data.namaToko
                quantityIn.text = "Jumlah Item : " + data.jumlahItem + " " + data.tipeQuantity
                pengirimOut.text = "Pengirim : " + data.namaPengirim
                platMobilOut.text = "Plat Mobil : " + data.platMobilPengirim
                returOut.text = "Retur / Exp : " + data.returnItem
                noNotaOut.text = "No. Nota : " + data.noNota
                nameUser.text = "Diinput oleh : " + data.createBy
                timeInputOut.text = data.createAt.toFormatDate()
                ketOut.text = "Keterangan : " + data.keterangan
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): InventoryOutAdapter.FileViewHolder =
        FileViewHolder(
            ItemListOutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )

    override fun onBindViewHolder(holder: InventoryOutAdapter.FileViewHolder, position: Int) {
        val item = data[position]
        holder.bind(item)
    }

    override fun getItemCount(): Int = data.size
}