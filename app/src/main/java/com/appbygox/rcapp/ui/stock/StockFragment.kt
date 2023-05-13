package com.appbygox.rcapp.ui.stock

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.appbygox.rcapp.R
import com.appbygox.rcapp.databinding.FragmentInventoryInBinding
import com.appbygox.rcapp.databinding.FragmentStockBinding

class StockFragment : Fragment() {

    private lateinit var binding:FragmentStockBinding

    private val stockAdapter : StockAdapter by lazy {
        StockAdapter()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentStockBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.apply {
            rvListStock.apply {
                layoutManager = LinearLayoutManager(requireActivity())
                adapter = stockAdapter
                setHasFixedSize(true)
            }
        }

//        getAppliedJob()
    }

}