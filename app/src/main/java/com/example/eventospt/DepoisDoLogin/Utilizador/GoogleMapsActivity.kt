package com.example.eventospt.DepoisDoLogin.Utilizador

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.eventospt.R
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import android.location.Geocoder
import android.location.Address
import androidx.activity.enableEdgeToEdge

class GoogleMapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private lateinit var searchBox: EditText
    private lateinit var btnSearch: TextView
    private lateinit var btnVoltar: ImageView
    private lateinit var btnZoomIn: ImageView
    private lateinit var btnZoomOut: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dlu_google_maps)
        enableEdgeToEdge()

        searchBox = findViewById(R.id.searchBox)
        btnSearch = findViewById(R.id.textView17)
        btnVoltar = findViewById(R.id.btnVoltar)
        btnZoomIn = findViewById(R.id.btnZoomIn)
        btnZoomOut = findViewById(R.id.btnZoomOut)

        val mapFragment = supportFragmentManager.findFragmentById(R.id.mapFragment) as SupportMapFragment
        mapFragment.getMapAsync(this)

        btnSearch.setOnClickListener {
            val destination = searchBox.text.toString()

            if (destination.isNotEmpty()) {
                if (isValidInput(destination)) {
                    searchForDestination(destination)
                } else {
                    Toast.makeText(this, "Destino inválido! Use apenas letras e números.", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(this, "Por favor, insira um destino", Toast.LENGTH_SHORT).show()
            }
        }

        btnVoltar.setOnClickListener {
            val intent = Intent(this, Inicio::class.java)
            startActivity(intent)
            finish()
        }

        btnZoomIn.setOnClickListener {
            mMap.animateCamera(CameraUpdateFactory.zoomIn())
        }

        btnZoomOut.setOnClickListener {
            mMap.animateCamera(CameraUpdateFactory.zoomOut())
        }
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(LatLng(38.7169, -9.1399), 5f)) // Usando coordenadas padrão para uma visão geral
    }

    private fun isValidInput(input: String): Boolean {
        val regex = "^[a-zA-Z0-9, ~]{1,50}$".toRegex()
        return input.matches(regex)
    }

    private fun searchForDestination(destination: String) {
        val destinationLatLng = getLatLngFromAddress(destination)

        if (destinationLatLng != null) {
            mMap.clear()
            mMap.addMarker(MarkerOptions().position(destinationLatLng).title("Destino: $destination"))
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(destinationLatLng, 15f))
        } else {
            Toast.makeText(this, "Destino não encontrado!", Toast.LENGTH_SHORT).show()
        }
    }

    private fun getLatLngFromAddress(address: String): LatLng? {
        val geocoder = Geocoder(this)
        val addresses = geocoder.getFromLocationName(address, 1)

        return if (addresses != null && addresses.isNotEmpty()) {
            val location = addresses[0]
            LatLng(location.latitude, location.longitude)
        } else {
            null
        }
    }
}
