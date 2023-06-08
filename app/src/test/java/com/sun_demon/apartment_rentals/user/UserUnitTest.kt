package com.sun_demon.apartment_rentals.user

import kotlinx.serialization.*
import kotlinx.serialization.json.Json
import org.junit.Assert.*
import org.junit.Test

class UserUnitTest {
    private val phone = "8005553535"
    private val password = "qwerty123"

    @Test
    fun constructor_isCorrect() {
        val user = User(phone = phone, password = password)
        assertEquals(user.phone, phone)
        assertEquals(user.password, password)
    }

    @Test
    fun serialization_isCorrect() {
        val user = User(phone = phone, password = password)
        val json = Json.encodeToString(user)
        assertEquals(json, """{"id":${user.id},"phone":"$phone","password":"$password"}""")
    }

    @Test
    fun deserialization_isCorrect() {
        val id = 7
        val json = """{"id":"$id","phone":"$phone","password":"$password"}"""
        val user = Json.decodeFromString<User>(json)
        assertEquals(user.id, id)
        assertEquals(user.phone, phone)
        assertEquals(user.password, password)
    }

    @Test
    fun serializationArray_isCorrect() {
        val phone2 = """1${phone.substring(1)}"""
        val password2 = "qwerty321"

        val user1 = User(phone = phone, password = password)
        val user2 = User(phone = phone2, password = password2)
        val list: ArrayList<User> = arrayListOf(user1, user2)

        val json = Json.encodeToString(list)

        assertEquals(json,
            """[{"id":${user1.id},"phone":"${user1.phone}","password":"${user1.password}"},""" +
             """{"id":${user2.id},"phone":"${user2.phone}","password":"${user2.password}"}]"""
        )

    }
}