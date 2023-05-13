package com.appbygox.rcapp.ui.out

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.appbygox.rcapp.databinding.ItemListInBinding
import com.appbygox.rcapp.databinding.ItemListOutBinding
import com.appbygox.rcapp.ui.`in`.InventoryInAdapter

class InventoryOutAdapter constructor (
//    private val data: MutableList<AppliedJob> = mutableListOf()
) : RecyclerView.Adapter<InventoryOutAdapter.FileViewHolder>() {

//    fun setData(data: MutableList<AppliedJob>) {
//        this.data.clear()
//        this.data.addAll(data)
//        notifyDataSetChanged()
//    }

    inner class FileViewHolder(private val binding: ItemListOutBinding) :
        RecyclerView.ViewHolder(binding.root) {

//        fun bind(data: AppliedJob) {
//            var status = data.status
//
//            binding.appliadFotoCompany.load(data.company_photo)
//            binding.appliedTitleJob.text = data.job_title
//            binding.appliedNameCompany.text = data.company_name
//            binding.appliedStatus.text = status
//
//            if(status == "Pending"){
//                binding.appliedMessage.text = "Kindly wait."
//                binding.appliedStatus.setTextColor(Color.parseColor("#DBD200"))
//            } else if (status == "Accepted") {
//                binding.appliedMessage.text = "You have passed the administrative stage, Please wait for the recruiter to contact you for further instructions."
//                binding.appliedStatus.setTextColor(Color.parseColor("#0DD109"))
//            } else if (status == "Rejected"){
//                binding.appliedMessage.text = "Better luck next time!"
//                binding.appliedStatus.setTextColor(Color.parseColor("DB0000"))
//            }
//        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): InventoryOutAdapter.FileViewHolder =
        FileViewHolder(
            ItemListOutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )

    override fun onBindViewHolder(holder: InventoryOutAdapter.FileViewHolder, position: Int) {
//        val item = data[position]
//        holder.bind(item)
    }



}