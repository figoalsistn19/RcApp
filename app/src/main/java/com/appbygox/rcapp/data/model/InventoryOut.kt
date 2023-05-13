package com.appbygox.rcapp.data.model

data class InventoryOut constructor(
    var idInventoryOut: String? = "",
    var idItem: String? = "",
    var namaItem: String? = "",
    var namaToko: String? = "",
    var jumlahItem: Long? = 0,
    var tipeQuantity: String? = "",
    var returnItem: String? = "",
    var namaPengirim: String? = "",
    var platMobilPengirim: String? = "",
    var noNota: String? = "",
    var keterangan: String? = "",
    var createAt: Long? = 0L,
    var createBy: String? = ""
)