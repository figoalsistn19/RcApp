package com.appbygox.rcapp.data.model

data class Stock constructor(
    var idItem: String? = "",
    var namaItem: String? = "",
    var namaSupplier: String? = "",
    var jumlahItem: Long? = 0,
    var tipeQuantity: String? = "",
    var updateAt: Long? = 0L
)