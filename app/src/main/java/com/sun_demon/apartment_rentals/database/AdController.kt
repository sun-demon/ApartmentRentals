package com.sun_demon.apartment_rentals.database

import com.sun_demon.apartment_rentals.ad.Ad

interface AdController {
    fun insertAd(ad: Ad): Int
    fun deleteAd(id: Int)
    fun updateAd(ad: Ad)
    fun getAds(): ArrayList<Ad>
}