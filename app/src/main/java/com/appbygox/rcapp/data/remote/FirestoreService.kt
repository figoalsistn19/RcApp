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

    fun addItem(item: Item) {
        db.collection("Item")
            .add(item)
            .onSuccessTask { doc ->
                doc.update("idItem", doc.id)
            }
    }

    fun addInventoryIn(inventoryIn: InventoryIn, stockItem: Long) {
        db.collection("InventoryIn")
            .add(inventoryIn)
            .onSuccessTask { doc ->
                doc.update("idInventoryIn", doc.id)
            }
       with( db.collection("Stock").document(inventoryIn.idItem.orEmpty())){
           update("idItem", inventoryIn.idItem)
           update("namaItem", inventoryIn.namaItem)
           update("namaSupplier", inventoryIn.namaSupplier)
           update("jumlahItem", stockItem+inventoryIn.jumlahItem.orZero())
           update("tipeQuantity", inventoryIn.tipeQuantity)
           update("updateAt", inventoryIn.createAt)
       }
    }

    fun addInventoryOut(inventoryOut: InventoryOut, stockItem: Int) {
        db.collection("InventoryOut")
            .add(inventoryOut)
            .onSuccessTask { doc ->
                doc.update("idInventoryOut", doc.id)
            }
        with( db.collection("Stock").document(inventoryOut.idItem.orEmpty())){
            update("idItem", inventoryOut.idItem)
            update("namaItem", inventoryOut.namaItem)
            update("jumlahItem", stockItem-inventoryOut.jumlahItem.orZero())
            update("tipeQuantity", inventoryOut.tipeQuantity)
            update("updateAt", inventoryOut.createAt)
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

    fun getInventoryInNewest(today : Long) : Query =
        db.collection("InventoryIn")
            .whereGreaterThan("createAt", today)

    fun getInventoryInOldest(today : Long) : Query =
        db.collection("InventoryIn")
            .whereLessThan("createAt", today)

    fun getInventoryOutNewest(today : Long) : Query =
        db.collection("InventoryOut")
            .whereGreaterThan("createAt", today)

    fun getInventoryOutOldest(today : Long) : Query =
        db.collection("InventoryOut")
            .whereLessThan("createAt", today)

    fun getStockOutNewest(today : Long) : Query =
        db.collection("Stock")
            .whereGreaterThan("createAt", today)

    fun getStockOutOldest(today : Long) : Query =
        db.collection("Stock")
            .whereLessThan("createAt", today)

}