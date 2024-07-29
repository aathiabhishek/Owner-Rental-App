package com.example.myproject.adapter

import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.Switch
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.myproject.R
import com.example.myproject.models.LandlordProfiles
import com.example.myproject.Property


class MyAdapter(var yourListData:List<LandlordProfiles>,
                var Available: (String)->Unit, private val addToWatchlistListener: (LandlordProfiles) -> Unit ) : RecyclerView.Adapter<MyAdapter.MyViewHolder>() {


    private var dataList = mutableListOf<LandlordProfiles>()
    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder (itemView) {
        val btnAddToWatchlist: Button = itemView.findViewById(R.id.updateButton)
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view: View = LayoutInflater.from(parent.context).inflate(R.layout.row_layout, parent, false)
        return MyViewHolder(view)
    }
    override fun getItemCount(): Int {
        return yourListData.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currItem: LandlordProfiles = yourListData.get(position)
        val tvRow1 = holder.itemView.findViewById<TextView>(R.id.textView5)
        val tvRow2 = holder.itemView.findViewById<TextView>(R.id.textView6)
        val tvRow3 = holder.itemView.findViewById<TextView>(R.id.textView7)
        val tvRow4 = holder.itemView.findViewById<TextView>(R.id.textView8)
        tvRow1.text = currItem.Numberofbedrooms.toString()
        tvRow2.text = currItem.Rental.toString()
        tvRow3.text = currItem.Rentaltype
        tvRow4.text =currItem.Propertyaddress
       // val btnUpdate: Button = holder.itemView.findViewById<Button>(R.id.updateButton)
        val switch1: Switch = holder.itemView.findViewById(R.id.switch1)
        val iv = holder.itemView.findViewById<ImageView>(R.id.imageView)
        Log.d("TESTING", "What is the image: ${currItem.PropertyImage}")
        Glide.with(holder.itemView.context)
            .load(currItem.PropertyImage)
            .into(iv)

        holder.btnAddToWatchlist.setOnClickListener {
            addToWatchlistListener(currItem)
            val intent = Intent(holder.itemView.context, Property::class.java)
            intent.putExtra("Property", currItem)
            holder.itemView.context.startActivity(intent)

        }

        switch1.isChecked = currItem.isAvailable // Set initial state
        switch1.setOnCheckedChangeListener { buttonView, isChecked ->
            Available(isChecked.toString())
        }
    }

    fun setData(data: List<LandlordProfiles>) {
        dataList.clear()
        dataList.addAll(data)
        notifyDataSetChanged()
    }

}