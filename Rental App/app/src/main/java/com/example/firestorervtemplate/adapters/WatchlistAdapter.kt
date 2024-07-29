import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.firestorervtemplate.R
import com.example.firestorervtemplate.models.Details
import com.google.firebase.firestore.FirebaseFirestore

class WatchlistAdapter : RecyclerView.Adapter<WatchlistAdapter.WatchlistViewHolder>() {

    private var watchlistData = mutableListOf<Details>()

    inner class WatchlistViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvAddress: TextView = itemView.findViewById(R.id.tv_address)
        val tvPrice: TextView = itemView.findViewById(R.id.tv_price)
        val ivPropertyImage: ImageView = itemView.findViewById(R.id.iv_property_image)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WatchlistViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_watchlist_property, parent, false)
        return WatchlistViewHolder(view)
    }

    override fun onBindViewHolder(holder: WatchlistViewHolder, position: Int) {
        val currItem = watchlistData[position]

        holder.tvAddress.text = currItem.Propertyaddress
        holder.tvPrice.text = "Monthly price: ${currItem.Rental}, Bedrooms: ${currItem.Numberofbedrooms}"

        Glide.with(holder.itemView.context)
            .load(currItem.PropertyImage)
            .into(holder.ivPropertyImage)

    }

    override fun getItemCount(): Int {
        return watchlistData.size
    }

    fun setData(data: List<Details>) {
        watchlistData.clear()
        watchlistData.addAll(data)
        notifyDataSetChanged()
    }

    private fun removeFromWatchlist(details: Details) {
        Log.d("WatchlistAdapter", "Removing from watchlist: ${details.Propertyaddress}")
        // Access Firebase Firestore instance
        val db = FirebaseFirestore.getInstance()

        // Define a collection reference for the watchlist
        val watchlistCollection = db.collection("Watchlist")

        // Query to find the document to delete
        watchlistCollection.whereEqualTo("Propertyaddress", details.Propertyaddress)
            .get()
            .addOnSuccessListener { documents ->
                for (document in documents) {
                    Log.d("WatchlistAdapter", "Found document: ${document.id}")
                    // Delete the document from the watchlist
                    watchlistCollection.document(document.id).delete()
                        .addOnSuccessListener {
                            // Remove the item from the adapter's list
                            watchlistData.remove(details)
                            notifyDataSetChanged()
                            Log.d("WatchlistAdapter", "Document deleted successfully")
                        }
                        .addOnFailureListener { e ->
                            // Handle errors if needed
                            Log.e("WatchlistAdapter", "Error deleting document", e)
                        }
                }
            }
            .addOnFailureListener { exception ->
                // Handle errors if needed
                Log.e("WatchlistAdapter", "Error getting documents", exception)
            }
    }
}
