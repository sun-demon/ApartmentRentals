package com.sun_demon.apartment_rentals.user

import kotlinx.serialization.Serializable

@Serializable
class User {

    var id: Int
    var phone: String
    var password: String

    constructor(id: Int = 0, phone: String = "", password: String = "") {
        this.id = id
        this.phone = phone
        this.password = password
    }
}