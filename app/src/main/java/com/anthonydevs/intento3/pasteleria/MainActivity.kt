package com.anthonydevs.intento3.pasteleria

import android.content.Intent
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException

class MainActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var etEmail: EditText
    private lateinit var etPassword: EditText
    private lateinit var btnLogin: Button
    private lateinit var tvRegisterLink: TextView // ✅ Enlace de registro

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        auth = FirebaseAuth.getInstance()

        // ✅ Vinculamos vistas
        etEmail = findViewById(R.id.etUsuario)
        etPassword = findViewById(R.id.etContrasena)
        btnLogin = findViewById(R.id.btnLogin)
        tvRegisterLink = findViewById(R.id.tvRegisterLink)

        // --- Iniciar sesión ---
        btnLogin.setOnClickListener {
            val email = etEmail.text.toString().trim()
            val password = etPassword.text.toString().trim()

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Por favor, completa todos los campos", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Toast.makeText(this, "Inicio de sesión exitoso 🌸", Toast.LENGTH_SHORT).show()
                        startActivity(Intent(this, CatalogoActivity::class.java))
                        finish()
                    } else {
                        val msg = when (task.exception) {
                            is FirebaseAuthInvalidUserException -> "El correo no está registrado."
                            is FirebaseAuthInvalidCredentialsException -> "Contraseña incorrecta."
                            else -> "Error al iniciar sesión. Inténtalo de nuevo."
                        }
                        Toast.makeText(this, msg, Toast.LENGTH_LONG).show()
                    }
                }
        }

        // --- Enlace a registro ---
        tvRegisterLink.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
        }
    }

    override fun onStart() {
        super.onStart()
        if (auth.currentUser != null) {
            startActivity(Intent(this, CatalogoActivity::class.java))
            finish()
        }
    }
}
