package com.appbygox.rcapp.ui.`in`

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.appbygox.rcapp.R
import com.appbygox.rcapp.data.model.InventoryIn
import com.appbygox.rcapp.data.model.Stock
import com.appbygox.rcapp.data.remote.FirestoreService
import com.appbygox.rcapp.databinding.FragmentInventoryInBinding
import com.appbygox.rcapp.ui.stock.StockAdapter
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber
import javax.inject.Inject

@AndroidEntryPoint
class InventoryInFragment : Fragment() {

    @Inject
    lateinit var service: FirestoreService

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
        getInventoryIn()
    }

    private fun getInventoryIn(){
        service.getInventoryInNewest()
            .addSnapshotListener { value, e ->
                if (e != null) {
                    Timber.d("Listen failed.")
                    return@addSnapshotListener
                }
                val listIn = ArrayList<InventoryIn>()
                for (doc in value!!) {
                    val idInventoryIn = doc.getString("idInventoryIn")
                    val idItem = doc.getString("idItem")
                    val namaItem = doc.getString("namaItem")
                    val namaSupplier = doc.getString("namaSupplier")
                    val jumlahItem = doc.getLong("jumlahItem")
                    val tipeQuantity = doc.getString("tipeQuantity")
                    val returnItem = doc.getString("returnItem")
                    val namaPengirim = doc.getString("namaPengirim")
                    val platMobilPengirim = doc.getString("platMobilPengirim")
                    val noNota = doc.getString("noNota")
                    val keterangan = doc.getString("keterangan")
                    val createAt = doc.getLong("createAt")
                    val createBy = doc.getString("createBy")
                    listIn.add(InventoryIn(idInventoryIn, idItem, namaItem, namaSupplier, jumlahItem, tipeQuantity, returnItem, namaPengirim, platMobilPengirim, noNota, keterangan, createAt, createBy ))
                }
                inventoryInAdapter.setData(listIn)
            }
    }

}