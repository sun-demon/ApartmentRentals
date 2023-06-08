package com.sun_demon.apartment_rentals.database

import android.net.Uri

interface PhotoController {
    fun insertPhoto(uri: Uri): Int
    fun deletePhoto(id: Int)
    fun getPhoto(id: Int): Uri
}