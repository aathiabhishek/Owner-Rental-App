package com.example.myproject.models

import android.os.Parcel
import android.os.Parcelable
import com.google.firebase.firestore.DocumentId

data class LandlordProfiles(

    @DocumentId
    var PropertyImage: String = "",
    var Numberofbedrooms: Int = 0,
    var Propertyaddress: String = "",
    var Rental: Int = 0,
    var Rentaltype: String = "",

    @JvmField
    var isAvailable: Boolean = true,
    var Latitude: Double = 0.0,
    var Longitude: Double = 0.0
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString() ?: "https://via.placeholder.com/250",
        parcel.readInt(),
        parcel.readString() ?: "",
        parcel.readInt(),
        parcel.readString() ?: "",
        parcel.readByte() != 0.toByte(),
        parcel.readDouble(),
        parcel.readDouble()
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(PropertyImage)
        parcel.writeInt(Numberofbedrooms)
        parcel.writeString(Propertyaddress)
        parcel.writeInt(Rental)
        parcel.writeString(Rentaltype)
        parcel.writeByte(if (isAvailable) 1 else 0)
        parcel.writeDouble(Latitude)
        parcel.writeDouble(Longitude)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<LandlordProfiles> {
        override fun createFromParcel(parcel: Parcel): LandlordProfiles {
            return LandlordProfiles(parcel)
        }

        override fun newArray(size: Int): Array<LandlordProfiles?> {
            return arrayOfNulls(size)
        }
    }
}
