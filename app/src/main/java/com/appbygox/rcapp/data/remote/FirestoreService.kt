package com.appbygox.rcapp.data.remote

import com.appbygox.rcapp.data.model.InventoryIn
import com.appbygox.rcapp.data.model.InventoryOut
import com.appbygox.rcapp.data.model.Item
import com.appbygox.rcapp.orZero
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class FirestoreService {
    private val db = Firebase.firestore

    fun login(email: String, password : String) =
        db.collection("User")
            .whereEqualTo("email", email)
            .whereEqualTo("password", password)

    fun addItem(item: Item, success: (Boolean) -> Unit) {
        db.collection("Item")
            .add(item)
            .onSuccessTask { doc ->
                doc.update("idItem", doc.id)
                    .addOnSuccessListener {
                        success(true)
                    }
            }
    }

    fun addInventoryIn(inventoryIn: InventoryIn, stockItem: Long, success: (Boolean) -> Unit) {
        db.collection("InventoryIn")
            .add(inventoryIn)
            .onSuccessTask { doc ->
                doc.update("idInventoryIn", doc.id)
                with( db.collection("Stock").document(inventoryIn.idItem.orEmpty())){
                    update("idItem", inventoryIn.idItem)
                    update("namaItem", inventoryIn.namaItem)
                    update("namaSupplier", inventoryIn.namaSupplier)
                    update("jumlahItem", stockItem+inventoryIn.jumlahItem.orZero())
                    update("tipeQuantity", inventoryIn.tipeQuantity)
                    update("updateAt", inventoryIn.createAt)
                }
                    .addOnSuccessListener {
                        success(true)
                    }
            }
    }
    
    fun addInventoryOut(inventoryOut: InventoryOut, stockItem: Long, success: (Boolean) -> Unit) {
        db.collection("InventoryOut")
            .add(inventoryOut)
            .onSuccessTask { doc ->
                doc.update("idInventoryOut", doc.id)
                with( db.collection("Stock").document(inventoryOut.idItem.orEmpty())){
                    update("idItem", inventoryOut.idItem)
                    update("namaItem", inventoryOut.namaItem)
                    update("jumlahItem", stockItem-inventoryOut.jumlahItem.orZero())
                    update("tipeQuantity", inventoryOut.tipeQuantity)
                    update("updateAt", inventoryOut.createAt)
                }
                    .addOnSuccessListener {
                        success(true)
                    }
            }
    }

    fun getStock(idItem: String): Long {
        var stock = 0L
        db.collection("Stock")
            .document(idItem)
            .get()
            .addOnSuccessListener {
                stock = it.getLong("jumlahItem").orZero()
            }
        return stock
    }

    fun getInventoryInNewest() : Query =
        db.collection("InventoryIn")
            .orderBy("createAt")

    fun getInventoryOutNewest() : Query =
        db.collection("InventoryOut")
            .orderBy("createAt")

    fun getStockNewest() : Query =
        db.collection("Stock")
            .orderBy("updateAt")

    fun getItems() : Query =
        db.collection("Item")
            .orderBy("namaItem")

}