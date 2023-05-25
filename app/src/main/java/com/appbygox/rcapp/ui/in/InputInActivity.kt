package com.appbygox.rcapp.ui.`in`

import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.ListView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.appbygox.rcapp.MainActivity
import com.appbygox.rcapp.R
import com.appbygox.rcapp.data.LoginPref
import com.appbygox.rcapp.data.model.InventoryIn
import com.appbygox.rcapp.data.model.Item
import com.appbygox.rcapp.data.remote.FirestoreService
import com.appbygox.rcapp.databinding.ActivityInputInBinding
import com.appbygox.rcapp.orZero
import com.appbygox.rcapp.ui.`in`.adapter.ItemAdapter
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@Suppress("DEPRECATION")
@AndroidEntryPoint
class InputInActivity : AppCompatActivity() {

    private lateinit var binding: ActivityInputInBinding
    private lateinit var itemAdapter: ItemAdapter

    @Inject
    lateinit var service: FirestoreService

    private var dueDateMillis: Long = System.currentTimeMillis()
    private var posisi_item: Item? = Item()
    private var posisi_retur: String? = ""
    private lateinit var dialog: Dialog



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
//                val adapter =
//                    ArrayAdapter(this, R.layout.spinner_item, listItem.map { it.namaItem })
//                binding.spinnerNamaItem.adapter = adapter
                binding.newSpinner.setOnClickListener {
                    dialog = Dialog(this@InputInActivity)
                    dialog.setContentView(R.layout.dialog_searchable_spinner)
                    dialog.window?.setLayout(650,800)
                    dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
                    dialog.show()

                    val editText: EditText = dialog.findViewById(R.id.edit_text)
                    val listView: RecyclerView = dialog.findViewById(R.id.list_view)
                    listView.layoutManager = LinearLayoutManager(this)

                    itemAdapter = ItemAdapter(listItem, onClick = {
                        binding.newSpinner.text = it.namaItem
                        posisi_item = it
                        dialog.dismiss()
                    })
                    listView.adapter = itemAdapter

                    val textWatcher = object : TextWatcher {
                        override fun afterTextChanged(s: Editable?) {
                        }
                        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                        }
                        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                            itemAdapter.updateData(listItem.filter { it.namaItem.contains(s.toString().toLowerCase()) })
                        }
                    }

                    editText.addTextChangedListener(textWatcher)

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
                                    service.getStock(inventoryIn.idItem.orEmpty(), stock = {
                                        val stockExisting = it.orZero()
                                        service.updateStock(
                                            inventoryIn.idItem.orEmpty(),
                                            stockExisting,
                                            inventoryIn.jumlahItem.orZero(),
                                            true,
                                            inventoryIn.createAt.orZero(),
                                            success = { success ->
                                                if (success) {
                                                    val i = Intent(this, MainActivity::class.java)
                                                    i.putExtra("selected_fragment", R.id.navigation_invent_in) // Ganti dengan ID destinasi Fragment Anda
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