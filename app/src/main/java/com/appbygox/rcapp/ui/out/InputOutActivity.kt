package com.appbygox.rcapp.ui.out

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import com.appbygox.rcapp.MainActivity
import com.appbygox.rcapp.R
import com.appbygox.rcapp.data.LoginPref
import com.appbygox.rcapp.data.model.InventoryIn
import com.appbygox.rcapp.data.model.InventoryOut
import com.appbygox.rcapp.data.model.Item
import com.appbygox.rcapp.data.remote.FirestoreService
import com.appbygox.rcapp.databinding.ActivityInputOutBinding
import com.appbygox.rcapp.orZero
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class InputOutActivity : AppCompatActivity() {

    private  lateinit var binding: ActivityInputOutBinding

    @Inject
    lateinit var service: FirestoreService
    private var dueDateMillis: Long = System.currentTimeMillis()
    private var posisi_item: Item? = Item()
    private var posisi_retur: String? = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityInputOutBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()

        val listItem = arrayListOf<Item>()
        service.getItems()
            .addSnapshotListener { value, error ->
                for (doc in value!!){
                    val idItem = doc.getString("idItem").orEmpty()
                    val namaItem = doc.getString("namaItem").orEmpty()
                    val tipeQuantity = doc.getString("tipeQuantity").orEmpty()
                    val namaSupplier = doc.getString("namaSupplier").orEmpty()
                    listItem.add(Item(idItem,namaItem,tipeQuantity,namaSupplier))
                }
                val adapter = ArrayAdapter(this, R.layout.spinner_item, listItem.map { it.namaItem })
                binding.spinnerNamaItemOut.adapter = adapter
            }
        val adapter = ArrayAdapter(this, R.layout.spinner_item, listItem.map { it.namaItem })
        binding.spinnerNamaItemOut.adapter = adapter

        binding.spinnerNamaItemOut.onItemSelectedListener = object :
            AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View, position: Int, id: Long
            ) {
                posisi_item = listItem[position]
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                // write code to perform some action
            }
        }

        val listRetur = resources.getStringArray(R.array.posisi_list)
        val adapterRetur = ArrayAdapter(this, R.layout.spinner_item, listRetur)
        binding.spinnerOut.adapter = adapterRetur

        binding.spinnerOut.onItemSelectedListener = object :
            AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View, position: Int, id: Long
            ) {
                posisi_retur = listRetur[position]
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                // write code to perform some action
            }
        }

        binding.btnBatal.setOnClickListener {
            onBackPressed()
        }

        binding.btnBack.setOnClickListener {
            onBackPressed()
        }

        binding.btnSimpan.setOnClickListener {
            if (binding.checkBox.isChecked) {
                inputOut()
            } else {
                Toast.makeText(this@InputOutActivity, "Check terlebih dahulu", Toast.LENGTH_SHORT)
                    .show()
            }
        }

    }

    private fun inputOut() {

        val idItem = posisi_item?.idItem
        val namaItem = posisi_item?.namaItem
        val jumlahItem = binding.editQtyItem.text.toString().toLong()
        val namaPengirim = binding.editNamaPengirim.text.toString()
        val platEkspedisi = binding.editPlatPengirim.text.toString()
        val noNota = binding.editNoNota.text.toString()
        val returTipe = posisi_retur.toString()
        val ket = binding.editKet.text.toString()
        val inputTime = dueDateMillis
        val namaToko = binding.editNamaToko.text.toString()
        val tipeQuantity = posisi_item?.tipeQuantity
        val checkBox = binding.checkBox.isChecked

        when {
            binding.editQtyItem.text.toString().isEmpty() -> {
                binding.editQtyItem.error = "Masukan Jumlah Item"
            }
            namaPengirim.isEmpty() -> {
                binding.editNamaPengirim.error = "Masukkan Nama Ekspedisi"
            }
            platEkspedisi.isEmpty() -> {
                binding.editPlatPengirim.error = "Masukkan Plat Ekspedisi"
            }
            noNota.isEmpty() -> {
                binding.editNoNota.error = "Masukkan No. Nota"
            }
            checkBox.not() -> {
                binding.checkBox.error= "Cek dulu"
            }

            else -> {
                val nama_user = LoginPref(this).getNamaUser().toString()

                val inventoryOut = InventoryOut(
                    idItem = idItem,
                    namaItem = namaItem,
                    createAt = inputTime,
                    namaToko = namaToko,
                    namaPengirim = namaPengirim,
                    platMobilPengirim = platEkspedisi,
                    createBy = nama_user,
                    noNota = noNota,
                    jumlahItem = jumlahItem,
                    returnItem = returTipe,
                    keterangan = ket,
                    tipeQuantity = tipeQuantity
                )
                service.addInventoryOut(
                    inventoryOut, success = { success ->
                        if (success) {
                            service.checkStock(inventoryOut.idItem.orEmpty(), isStockFirstTime = {
                                if (it) {
                                    Toast.makeText(
                                        this@InputOutActivity,
                                        "Barang tidak ada di stok",
                                        Toast.LENGTH_LONG
                                    ).show()
                                } else {
                                    service.getStock(inventoryOut.idItem.orEmpty(), stock = {
                                        val stockExisting = it.orZero()
                                        service.updateStock(
                                            inventoryOut.idItem.orEmpty(),
                                            stockExisting,
                                            inventoryOut.jumlahItem.orZero(),
                                            false,
                                            success = { success ->
                                                if (success) {
                                                    val i = Intent(
                                                        this@InputOutActivity,
                                                        MainActivity::class.java
                                                    )
                                                    i.flags =
                                                        Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                                                    startActivity(i)

                                                    Toast.makeText(
                                                        this@InputOutActivity,
                                                        "Berhasil Input Barang",
                                                        Toast.LENGTH_LONG
                                                    ).show()
                                                } else {
                                                    Toast.makeText(
                                                        this@InputOutActivity,
                                                        "Gagal Input Barang",
                                                        Toast.LENGTH_LONG
                                                    ).show()
                                                }
                                            })
                                    })
                                }
                            })
                        } else {
                            Toast.makeText(
                                this@InputOutActivity,
                                "Gagal Input Barang",
                                Toast.LENGTH_LONG
                            ).show()
                        }
                    })
            }

        }
    }
}