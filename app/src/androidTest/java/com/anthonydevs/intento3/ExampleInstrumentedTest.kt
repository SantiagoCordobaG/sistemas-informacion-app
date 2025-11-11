package com.anthonydevs.intento3.pasteleria

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException

class LoginActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var etEmail: EditText
    private lateinit var etPassword: EditText
    private lateinit var btnLogin: Button
    // No he visto un TextView para errores en tu XML, así que lo manejaremos con Toasts.
    // Si tienes uno, puedes añadir la variable aquí.

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Usamos tu layout principal que contiene los campos de login.
        setContentView(R.layout.activity_main)

        // ✅ CORRECCIÓN: Inicializamos las vistas con los IDs CORRECTOS de tu XML.
        etEmail = findViewById(R.id.etUsuario)
        etPassword = findViewById(R.id.etContrasena)
        btnLogin = findViewById(R.id.btnLogin)

        // Inicializamos Firebase
        auth = FirebaseAuth.getInstance()

        // Listener para el botón de inicio de sesión
        btnLogin.setOnClickListener {
            val email = etEmail.text.toString().trim()
            val password = etPassword.text.toString().trim()

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Por favor, llena todos los campos", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Toast.makeText(this, "Inicio de sesión exitoso", Toast.LENGTH_SHORT).show()

                        // Navegamos a la siguiente actividad (ajusta 'CatalogoActivity::class.java' si es diferente)
                        val intent = Intent(this, CatalogoActivity::class.java)
                        startActivity(intent)
                        finish() // Cerramos esta actividad para que el usuario no pueda volver con el botón de atrás.
                    } else {
                        // Manejo de errores más específico para el usuario.
                        val errorMsg = when (task.exception) {
                            is FirebaseAuthInvalidUserException -> "El correo no se encuentra registrado."
                            is FirebaseAuthInvalidCredentialsException -> "La contraseña es incorrecta."
                            else -> "Error de autenticación. Inténtalo de nuevo."
                        }
                        Toast.makeText(this, errorMsg, Toast.LENGTH_LONG).show()
                    }
                }
        }
    }

    override fun onStart() {
        super.onStart()
        // Si el usuario ya inició sesión previamente, lo mandamos directo al catálogo.
        if (auth.currentUser != null) {
            startActivity(Intent(this, CatalogoActivity::class.java))
            finish()
        }
    }
}
