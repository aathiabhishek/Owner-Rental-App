package com.example.myproject

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myproject.adapter.MyAdapter
import com.example.myproject.databinding.ActivityViewListingScreenBinding
import com.example.myproject.models.LandlordProfiles
import com.google.firebase.firestore.FirebaseFirestore


class ViewListingScreen : AppCompatActivity() {

    private lateinit var binding: ActivityViewListingScreenBinding
    private lateinit var adapter: MyAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityViewListingScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val updateRowButtonClicked: (String) -> Unit = { docPropertyAddress ->
            val intent = Intent(this, Property::class.java)
            intent.putExtra("KEY_DOCUMENT_ID", docPropertyAddress)
            startActivity(intent)
        }
        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        adapter = MyAdapter(emptyList(),updateRowButtonClicked,{ details -> addToWatchlist(details) })
        binding.recyclerView.adapter = adapter
        loadPropertyListings()
    }

    private fun addToWatchlist(details: LandlordProfiles) {
        val db = FirebaseFirestore.getInstance()
        val watchlistCollection = db.collection("Landlord")
        watchlistCollection.add(details)
            .addOnSuccessListener { documentReference ->
                Log.d("MainActivity", "Property added to watchlist with ID: ${documentReference.id}")

            }
            .addOnFailureListener { e ->
                Log.w("MainActivity", "Error adding property to watchlist", e)

            }
        Log.d("MainActivity", "Added to watchlist: ${details.Propertyaddress}")
    }


    private fun loadPropertyListings() {
        val db = FirebaseFirestore.getInstance()

        db.collection("Landlord")
            .get()
            .addOnSuccessListener { documents ->
                val listings = mutableListOf<LandlordProfiles>()
                for (document in documents) {
                    val imageURL = document.getString("PropertyImage")?: ""
                    val bedrooms = document.getLong("Numberofbedrooms")?.toInt() ?:0
                    val address = document.getString("Propertyaddress")?: ""
                    val price = document.getDouble("Rental")?.toInt() ?:0
                    val rentalType = document.getString("Rentaltype")?: ""
                    val isAvailable = document.getBoolean("isAvailable") ?: true

                    val listing = LandlordProfiles(imageURL, bedrooms,
                        address, price, rentalType,isAvailable)
                    listings.add(listing)
                }
                adapter.setData(listings)
                adapter.yourListData = listings
//                adapter.notifyDataSetChanged()
            }
            .addOnFailureListener { exception ->
                Log.e("ViewListingScreen", "Error getting property listings", exception)
            }
    }
}
