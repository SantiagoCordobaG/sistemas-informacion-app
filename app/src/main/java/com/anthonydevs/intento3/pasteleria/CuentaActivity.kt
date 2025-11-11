package com.anthonydevs.intento3.pasteleria

import android.content.Intent
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth

class CuentaActivity : AppCompatActivity() {

    private lateinit var etNombre: EditText
    private lateinit var etDireccion: EditText
    private lateinit var etPassword: EditText
    private lateinit var btnEditarPerfil: Button
    private lateinit var btnCerrarSesion: Button
    private lateinit var tvEmailUsuario: TextView
    private lateinit var btnBack: ImageView

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cuenta)

        auth = FirebaseAuth.getInstance()

        // Vincular vistas
        etNombre = findViewById(R.id.etNombre)
        etDireccion = findViewById(R.id.etDireccion)
        etPassword = findViewById(R.id.etPassword)
        btnEditarPerfil = findViewById(R.id.btnEditarPerfil)
        btnCerrarSesion = findViewById(R.id.btnCerrarSesion)
        tvEmailUsuario = findViewById(R.id.tvEmailUsuario)
        btnBack = findViewById(R.id.btnBack)

        // Mostrar email del usuario
        tvEmailUsuario.text = auth.currentUser?.email ?: "Sin correo"

        // Botón atrás
        btnBack.setOnClickListener {
            finish()
        }

        // Editar perfil (simulado)
        btnEditarPerfil.setOnClickListener {
            Toast.makeText(this, "Cambios guardados (simulado)", Toast.LENGTH_SHORT).show()
        }

        // Cerrar sesión y volver al login
        btnCerrarSesion.setOnClickListener {
            auth.signOut()
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }
    }
}
