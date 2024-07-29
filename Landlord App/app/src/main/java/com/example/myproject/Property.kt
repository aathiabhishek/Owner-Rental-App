package com.example.myproject

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.myproject.databinding.ActivityPropertyBinding
import com.example.myproject.models.LandlordProfiles
import com.google.firebase.firestore.FirebaseFirestore


class Property : AppCompatActivity() {

    private lateinit var binding: ActivityPropertyBinding
    private var property: LandlordProfiles? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPropertyBinding.inflate(layoutInflater)
        setContentView(binding.root)
        property = intent.getParcelableExtra<LandlordProfiles>("Property")

        if (property == null) {
            Toast.makeText(this, "Property details not found in intent", Toast.LENGTH_SHORT).show()
            finish()
            return
        }
        loadPropertyDetails()

        binding.deleteButton.setOnClickListener {
            deleteProperty()
        }
        binding.button2.setOnClickListener {
            updateProperty()
        }
    }

    private fun updateProperty() {
        val updatedPriceStr = binding.priceEditText.text.toString()
        val updatedRental = binding.rentalTypeEditText.text.toString()

        if (updatedPriceStr.isBlank() || updatedRental.isBlank()) {
            Toast.makeText(this, "Fill the form with valid values", Toast.LENGTH_SHORT).show()
            return
        }

        val updatedPrice = updatedPriceStr.toIntOrNull()
        if (updatedPrice == null) {
            Toast.makeText(this, "Invalid price value", Toast.LENGTH_SHORT).show()
            return
        }

        val property = property
        if (property != null) {
            val db = FirebaseFirestore.getInstance()
            val propertyAddress = property.Propertyaddress

            val updatedProperty = property.copy(Rental = updatedPrice, Rentaltype = updatedRental)

            db.collection("Landlord")
                .document(propertyAddress)
                .set(updatedProperty)
                .addOnSuccessListener {
                    Toast.makeText(this, "Property updated successfully", Toast.LENGTH_SHORT).show()
                    loadPropertyDetails() // Reload property details after update
                }
                .addOnFailureListener { exception ->
                    Toast.makeText(this, "Error updating property: ${exception.message}", Toast.LENGTH_SHORT).show()
                    // Log the error for debugging
                    Log.e("UpdateProperty", "Error updating property", exception)
                }
        } else {
            Toast.makeText(this, "Property is null", Toast.LENGTH_SHORT).show()
        }
    }

    private fun deleteProperty() {
        val property = property
        if (property != null) {
            val db = FirebaseFirestore.getInstance()
            val propertyAddress = property.Propertyaddress

            db.collection("Landlord")
                .document(propertyAddress)
                .delete()
                .addOnSuccessListener {
                    Toast.makeText(this, "Property deleted successfully", Toast.LENGTH_SHORT).show()
                    finish() // Finish activity after deletion
                }
                .addOnFailureListener { exception ->
                    Toast.makeText(this, "Error deleting property: ${exception.message}", Toast.LENGTH_SHORT).show()
                    // Log the error for debugging
                    Log.e("DeleteProperty", "Error deleting property", exception)
                }
        } else {
            Toast.makeText(this, "Property is null", Toast.LENGTH_SHORT).show()
        }
    }

    private fun loadPropertyDetails() {
        binding.apply {
            priceEditText.setText(property?.Rental.toString())
            rentalTypeEditText.setText(property?.Rentaltype)
        }
    }
}

