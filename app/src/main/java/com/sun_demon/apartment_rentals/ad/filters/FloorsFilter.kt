package com.sun_demon.apartment_rentals.ad.filters

import kotlinx.serialization.Serializable

@Serializable
open class FloorsFilter: Range<Int> {
    constructor (
        from: Int = 1,
        to: Int = 20,
        notFirst: Boolean = false,
        notLast: Boolean = false
    ) : super(from, to) {
        this.notFirst = notFirst
        this.notLast = notLast
    }

    var notFirst: Boolean
    var notLast: Boolean
}