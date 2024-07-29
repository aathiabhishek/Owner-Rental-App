package com.example.firestorervtemplate

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.SearchView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.firestorervtemplate.adapters.MyAdapter
import com.example.firestorervtemplate.databinding.ActivityMainBinding
import com.example.firestorervtemplate.models.Details
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class MainActivity : AppCompatActivity(), MyAdapter.PriceFilterListener {

    private lateinit var binding: ActivityMainBinding
    private lateinit var adapter: MyAdapter
    private lateinit var db: FirebaseFirestore
    private lateinit var recyclerView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)

        db = FirebaseFirestore.getInstance()
        recyclerView = binding.recyclerView

        // Initialize RecyclerView and Adapter
        adapter = MyAdapter(this, isUserLoggedIn(), this, { details -> addToWatchlist(details) }) // Passing MainActivity as PriceFilterListener
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.addItemDecoration(
            DividerItemDecoration(
                this,
                LinearLayoutManager.VERTICAL
            )
        )
        recyclerView.adapter = adapter

        // Load data from Firestore
        loadData()

        // Apply filter button click listener
        binding.buttonApplyFilter.setOnClickListener {
            applyFilter()
        }

        // Initialize SearchView
        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                val query = newText?.trim()?.toLowerCase() ?: ""
                // Filter adapter data by address as the user types
                if (query.isNotEmpty()) {
                    adapter.filterByAddress(query)
                } else {
                    loadData()
                }
                return true
            }
        })
    }

    private fun applyFilter() {
        val minPrice = binding.editTextMinPrice.text.toString().toDoubleOrNull()?.toInt() ?: 0
        val maxPrice = binding.editTextMaxPrice.text.toString().toDoubleOrNull()?.toInt() ?: Int.MAX_VALUE
        Log.d("MainActivity", "Applying filter with minPrice: $minPrice, maxPrice: $maxPrice")
        adapter.filterByPrice(minPrice, maxPrice)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_items, menu)
        return true
    }

    private fun addToWatchlist(details: Details) {
        // Implement logic to add property to watchlist
        // This can include storing data in SharedPreferences, Firebase Realtime Database, or Firestore
        // For example, you can store the property details in Firestore under a collection named "Watchlist"
        // or in SharedPreferences
        // After adding to the watchlist, you may also show a confirmation message

        val db = FirebaseFirestore.getInstance()

        // Define a collection reference for the watchlist
        val watchlistCollection = db.collection("Watchlist")

        // Create a new document in the Watchlist collection with the property details
        watchlistCollection.add(details)
            .addOnSuccessListener { documentReference ->
                Log.d("MainActivity", "Property added to watchlist with ID: ${documentReference.id}")
                // Show a confirmation message if needed
            }
            .addOnFailureListener { e ->
                Log.w("MainActivity", "Error adding property to watchlist", e)
                // Handle errors if needed
            }

        Log.d("MainActivity", "Added to watchlist: ${details.Propertyaddress}")
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.show_map -> {
                startActivity(Intent(this, MapsActivity::class.java))
                return true
            }
            R.id.watch_list -> {
                startActivity(Intent(this, WatchlistActivity::class.java))
                return true
            }
            R.id.login -> {
                startActivity(Intent(this, LoginActivity::class.java))
                return true
            }
            R.id.logout -> {
                startActivity(Intent(this, LogoutActivity::class.java))
                return true
            }
            R.id.show_list->{
                startActivity(Intent(this, MainActivity::class.java))
                return true
            }
            else -> return super.onOptionsItemSelected(item)
        }
    }

    private fun isUserLoggedIn(): Boolean {
        return FirebaseAuth.getInstance().currentUser != null
    }

    override fun onPriceFilterApplied(minPrice: Int, maxPrice: Int) {
        adapter.filterByPrice(minPrice, maxPrice)
    }

    private fun loadData() {
        db.collection("Landlord")
            .get()
            .addOnSuccessListener { result ->
                val detailsList = mutableListOf<Details>()
                for (document in result) {
                    try {
                        val propertyImage = document.getString("PropertyImage") ?: ""
                        val numberOfBedrooms = document.getLong("Numberofbedrooms")?.toInt() ?: 0
                        val propertyAddress = document.getString("Propertyaddress") ?: ""
                        val rental = document.getLong("Rental")?.toInt() ?: 0
                        val rentalType = document.getString("Rentaltype") ?: ""
                        val latitude = document.getDouble("Latitude") ?: 0.0
                        val longitude = document.getDouble("Longitude") ?: 0.0
                        val details = Details(propertyImage, numberOfBedrooms, propertyAddress, rental, rentalType, latitude, longitude)
                        detailsList.add(details)
                    } catch (e: Exception) {
                        Log.e("MainActivity", "Error parsing document: ${document.id}", e)
                    }
                }
                adapter.setData(detailsList)
            }
            .addOnFailureListener { exception ->
                Log.e("MainActivity", "Error getting documents: ", exception)
            }
    }
}