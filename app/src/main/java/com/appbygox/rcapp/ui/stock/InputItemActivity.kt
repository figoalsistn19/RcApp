package com.appbygox.rcapp.ui.stock

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.appbygox.rcapp.MainActivity
import com.appbygox.rcapp.R
import com.appbygox.rcapp.data.model.Item
import com.appbygox.rcapp.data.remote.FirestoreService
import com.appbygox.rcapp.databinding.ActivityInputItemBinding
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class InputItemActivity : AppCompatActivity() {

    @Inject
    lateinit var service: FirestoreService

    private lateinit var binding: ActivityInputItemBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityInputItemBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnSimpan.setOnClickListener {
            if (binding.checkBox.isChecked) {
                saveData()
            } else {
                Toast.makeText(this@InputItemActivity, "Check terlebih dahulu", Toast.LENGTH_SHORT)
                    .show()
            }
        }
    }

    private fun saveData() {
        with(binding) {
            val namaItem = editNamaItem.text.toString()
            val tipeKuantiti = editQtyItem.text.toString()
            val namaSupplier = editNamaSupplier.text.toString()

            when{
                namaItem.isEmpty() -> {
                    editNamaItem.error = "Tidak boleh kosong"
                }
                tipeKuantiti.isEmpty() -> {
                    binding.editQtyItem.error = "Tidak boleh kosong"
                }
                namaSupplier.isEmpty() -> {
                    binding.editNamaSupplier.error = "Tidak boleh kosong"
                }
                else -> {
                    service.addItem(
                        Item(
                            namaItem = namaItem,
                            tipeQuantity = tipeKuantiti,
                            namaSupplier = namaSupplier
                        )
                    ) {
                        val i = Intent(this@InputItemActivity, MainActivity::class.java)
                        i.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                        startActivity(i)

                        Toast.makeText(
                            this@InputItemActivity,
                            "Berhasil Tambah Item",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }
            }
        }
    }
}