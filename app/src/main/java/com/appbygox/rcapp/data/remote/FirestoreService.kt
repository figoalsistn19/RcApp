package com.appbygox.rcapp.data.remote

import android.content.Intent
import android.widget.Toast
import androidx.core.view.isVisible
import com.appbygox.rcapp.MainActivity
import com.appbygox.rcapp.data.LoginPref
import com.appbygox.rcapp.data.model.InventoryIn
import com.appbygox.rcapp.data.model.InventoryOut
import com.appbygox.rcapp.data.model.Item
import com.appbygox.rcapp.data.model.Stock
import com.appbygox.rcapp.orZero
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class FirestoreService {
    private val db = Firebase.firestore

    fun login(email: String, password: String) =
        db.collection("User")
            .whereEqualTo("email", email)
            .whereEqualTo("password", password)

    fun addItem(item: Item, success: (Boolean) -> Unit) {
        db.collection("Item")
            .add(item)
            .onSuccessTask { doc ->
                doc.update("idItem", doc.id)
                    .addOnSuccessListener { success(true) }
                    .addOnFailureListener { success(false) }
            }
    }

    fun addInventoryIn(inventoryIn: InventoryIn, success: (Boolean) -> Unit) {
        db.collection("InventoryIn")
            .add(inventoryIn)
            .onSuccessTask { doc ->
                doc.update("idInventoryIn", doc.id)
                    .addOnSuccessListener { success(true) }
                    .addOnFailureListener { success(false) }
            }
    }

    fun addStock(inventoryIn: InventoryIn, success: (Boolean) -> Unit) {
        db.collection("Stock")
            .document(inventoryIn.idItem.orEmpty())
            .set(
                Stock(
                    idItem = inventoryIn.idItem,
                    namaItem = inventoryIn.namaItem,
                    namaSupplier = inventoryIn.namaSupplier,
                    jumlahItem = inventoryIn.jumlahItem,
                    tipeQuantity = inventoryIn.tipeQuantity,
                    updateAt = inventoryIn.createAt
                )
            )
            .addOnSuccessListener { success(true) }
            .addOnFailureListener { success(false) }
    }

    fun updateStock(idItem: String, jumlahExisting: Long,  jumlahItem: Long, isFromIn: Boolean, success: (Boolean) -> Unit){
        if(isFromIn){
            db.collection("Stock").document(idItem)
                .update("jumlahItem", jumlahExisting+jumlahItem)
                .addOnSuccessListener { success(true) }
                .addOnFailureListener { success(false) }
        } else {
            db.collection("Stock").document(idItem)
                .update("jumlahItem", jumlahExisting-jumlahItem)
                .addOnSuccessListener { success(true) }
                .addOnFailureListener { success(false) }
        }
    }

    fun addInventoryOut(inventoryOut: InventoryOut, success: (Boolean) -> Unit) {
        db.collection("InventoryOut")
            .add(inventoryOut)
            .onSuccessTask { doc ->
                doc.update("idInventoryOut", doc.id)
                    .addOnSuccessListener { success(true) }
                    .addOnFailureListener { success(false) }
            }
    }

    fun getStock(idItem: String, stock: (Long) -> Unit) {
        db.collection("Stock")
            .document(idItem)
            .get()
            .addOnSuccessListener {
                stock(it.getLong("jumlahItem").orZero())
            }
    }

    fun getInventoryInNewest(): Query =
        db.collection("InventoryIn")
            .orderBy("createAt")

    fun getInventoryOutNewest(): Query =
        db.collection("InventoryOut")
            .orderBy("createAt")

    fun getStockNewest(): Query =
        db.collection("Stock")
            .orderBy("updateAt")

    fun getItems(): Query =
        db.collection("Item")
            .orderBy("namaItem")

    fun checkStock(idItem: String, isStockFirstTime: (Boolean) -> Unit) {
        db.collection("Stock")
            .document(idItem)
            .get()
            .addOnSuccessListener { document ->
                if (document != null && document.exists()) {
                    isStockFirstTime(false)
                } else {
                    isStockFirstTime(true)
                }
            }
    }



}