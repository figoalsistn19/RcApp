package com.appbygox.rcapp.ui.out

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.appbygox.rcapp.R
import com.appbygox.rcapp.databinding.FragmentInventoryInBinding
import com.appbygox.rcapp.databinding.FragmentInventoryOutBinding
import com.appbygox.rcapp.ui.stock.StockAdapter

class InventoryOutFragment : Fragment() {

    private lateinit var binding:FragmentInventoryOutBinding

    private val inventoryOutAdapter : InventoryOutAdapter by lazy {
        InventoryOutAdapter()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentInventoryOutBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.apply {
            rvListOut.apply {
                layoutManager = LinearLayoutManager(requireActivity())
                adapter = inventoryOutAdapter
                setHasFixedSize(true)
            }
        }

//        getAppliedJob()
    }

}