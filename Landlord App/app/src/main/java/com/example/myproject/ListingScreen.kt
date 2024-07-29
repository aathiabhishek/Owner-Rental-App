package com.example.myproject


import android.content.Intent
import android.location.Address
import android.location.Geocoder
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.myproject.databinding.ActivityListingScreenBinding
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.util.Locale

class ListingScreen : AppCompatActivity() {

    private lateinit var binding: ActivityListingScreenBinding
    lateinit var geocoder:Geocoder
    private val TAG:String = "TESTING"
    private var Latitude: Double = 0.0
    private var Longitude: Double = 0.0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityListingScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)

        geocoder = Geocoder(this, Locale.getDefault())

        binding.useCurrentLocationButton.setOnClickListener {
            getCurrentLocation()
        }
        binding.submitButton.setOnClickListener {
            getCoordinates()
            submitListing()

        }
        binding.nextButton.setOnClickListener{
            nextPage()
        }
    }
    private fun nextPage(){
        val intent = Intent(this, ViewListingScreen::class.java).apply {
        }
        startActivity(intent)
    }

    private fun getCurrentLocation() {
        val dummyAddress = "Eglinton Avenue East"
        binding.addressEditText.setText(dummyAddress)
    }
    private fun getCoordinates() {
        val addressFromUI = binding.addressEditText.text.toString()
        Log.d(TAG, "Getting coordinates for ${addressFromUI}")

        try {
            val searchResults:MutableList<Address>? =
                geocoder.getFromLocationName(addressFromUI, 1)

            if (searchResults == null) {
                Toast.makeText(this, "ERROR: Results is null", Toast.LENGTH_SHORT).show()
                return
            }
            if (searchResults.isEmpty() == true) {
                Toast.makeText(this, "No matching coordinates found.", Toast.LENGTH_SHORT).show()
                return
            }
            val matchingItem: Address = searchResults.get(0)
             Latitude = matchingItem.latitude
             Longitude = matchingItem.longitude
            //Toast.makeText(this, "Coordinate found: (${matchingItem.latitude}, ${matchingItem.longitude})", Toast.LENGTH_SHORT).show()
        } catch(ex:Exception) {
            Log.e(TAG, "Error encountered while getting coordinates")
            Log.e(TAG, ex.toString())
        }
    }


    private fun submitListing() {
        val address = binding.addressEditText.text.toString()
        val propertyImage = binding.imageURLEditText.text.toString()
        val rental = binding.priceEditText.text.toString().toInt()
        val bedrooms = binding.bedroomsEditText.text.toString().toIntOrNull() ?: 0
        val rentalType = binding.rentalEditText.text.toString()
        val data: MutableMap<String, Any> = HashMap();
        data["Propertyaddress"] = address
        data["PropertyImage"] = propertyImage
        data["Rental"] = rental
        data["Numberofbedrooms"] = bedrooms
        data["Rentaltype"] = rentalType
        data["Latitude"]=Latitude
        data["Longitude"]=Longitude

        val db = Firebase.firestore
        db.collection("Landlord")
            .add(data)
            .addOnSuccessListener {
                Toast.makeText(this, "Listing submitted successfully!", Toast.LENGTH_SHORT).show()
                val intent = Intent(this, ViewListingScreen::class.java).apply {
                }
                startActivity(intent)
            }
            .addOnFailureListener { e ->
                Toast.makeText(this, "Error submitting listing: ${e.message}", Toast.LENGTH_SHORT)
                    .show()
            }
    }
}
