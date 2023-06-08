package com.sun_demon.apartment_rentals.database

import com.sun_demon.apartment_rentals.user.User

interface UserController {
    fun insertUser(user: User): Int
    fun deleteUser(id: Int)
    fun updateUser(user: User)
    fun getUsers(): ArrayList<User>
}