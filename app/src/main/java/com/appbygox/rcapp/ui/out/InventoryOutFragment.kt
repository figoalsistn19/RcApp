package com.appbygox.rcapp.ui.out

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.appbygox.rcapp.data.model.InventoryOut
import com.appbygox.rcapp.data.remote.FirestoreService
import com.appbygox.rcapp.databinding.FragmentInventoryOutBinding
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber
import javax.inject.Inject

@AndroidEntryPoint
class InventoryOutFragment : Fragment() {

    @Inject
    lateinit var service: FirestoreService

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

        binding.floatingActionButton.setOnClickListener {
            val i = Intent(requireActivity(), InputOutActivity::class.java)
            startActivity(i)
        }
        getInventoryOut()
    }

    private fun getInventoryOut(){
        service.getInventoryOutNewest()
            .addSnapshotListener { value, e ->
                if (e != null) {
                    Timber.d("Listen failed.")
                    return@addSnapshotListener
                }
                val listIn = ArrayList<InventoryOut>()
                for (doc in value!!) {
                    val idInventoryOut = doc.getString("idInventoryOut")
                    val idItem = doc.getString("idItem")
                    val namaItem = doc.getString("namaItem")
                    val namaToko = doc.getString("namaToko")
                    val jumlahItem = doc.getLong("jumlahItem")
                    val tipeQuantity = doc.getString("tipeQuantity")
                    val returnItem = doc.getString("returnItem")
                    val namaPengirim = doc.getString("namaPengirim")
                    val platMobilPengirim = doc.getString("platMobilPengirim")
                    val noNota = doc.getString("noNota")
                    val keterangan = doc.getString("keterangan")
                    val createAt = doc.getLong("createAt")
                    val createBy = doc.getString("createBy")
                    listIn.add(InventoryOut(idInventoryOut, idItem, namaItem, namaToko, jumlahItem, tipeQuantity, returnItem, namaPengirim, platMobilPengirim, noNota, keterangan, createAt, createBy ))
                }
                inventoryOutAdapter.setData(listIn)
            }
    }

}