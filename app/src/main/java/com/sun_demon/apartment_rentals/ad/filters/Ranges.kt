package com.sun_demon.apartment_rentals.ad.filters

import kotlinx.serialization.Serializable

@Serializable
open class Ranges (
    var price: Range<Int> = Range(0, 99999),
    var roomsNumber: Range<Int> = Range(1, 20),
    var totalArea: Range<Double> = Range(10.0, 99.9),
    var floor: FloorsFilter = FloorsFilter()
)