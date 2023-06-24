package com.appbygox.rcapp.data.model

data class Users constructor(
    var email: String? = "",
    var password: String = "",
    var nama: String? = "",
    var role: String? = "",
    var idUser: String = ""
)
