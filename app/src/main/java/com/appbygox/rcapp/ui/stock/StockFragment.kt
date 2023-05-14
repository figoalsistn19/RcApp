package com.appbygox.rcapp.ui.stock

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.appbygox.rcapp.R
import com.appbygox.rcapp.data.model.Stock
import com.appbygox.rcapp.data.remote.FirestoreService
import com.appbygox.rcapp.databinding.FragmentInventoryInBinding
import com.appbygox.rcapp.databinding.FragmentStockBinding
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber
import javax.inject.Inject

@AndroidEntryPoint
class StockFragment : Fragment() {

    @Inject
    lateinit var service: FirestoreService

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

        binding.floatingActionButton3.setOnClickListener {
            val i = Intent(requireActivity(), InputItemActivity::class.java)
            startActivity(i)
        }

        getStocks()
    }

    private fun getStocks(){
        service.getStockNewest()
            .addSnapshotListener { value, e ->
                if (e != null) {
                    Timber.d("Listen failed.")
                    return@addSnapshotListener
                }
                val listStock = ArrayList<Stock>()
                for (doc in value!!) {
                    val idItem = doc.getString("idItem")
                    val namaItem = doc.getString("namaItem")
                    val namaSupplier = doc.getString("namaSupplier")
                    val jumlahItem = doc.getLong("jumlahItem")
                    val tipeQuantity = doc.getString("tipeQuantity")
                    val updateAt = doc.getLong("updateAt")
                    listStock.add(Stock(idItem, namaItem, namaSupplier, jumlahItem, tipeQuantity, updateAt))
                }
                stockAdapter.setData(listStock)
            }
    }

}