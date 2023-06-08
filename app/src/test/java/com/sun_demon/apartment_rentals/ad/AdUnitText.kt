package com.sun_demon.apartment_rentals.ad

import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.junit.Assert.*
import org.junit.Test

class AdUnitText {
    private val userID = 100
    private val photos = arrayListOf(-1, -1, -1, -1)
    private val price = 10000
    private val roomsNumber = 2
    private val totalArea = 30.2
    private val floor = 3
    private val floorsNumber = 4
    private val district = District.MOSCOW
    private val possibleWithChildren = true

    @Test
    fun serializationArray_isCorrect() {
        val ad = Ad(
            userID = userID,
            photos = photos,
            price = price,
            roomsNumber = roomsNumber,
            totalArea = totalArea,
            floor = floor,
            floorsNumber = floorsNumber,
            district = district,
            possibleWithChildren = possibleWithChildren
        )
        val id = ad.id
        val dateTime = ad.dateTime

        val userID2 = 5000
        val photos2 = arrayListOf(-2, -3, -1, -4)
        val price2 = 20000
        val roomsNumber2 = 4
        val totalArea2 = 55.3
        val floor2 = 1
        val floorsNumber2 = 10
        val district2 = District.OCTOBER
        val possibleWithChildren2 = false

        val ad2 = Ad(
            userID = userID2,
            photos = photos2,
            price = price2,
            roomsNumber = roomsNumber2,
            totalArea = totalArea2,
            floor = floor2,
            floorsNumber = floorsNumber2,
            district = district2,
            possibleWithChildren = possibleWithChildren2
        )
        val id2 = ad2.id
        val dateTime2 = ad2.dateTime

        val list = arrayListOf(ad, ad2)
        val json = Json.encodeToString(list)

        assertEquals(json,
                """[{"id":$id,"userID":$userID,""" +
                """"photos":[${photos[0]},${photos[1]},${photos[2]},${photos[3]}],""" +
                """"price":$price,"roomsNumber":$roomsNumber,"totalArea":$totalArea,""" +
                """"floor":$floor,"floorsNumber":$floorsNumber,"district":"$district",""" +
                """"possibleWithChildren":$possibleWithChildren,"dateTime":$dateTime},""" +
                """{"id":$id2,"userID":$userID2,""" +
                """"photos":[${photos2[0]},${photos2[1]},${photos2[2]},${photos2[3]}],""" +
                """"price":$price2,"roomsNumber":$roomsNumber2,"totalArea":$totalArea2,""" +
                """"floor":$floor2,"floorsNumber":$floorsNumber2,"district":"$district2",""" +
                """"possibleWithChildren":$possibleWithChildren2,"dateTime":$dateTime2}]"""
            )
    }
}