package com.sun_demon.apartment_rentals.ad

import android.util.Log
import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

@Serializable
class Ad {
    var id: Int
    val userID: Int
    var photos: ArrayList<Int>
    var price: Int
    var roomsNumber: Int
    var totalArea: Double
    var floor: Int
    var floorsNumber: Int
    var district: District
    var possibleWithChildren: Boolean
    var dateTime: Long

    constructor(
        userID: Int,
        photos: ArrayList<Int> = arrayListOf(-1, -1, -1, -1),
        price: Int = -1,
        roomsNumber: Int = -1,
        totalArea: Double = -1.0,
        floor: Int = -1,
        floorsNumber: Int = -1,
        district: District = District.LENIN,
        possibleWithChildren: Boolean = true
    ) {
        id = -1
        this.userID = userID
        this.photos = photos
        this.price = price
        this.roomsNumber = roomsNumber
        this.totalArea = totalArea
        this.floor = floor
        this.floorsNumber = floorsNumber
        this.district = district
        this.possibleWithChildren = possibleWithChildren
        dateTime = System.currentTimeMillis()
    }

    val pricePerSquare: Double
        get() = price.toDouble() / totalArea

    fun contentEquals(other: Any?): Boolean {
        Log.d("AD_CONTENT_EQUALS","")
        if (other !is Ad)
            return false
        Log.d("THE_AD", Json.encodeToString(this))
        Log.d("OTHER_AD", Json.encodeToString(other))
        return id == other.id &&
                photos.mapIndexed { i, photoID -> photoID == other.photos[i] }.all { it } &&
                price == other.price &&
                roomsNumber == other.roomsNumber &&
                totalArea == other.totalArea &&
                floor == other.floor &&
                floorsNumber == other.floorsNumber &&
                district == other.district &&
                possibleWithChildren == other.possibleWithChildren &&
                dateTime == other.dateTime
    }
}