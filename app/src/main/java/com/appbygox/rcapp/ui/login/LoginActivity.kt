package com.appbygox.rcapp.ui.login

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.core.view.isVisible
import com.appbygox.rcapp.MainActivity
import com.appbygox.rcapp.R
import com.appbygox.rcapp.data.LoginPref
import com.appbygox.rcapp.data.remote.FirestoreService
import com.appbygox.rcapp.databinding.ActivityLoginBinding
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber
import javax.inject.Inject

@AndroidEntryPoint
class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding

    @Inject
    lateinit var service: FirestoreService
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()

        val loginPref = LoginPref(this)
        val isLogin = loginPref.getSession()

        if (isLogin) {
            val i = Intent(this, MainActivity::class.java)
            i.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
            startActivity(i)
        }

        binding.btnLogin.setOnClickListener { login() }

    }
    private fun login(){
        binding.progressBar.isVisible = true
        val email = binding.editEmail.text.toString()
        val password = binding.editPassword.text.toString()

        when {
            email.isEmpty() -> {
                binding.editEmail.error = "Masukkan username"
            }
            password.isEmpty() -> {
                binding.editPassword.error = "Masukkan password"
            }
            else -> {
                service.login(email, password)
                    .addOnCompleteListener {
                        if (it.isSuccessful){
                            var nama: String = email
                            LoginPref(this).apply {
                                setSession(true)
                                setNamaUser(nama)
                            }
                            binding.progressBar.isVisible = false
                            Toast.makeText(
                                this,
                                "Login berhasil",
                                Toast.LENGTH_LONG
                            ).show()

                            val mainActivityIntent = Intent(this, MainActivity::class.java)
                            mainActivityIntent.putExtra("selected_fragment", R.id.navigation_stock)
                            mainActivityIntent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                            startActivity(mainActivityIntent)
                        }
                        else {
                            Toast.makeText(this,it.exception.toString(),Toast.LENGTH_SHORT).show()
                        }
                    }
//                    .addSnapshotListener { value, e ->
//                        if (e != null) {
//                            Timber.d("Listen failed.")
//                            binding.progressBar.isVisible = false
//                            return@addSnapshotListener
//                        }
//                        var id_user : String? = null
//                        var nama : String? = null
//                        for (doc in value!!) {
//                            id_user = doc.getString("id_user")
//                            nama = doc.getString("nama")
//                        }
//                        if (id_user != null && nama != null) {
//                            LoginPref(this).apply {
//                                setSession(true)
//                                setIdUser(id_user)
//                                setNamaUser(nama)
//                            }
//                            binding.progressBar.isVisible = false
//                            Toast.makeText(
//                                this,
//                                "Login berhasil",
//                                Toast.LENGTH_LONG
//                            ).show()
//                            val mainActivityIntent = Intent(this, MainActivity::class.java)
//                            mainActivityIntent.putExtra("selected_fragment", R.id.navigation_stock)
//                            mainActivityIntent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
//                            startActivity(mainActivityIntent)
//                        } else {
//                            binding.progressBar.isVisible = false
//                            Toast.makeText(
//                                this,
//                                "Email atau password salah",
//                                Toast.LENGTH_LONG
//                            ).show()
//                        }
//                    }
            }
        }
    }
}