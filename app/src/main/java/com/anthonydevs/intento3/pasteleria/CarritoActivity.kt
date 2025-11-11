package com.anthonydevs.intento3.pasteleria

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ListView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class CarritoActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var db: FirebaseFirestore
    private lateinit var listViewCarrito: ListView
    private lateinit var tvTotal: TextView
    private lateinit var btnPagar: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_carrito)

        auth = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()

        listViewCarrito = findViewById(R.id.listViewCarrito)
        tvTotal = findViewById(R.id.tvTotal)
        btnPagar = findViewById(R.id.btnPagar)

        cargarCarrito()

        btnPagar.setOnClickListener {
            // Aquí puedes redirigir al pago o generar orden
            val intent = Intent(this, PagoActivity::class.java)
            startActivity(intent)
        }
    }

    private fun cargarCarrito() {
        val userId = auth.currentUser?.uid ?: return

        // Obtener productos del carrito desde Firestore
        db.collection("carritos")
            .document(userId)
            .collection("productos")
            .get()
            .addOnSuccessListener { documentos ->
                val listaProductos = mutableListOf<String>()
                var total = 0.0
                for (doc in documentos) {
                    val nombre = doc.getString("nombre") ?: ""
                    val precio = doc.getDouble("precio") ?: 0.0
                    listaProductos.add("$nombre - $precio$")
                    total += precio
                }

                val adapter = android.widget.ArrayAdapter(
                    this,
                    android.R.layout.simple_list_item_1,
                    listaProductos
                )
                listViewCarrito.adapter = adapter
                tvTotal.text = "Total: $${"%.2f".format(total)}"
            }
            .addOnFailureListener {
                tvTotal.text = "Error al cargar carrito"
            }
    }
}
