package com.anthonydevs.intento3.pasteleria

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class PagoActivity : AppCompatActivity() {

    private lateinit var etTarjeta: EditText
    private lateinit var etFecha: EditText
    private lateinit var etCVV: EditText
    private lateinit var tvTotal: TextView
    private lateinit var btnPagar: Button

    private lateinit var auth: FirebaseAuth
    private lateinit var db: FirebaseFirestore
    private var total: Double = 0.0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pago)

        auth = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()

        etTarjeta = findViewById(R.id.etTarjeta)
        etFecha = findViewById(R.id.etFecha)
        etCVV = findViewById(R.id.etCVV)
        tvTotal = findViewById(R.id.tvTotal)
        btnPagar = findViewById(R.id.btnPagar)

        cargarTotal()

        btnPagar.setOnClickListener {
            val tarjeta = etTarjeta.text.toString().trim()
            val fecha = etFecha.text.toString().trim()
            val cvv = etCVV.text.toString().trim()

            if (tarjeta.isEmpty() || fecha.isEmpty() || cvv.isEmpty()) {
                Toast.makeText(this, "Completa todos los campos", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Aquí puedes agregar integración con pasarela de pago
            Toast.makeText(this, "Pago realizado: $${"%.2f".format(total)}", Toast.LENGTH_LONG).show()

            // Limpiar carrito
            limpiarCarrito()
        }
    }

    private fun cargarTotal() {
        val userId = auth.currentUser?.uid ?: return

        db.collection("carritos")
            .document(userId)
            .collection("productos")
            .get()
            .addOnSuccessListener { documentos ->
                total = 0.0
                for (doc in documentos) {
                    val precio = doc.getDouble("precio") ?: 0.0
                    total += precio
                }
                tvTotal.text = "Total a pagar: $${"%.2f".format(total)}"
            }
            .addOnFailureListener {
                tvTotal.text = "Error al calcular total"
            }
    }

    private fun limpiarCarrito() {
        val userId = auth.currentUser?.uid ?: return

        db.collection("carritos")
            .document(userId)
            .collection("productos")
            .get()
            .addOnSuccessListener { documentos ->
                for (doc in documentos) {
                    db.collection("carritos")
                        .document(userId)
                        .collection("productos")
                        .document(doc.id)
                        .delete()
                }
                finish() // cerrar actividad después de pagar
            }
    }
}
