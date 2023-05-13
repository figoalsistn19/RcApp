package com.appbygox.rcapp.ui.`in`

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.appbygox.rcapp.R
import com.appbygox.rcapp.databinding.FragmentInventoryInBinding
import com.appbygox.rcapp.ui.stock.StockAdapter


class InventoryInFragment : Fragment() {

    private lateinit var binding: FragmentInventoryInBinding

    private val inventoryInAdapter : InventoryInAdapter by lazy {
        InventoryInAdapter()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentInventoryInBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.apply {
            rvListIn.apply {
                layoutManager = LinearLayoutManager(requireActivity())
                adapter = inventoryInAdapter
                setHasFixedSize(true)
            }
        }

//        getAppliedJob()
    }


}