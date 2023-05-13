package com.appbygox.rcapp.data.model

data class User constructor(
    var email: String? = "",
    var password: String = "",
    var nama: String? = "",
    var role: String? = "",
    var id_user: String = ""
)
