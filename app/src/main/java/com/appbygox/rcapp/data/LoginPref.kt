package com.appbygox.rcapp.data

import android.content.Context

class LoginPref(context : Context) {
    private val prefIsLogin = context.getSharedPreferences(PREFS_ISLOGIN, Context.MODE_PRIVATE)
    private val prefIdUser = context.getSharedPreferences(PREFS_ID_USER, Context.MODE_PRIVATE)
    private val prefNamaUser = context.getSharedPreferences(PREFS_NAMA_USER, Context.MODE_PRIVATE)
    private val prefRole = context.getSharedPreferences(PREFS_ROLE, Context.MODE_PRIVATE)

    fun setSession(isLogin: Boolean){
        val editor = prefIsLogin.edit()
        editor.putBoolean(SESSION, isLogin)
        editor.apply()
    }

    fun setIdUser(idUser: String){
        val editor = prefIdUser.edit()
        editor.putString(ID_USER, idUser)
        editor.apply()
    }

    fun setNamaUser(namaUser: String){
        val editor = prefNamaUser.edit()
        editor.putString(NAMA_USER, namaUser)
        editor.apply()
    }

    fun setRole(role: String){
        val editor = prefRole.edit()
        editor.putString(ROLE, role)
        editor.apply()
    }

    fun getSession() : Boolean{
        return prefIsLogin.getBoolean(SESSION, false)
    }

    fun getIdUser() : String? {
        return prefIdUser.getString(ID_USER, null)
    }

    fun getRole() : String? {
        return prefRole.getString(ROLE, null)
    }

    fun getNamaUser() : String? {
        return prefNamaUser.getString(NAMA_USER, null)
    }

    fun logout(){
        prefIsLogin.edit().clear().apply()
        prefIdUser.edit().clear().apply()
        prefNamaUser.edit().clear().apply()
        prefRole.edit().clear().apply()
    }

    companion object {
        private const val PREFS_ISLOGIN = "isLogin_pref"
        private const val SESSION = "session"
        private const val PREFS_ID_USER = "idUser_pref"
        private const val ID_USER = "id_user"
        private const val PREFS_NAMA_USER = "namaUser_pref"
        private const val NAMA_USER = "nama_user"
        private const val PREFS_ROLE = "role_prefs"
        private const val ROLE = "role"
    }

}