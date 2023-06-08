package com.sun_demon.apartment_rentals.ad.filters

import kotlinx.serialization.Serializable

@Serializable
enum class Sorting {
    DATE,
    PRICE_IN_ASCENDING,
    PRICE_IN_DESCENDING
}