package com.example.firestorervtemplate

import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.bumptech.glide.Glide

class PropertyDetail : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_property_detail)

        // Retrieve property details from Intent extras
        val propertyImage = intent.getStringExtra("propertyImage")
        val numberOfBedrooms = intent.getIntExtra("numberOfBedrooms", 0)
        val propertyAddress = intent.getStringExtra("propertyAddress")
        val rental = intent.getIntExtra("rental", 0)
        val rentalType = intent.getStringExtra("rentalType")

        // Populate UI elements with property details
        findViewById<ImageView>(R.id.property_image)?.let {
            Glide.with(this)
                .load(propertyImage)
                .into(it)
        }

        findViewById<TextView>(R.id.property_address)?.text = propertyAddress
        findViewById<TextView>(R.id.property_bedrooms)?.text = "Bedrooms: $numberOfBedrooms"
        findViewById<TextView>(R.id.property_rental)?.text = "Rental: $rental"
        findViewById<TextView>(R.id.property_type)?.text = "Rental Type: $rentalType"
        // You can similarly populate other UI elements with property details
    }
}