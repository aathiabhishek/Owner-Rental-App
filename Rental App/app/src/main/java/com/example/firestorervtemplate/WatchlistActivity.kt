package com.example.firestorervtemplate

import WatchlistAdapter
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.firestorervtemplate.databinding.ActivityWatchlistBinding
import com.example.firestorervtemplate.models.Details
import com.google.firebase.firestore.FirebaseFirestore

class WatchlistActivity : AppCompatActivity() {

    private lateinit var binding: ActivityWatchlistBinding
    private lateinit var adapter: WatchlistAdapter

    private lateinit var db: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWatchlistBinding.inflate(layoutInflater)
        setContentView(binding.root)

        db = FirebaseFirestore.getInstance()
        // Set up RecyclerView
        adapter = WatchlistAdapter()
        binding.watchlistRecyclerView.layoutManager = LinearLayoutManager(this)
        binding.watchlistRecyclerView.adapter = adapter

        // Load watchlist data
        loadData()
    }

    private fun loadData() {
        // Fetch watchlist data from somewhere (e.g., Firebase Firestore)
        // Replace this with your actual implementation to fetch watchlist data



        db.collection("Watchlist")
            .get()
            .addOnSuccessListener { result ->
                val detailsList = mutableListOf<Details>()
                for (document in result) {
                    val propertyImage = document.getString("propertyImage") ?: ""
                    val numberOfBedrooms = document.getLong("numberofbedrooms")?.toInt() ?: 0
                    val propertyAddress = document.getString("propertyaddress") ?: ""
                    val rental = document.getLong("rental")?.toInt() ?: 0
                    val rentalType = document.getString("rentaltype") ?: ""
                    val details = Details(propertyImage, numberOfBedrooms, propertyAddress, rental, rentalType)
                    detailsList.add(details)
                }
                adapter.setData(detailsList)
            }
            .addOnFailureListener { exception ->
                Log.e("MainActivity", "Error getting documents: ", exception)
            }




        //  val watchlistData = mutableListOf<Details>() // Replace this with your actual watchlist data
        // adapter.setData(watchlistData)
    }
}