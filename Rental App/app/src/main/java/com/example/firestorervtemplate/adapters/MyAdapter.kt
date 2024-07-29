package com.example.firestorervtemplate.adapters

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.firestorervtemplate.PropertyDetail
import com.example.firestorervtemplate.R
import com.example.firestorervtemplate.models.Details

class MyAdapter(
    private val context: Context,
    private val isUserLoggedIn: Boolean,
    private val priceFilterListener: PriceFilterListener,
    private val addToWatchlistListener: (Details) -> Unit
) : RecyclerView.Adapter<MyAdapter.MyViewHolder>() {
    private var dataList = mutableListOf<Details>()
    private var filteredList = mutableListOf<Details>()

    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvAddress: TextView = itemView.findViewById(R.id.tv_address)
        val tvPrice: TextView = itemView.findViewById(R.id.tv_price)
        val ivPropertyImage: ImageView = itemView.findViewById(R.id.iv_property_image)
        val btnAddToWatchlist: Button = itemView.findViewById(R.id.btn_add_to_watchlist)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_property, parent, false)
        return MyViewHolder(view)
    }

    override fun getItemCount(): Int {
        return filteredList.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currItem = filteredList[position]

        holder.btnAddToWatchlist.isEnabled = isUserLoggedIn // Disable the button if user is not logged in
        holder.btnAddToWatchlist.setOnClickListener {
            if (isUserLoggedIn) {
                // Call the listener with the current property only if user is logged in
                addToWatchlistListener(currItem)
            } else {
                // Optionally, show a message indicating that the user needs to be logged in
                Toast.makeText(context, "Please log in to add to watchlist", Toast.LENGTH_SHORT).show()
            }
        }

        holder.tvAddress.text = currItem.Propertyaddress
        holder.tvPrice.text = "Monthly price: ${currItem.Rental}, Bedrooms: ${currItem.Numberofbedrooms}"

        Glide.with(holder.itemView.context)
            .load(currItem.PropertyImage)
            .into(holder.ivPropertyImage)

        holder.itemView.setOnClickListener {
            // Create Intent to open PropertyDetailsActivity
            val intent = Intent(context, PropertyDetail::class.java).apply {
                // Pass the property details as extras
                putExtra("propertyImage", currItem.PropertyImage)
                putExtra("numberOfBedrooms", currItem.Numberofbedrooms)
                putExtra("propertyAddress", currItem.Propertyaddress)
                putExtra("rental", currItem.Rental)
                putExtra("rentalType", currItem.Rentaltype)
            }
            // Start PropertyDetailsActivity
            context.startActivity(intent)
        }
    }

    fun setData(data: List<Details>) {
        dataList.clear()
        dataList.addAll(data)
        filteredList.clear()
        filteredList.addAll(data)
        notifyDataSetChanged()
    }

    fun filterByAddress(address: String) {
        filteredList.clear()
        if (address.isEmpty()) {
            filteredList.addAll(dataList)
        } else {
            val searchText = address.toLowerCase().trim()
            dataList.forEach { item ->
                if (item.Propertyaddress.toLowerCase().contains(searchText)) {
                    filteredList.add(item)
                }
            }
        }
        notifyDataSetChanged()
    }

    fun filterByPrice(minPrice: Int, maxPrice: Int) {
        filteredList.clear()
        dataList.forEach { item ->
            if (item.Rental in minPrice..maxPrice) {
                filteredList.add(item)
            }
        }
        notifyDataSetChanged()
    }

    // Interface to communicate price filter events to MainActivity
    interface PriceFilterListener {
        fun onPriceFilterApplied(minPrice: Int, maxPrice: Int)
    }
}