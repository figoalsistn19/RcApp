package com.appbygox.rcapp.ui.`in`

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
import com.appbygox.rcapp.data.model.Item
import com.appbygox.rcapp.data.remote.FirestoreService
import com.appbygox.rcapp.databinding.ActivityInputInBinding
import com.appbygox.rcapp.orZero
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class InputInActivity : AppCompatActivity() {

    private lateinit var binding: ActivityInputInBinding

    @Inject
    lateinit var service: FirestoreService

    private var dueDateMillis: Long = System.currentTimeMillis()
    private var posisi_item: Item? = Item()
    private var posisi_retur: String? = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityInputInBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()

        val listItem = arrayListOf<Item>()
        service.getItems()
            .addSnapshotListener { value, error ->
                for (doc in value!!) {
                    val idItem = doc.getString("idItem").orEmpty()
                    val namaItem = doc.getString("namaItem").orEmpty()
                    val tipeQuantity = doc.getString("tipeQuantity").orEmpty()
                    val namaSupplier = doc.getString("namaSupplier").orEmpty()
                    listItem.add(Item(idItem, namaItem, tipeQuantity, namaSupplier))
                }
                val adapter =
                    ArrayAdapter(this, R.layout.spinner_item, listItem.map { it.namaItem })
                binding.spinnerNamaItem.adapter = adapter
            }

        binding.spinnerNamaItem.onItemSelectedListener = object :
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
        binding.spinner.adapter = adapterRetur

        binding.spinner.onItemSelectedListener = object :
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

        binding.btnBack.setOnClickListener {
            onBackPressed()
        }

        binding.btnBatal.setOnClickListener {
            onBackPressed()
        }

        binding.btnSimpan.setOnClickListener {
            if (binding.checkBox.isChecked) {
                inputIn()
            } else {
                Toast.makeText(this@InputInActivity, "Check terlebih dahulu", Toast.LENGTH_SHORT)
                    .show()
            }
        }
    }

    private fun inputIn() {
        var namaUser = LoginPref(this).getNamaUser().toString()

        var idItem = posisi_item?.idItem
        var namaItem = posisi_item?.namaItem
        var namaSupplier = posisi_item?.namaSupplier
        var jumlahItem = binding.editQtyItem.text.toString().toLong()
        var tipeQuantity = posisi_item?.tipeQuantity
        var returnItem = posisi_retur.toString()
        var namaPengirim = binding.editNamaEkspedisi.text.toString()
        var platMobilPengirim = binding.editPlatEkspedisi.text.toString()
        var noNota = binding.editNoNota.text.toString()
        var keterangan = binding.editKet.text.toString()
        var createAt = dueDateMillis
        var createBy = namaUser

        when {
            jumlahItem.toString().isEmpty() -> {
                binding.editQtyItem.error = "Masukan Jumlah Item"
            }
            namaPengirim.isEmpty() -> {
                binding.editNamaEkspedisi.error = "Masukkan Nama Ekspedisi"
            }
            platMobilPengirim.isEmpty() -> {
                binding.editPlatEkspedisi.error = "Masukkan Plat Ekspedisi"
            }
            noNota.isEmpty() -> {
                binding.editNoNota.error = "Masukkan No. Nota"
            }
            keterangan.isEmpty() -> {
                binding.editKet.error = "Masukan Keterangan"
            }
            else -> {

                val inventoryIn = InventoryIn(
                    idItem = idItem,
                    namaItem = namaItem,
                    createAt = createAt,
                    namaSupplier = namaSupplier,
                    namaPengirim = namaPengirim,
                    platMobilPengirim = platMobilPengirim,
                    createBy = createBy,
                    noNota = noNota,
                    jumlahItem = jumlahItem,
                    returnItem = returnItem,
                    keterangan = keterangan,
                    tipeQuantity = tipeQuantity
                )
                service.addInventoryIn(
                    inventoryIn, success = { success ->
                        if (success) {
                            service.checkStock(inventoryIn.idItem.orEmpty(), isStockFirstTime = {
                                if (it) {
                                    service.addStock(inventoryIn, success = { success ->
                                        if (success) {
                                            val i =
                                                Intent(
                                                    this@InputInActivity,
                                                    MainActivity::class.java
                                                )
                                            i.flags =
                                                Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                                            startActivity(i)

                                            Toast.makeText(
                                                this@InputInActivity,
                                                "Berhasil Input Barang",
                                                Toast.LENGTH_LONG
                                            ).show()
                                        } else {
                                            Toast.makeText(
                                                this@InputInActivity,
                                                "Gagal Input Barang",
                                                Toast.LENGTH_LONG
                                            ).show()
                                        }
                                    })
                                } else {
                                    val stockExisting =
                                        service.getStock(inventoryIn.idItem.orEmpty())
                                    service.updateStock(
                                        inventoryIn.idItem.orEmpty(),
                                        stockExisting,
                                        inventoryIn.jumlahItem.orZero(),
                                        true,
                                        success = { success ->
                                            if (success) {
                                                val i = Intent(
                                                    this@InputInActivity,
                                                    MainActivity::class.java
                                                )
                                                i.flags =
                                                    Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                                                startActivity(i)

                                                Toast.makeText(
                                                    this@InputInActivity,
                                                    "Berhasil Input Barang",
                                                    Toast.LENGTH_LONG
                                                ).show()
                                            } else {
                                                Toast.makeText(
                                                    this@InputInActivity,
                                                    "Gagal Input Barang",
                                                    Toast.LENGTH_LONG
                                                ).show()
                                            }
                                        })
                                }
                            })
                        } else {
                            Toast.makeText(
                                this@InputInActivity,
                                "Gagal Input Barang",
                                Toast.LENGTH_LONG
                            ).show()
                        }
                    })

            }
        }

    }
}