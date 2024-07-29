package com.example.firestorervtemplate.models

import com.google.firebase.firestore.DocumentId

data class Details(
    @DocumentId
    var PropertyImage: String = "",
    var Numberofbedrooms: Int = 0,
    var Propertyaddress: String = "",
    var Rental: Int = 0,
    var Rentaltype: String = "",
    var Latitude: Double? = null, // Update to nullable Double
    var Longitude: Double? = null // Update to nullable Double
)

