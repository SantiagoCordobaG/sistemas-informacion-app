package com.anthonydevs.intento3.pasteleria.util

import com.anthonydevs.intento3.pasteleria.R

object ImageHelper {
    /**
     * Obtiene el recurso de imagen según el ID del producto
     * 
     * INSTRUCCIONES PARA AGREGAR IMÁGENES:
     * 1. Coloca las imágenes en: app/src/main/res/drawable/
     * 2. Nombres de archivos DEBEN ser exactamente como se muestran abajo (sin espacios, usar guiones bajos)
     * 3. Formatos soportados: .png, .jpg, .jpeg, .webp
     * 4. Si la imagen no existe, se usará la imagen por defecto (imagen_fondo_login)
     * 
     * NOMBRES DE ARCHIVOS NECESARIOS:
     * - flores_ramo_rosas.png (o .jpg/.jpeg/.webp) para ID "1"
     * - flores_estacion_colorido.png para ID "2"
     * - flores_rosadas.png para ID "3"
     * - flores_multicolor_grande.png para ID "4"
     * - flores_rosas_liliums.png para ID "5"
     * - flores_primaverales.png para ID "6"
     */
    fun getImageResource(productId: String): Int {
        return when (productId) {
            "1" -> tryGetDrawable("flores_ramo_rosas") // Pastel vintage de cereza
            "2" -> tryGetDrawable("flores_estacion_colorido") // Pastel de cumpleaños azul
            "3" -> tryGetDrawable("flores_rosadas") // Pastel de Arándano
            "4" -> tryGetDrawable("flores_multicolor_grande") // Pastel fiesta colorida
            "5" -> tryGetDrawable("flores_rosas_liliums") // Pastel de Chocolate
            "6" -> tryGetDrawable("flores_primaverales") // Torta de Zanahoria
            else -> R.drawable.imagen_fondo_login // Imagen por defecto
        }
    }
    
    /**
     * Intenta obtener un recurso drawable por nombre usando reflexión
     * Si no existe, retorna la imagen por defecto
     */
    private fun tryGetDrawable(name: String): Int {
        return try {
            val field = R.drawable::class.java.getField(name)
            field.getInt(null)
        } catch (e: Exception) {
            // Si el recurso no existe, usar imagen por defecto
            R.drawable.imagen_fondo_login
        }
    }
}

