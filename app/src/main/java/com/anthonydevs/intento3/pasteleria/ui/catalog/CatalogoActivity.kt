package com.anthonydevs.intento3.pasteleria.ui.catalog

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import com.anthonydevs.intento3.pasteleria.R
import com.anthonydevs.intento3.pasteleria.data.model.Producto
import com.anthonydevs.intento3.pasteleria.data.model.CarritoItem
import com.anthonydevs.intento3.pasteleria.databinding.ActivityCatalogoBinding
import com.anthonydevs.intento3.pasteleria.ui.account.CuentaActivity
import com.anthonydevs.intento3.pasteleria.ui.cart.CarritoActivity
import android.widget.Toast
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class CatalogoActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCatalogoBinding
    private lateinit var catalogoAdapter: CatalogoAdapter
    private var listaProductos = mutableListOf<Producto>()
    private var listaFiltrada = mutableListOf<Producto>()

    companion object {
        val carritoItems = mutableListOf<CarritoItem>()

        fun agregarAlCarritoConCantidad(producto: Producto, cantidad: Int) {
            val itemExistente = carritoItems.find { it.producto.id == producto.id }

            if (itemExistente != null) {
                itemExistente.cantidad += cantidad
            } else {
                carritoItems.add(CarritoItem(producto, cantidad))
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()

        binding = ActivityCatalogoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // 🔥 FIX COMPLETO PARA ANDROID 14–16 (NO TAPA NADA)
        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { view, insets ->
            val status = insets.getInsets(WindowInsetsCompat.Type.statusBars())
            val nav = insets.getInsets(WindowInsetsCompat.Type.navigationBars())

            view.setPadding(
                view.paddingLeft,
                status.top,     // espacio arriba
                view.paddingRight,
                nav.bottom      // espacio abajo
            )

            insets
        }

        setupRecyclerView()
        setupBottomNavigation()
        setupSearch()
        setupClickListeners()
        loadProducts()
    }

    private fun setupRecyclerView() {
        catalogoAdapter = CatalogoAdapter(listaFiltrada) { producto ->
            agregarAlCarrito(producto)
        }
        binding.recyclerCatalogo.apply {
            layoutManager = GridLayoutManager(this@CatalogoActivity, 2)
            adapter = catalogoAdapter
        }
    }

    private fun setupBottomNavigation() {
        binding.bottomNav.selectedItemId = R.id.navigation_home

        binding.bottomNav.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.navigation_home -> true
                R.id.navigation_carrito -> {
                    startActivity(Intent(this, CarritoActivity::class.java))
                    true
                }
                R.id.navigation_cuenta -> {
                    startActivity(Intent(this, CuentaActivity::class.java))
                    true
                }
                else -> false
            }
        }
    }

    private fun setupClickListeners() {
        binding.profileImage.setOnClickListener {
            startActivity(Intent(this, CuentaActivity::class.java))
        }

        binding.cartIcon.setOnClickListener {
            startActivity(Intent(this, CarritoActivity::class.java))
        }
    }

    private fun setupSearch() {
        binding.etBuscarProducto.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                filtrarProductos(s.toString())
            }
            override fun afterTextChanged(s: Editable?) {}
        })

        binding.btnBuscar.setOnClickListener {
            filtrarProductos(binding.etBuscarProducto.text.toString())
        }
    }

    private fun filtrarProductos(query: String) {
        listaFiltrada.clear()
        if (query.isEmpty()) {
            listaFiltrada.addAll(listaProductos)
        } else {
            listaProductos.forEach { producto ->
                if (producto.nombre.contains(query, ignoreCase = true)) {
                    listaFiltrada.add(producto)
                }
            }
        }
        catalogoAdapter.notifyDataSetChanged()
    }

    private fun loadProducts() {
        listaProductos = mutableListOf(
            Producto("1", "Ramo de rosas x6", "Ramo personalizable a tu gusto.hazlo saber en nuestro whatsapp", 40000.0),
            Producto("2", "Adorno primaveral grande", "Para un gran amor, un gran ramo. Ramo personalizable y muy vendido.", 45000.0),
            Producto("3", "Flores rosadas de temporada", "Flores para un momento especial, o para una especial; el momento es AHORA.", 38000.0),
            Producto("4", "Ramo colorido", "Ideal para una persona que le da color a tu vida, este ramo sacara mas de una sonrisa.", 42000.0),
            Producto("5", "Rosas y Lilium", "Definitvamente nuestro arreglo mas querido y pedido por todos.", 35000.0),
            Producto("6", "Ramo primaveral", "Un ramo especial que no necesita presentacion, un clasico de nuestras sedes.", 37000.0)
        )

        listaFiltrada.clear()
        listaFiltrada.addAll(listaProductos)
        catalogoAdapter.notifyDataSetChanged()
    }

    private fun agregarAlCarrito(producto: Producto) {
        val itemExistente = carritoItems.find { it.producto.id == producto.id }

        if (itemExistente != null) {
            itemExistente.cantidad++
            Toast.makeText(this, "${producto.nombre} agregado al carrito (Cantidad: ${itemExistente.cantidad})", Toast.LENGTH_SHORT).show()
        } else {
            carritoItems.add(CarritoItem(producto, 1))
            Toast.makeText(this, "${producto.nombre} agregado al carrito", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onResume() {
        super.onResume()
        binding.bottomNav.selectedItemId = R.id.navigation_home
    }
}
