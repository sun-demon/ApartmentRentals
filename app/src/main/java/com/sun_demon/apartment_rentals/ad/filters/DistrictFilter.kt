package com.sun_demon.apartment_rentals.ad.filters

import kotlinx.serialization.Serializable

@Serializable
data class DistrictFilter (
    var lenin: Boolean = true,
    var moscow: Boolean = true,
    var october: Boolean = true
)