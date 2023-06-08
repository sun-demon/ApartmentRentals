package com.sun_demon.apartment_rentals.ad.filters

import kotlinx.serialization.Serializable

@Serializable
open class Range<T>  (
    var from: T,
    var to: T
)