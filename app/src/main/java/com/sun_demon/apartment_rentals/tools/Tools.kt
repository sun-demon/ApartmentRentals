package com.sun_demon.apartment_rentals.tools

import android.annotation.SuppressLint
import android.content.Context
import android.widget.EditText
import android.widget.Toast
import com.sun_demon.apartment_rentals.R
import com.sun_demon.apartment_rentals.ad.Ad
import com.sun_demon.apartment_rentals.application.Application
import com.sun_demon.apartment_rentals.exception.ad.FloorException
import com.sun_demon.apartment_rentals.exception.ad.FloorsNumberException
import com.sun_demon.apartment_rentals.exception.ad.PhotosException
import com.sun_demon.apartment_rentals.exception.ad.PriceException
import com.sun_demon.apartment_rentals.exception.ad.RoomsNumberException
import com.sun_demon.apartment_rentals.exception.ad.TotalAreaException
import com.sun_demon.apartment_rentals.exception.user.PasswordAbcException
import com.sun_demon.apartment_rentals.exception.user.PasswordFormatException
import com.sun_demon.apartment_rentals.exception.user.PasswordLengthException
import com.sun_demon.apartment_rentals.exception.user.PasswordNotDigitException
import com.sun_demon.apartment_rentals.exception.user.PasswordSpaceException
import com.sun_demon.apartment_rentals.exception.user.PhoneFormatException
import java.text.SimpleDateFormat
import java.util.Date

object Tools {
    private var toastStartTime: Long = 0L

    fun toastMessageLongTime(context: Context, message: String) {
        if (System.currentTimeMillis() - toastStartTime > 3500) {
            Toast.makeText(context, message, Toast.LENGTH_LONG).show()
            toastStartTime = System.currentTimeMillis()
        }
    }



    fun checkUser(phone: String, password: String) {
        checkPhone(phone)
        checkPassword(password)
    }

    private fun checkPhone(phone: String) {
        if (!isPhoneValid(phone))
            throw PhoneFormatException()
    }

    fun isPhoneValid(phone: String): Boolean {
        return """\d{10}""".toRegex() matches phone
    }

    fun checkPassword(password: String) {
        if (isPasswordValid(password))
            return
        throw if (password.length !in 8..40) {
            PasswordLengthException()
        } else if (!"""^.*\d.*$""".toRegex().matches(password)) {
            PasswordNotDigitException()
        } else if (!"""^.*[a-zA-Z].*$""".toRegex().matches(password)) {
            PasswordAbcException()
        } else if ("""^.*\s.*$""".toRegex() matches password) {
            PasswordSpaceException()
        } else {
            PasswordFormatException()
        }
    }

    private fun isPasswordValid(password: String): Boolean {
        return if (password.length !in 8..40)
            false
        else if ("""^\D*$""".toRegex() matches password)
            false
        else if ("""^[^a-zA-Z]*$""".toRegex() matches password)
            false
        else ("""^\S*$""".toRegex() matches password)
    }


    fun String?.toPhoneDigits(): String {
        if (this == null)
            return ""
        return this.toString().filter { it.isDigit() }.substring(1)
    }

    fun String?.toPhoneFormat(): String {
        if (this == null)
            return ""
        return """+7(${this.substring(0,3)})${this.substring(3, 6)}-${this.substring(6, 8)}-${this.substring(8, 10)}"""
    }

    fun isPhoneReserved(phone: String) =
        Application.manager
            .getUsers()
            .any { user -> user.phone == phone }



    fun checkAd(ad: Ad) {
        checkFloor(ad.floor)
        checkRoomsNumber(ad.roomsNumber)
        checkTotalArea(ad.totalArea)
        checkFloorsNumber(ad.floor, ad.floorsNumber)
        checkPhotos(ad.photos)
        checkPrice(ad.price)
    }

    private fun checkFloor(floor: Int) {
        if (floor !in 1..20)
            throw FloorException()
    }

    private fun checkRoomsNumber(roomsNumber: Int) {
        if (roomsNumber !in 1..20)
            throw RoomsNumberException()
    }

    private fun checkTotalArea(totalArea: Double) {
        if (totalArea !in 10.0..99.9)
            throw TotalAreaException()
    }

    private fun checkFloorsNumber(floor: Int, floorsNumber: Int) {
        if (floorsNumber !in 1..20 || floorsNumber < floor)
            throw FloorsNumberException()
    }

    private fun checkPhotos(photos: ArrayList<Int>) {
        if (photos.any { it < 0 })
            throw PhotosException()

    }

    private fun checkPrice(price: Int) {
        if (price !in 0..200000)
            throw PriceException()
    }

    @SuppressLint("SimpleDateFormat")
    fun Long.toDateTime(): String {
        val date = Date(this)
        val format = SimpleDateFormat("HH:mm dd.MM.yyyy")
        return format.format(date)
    }


    fun toBad(editText: EditText, message: String) {
        editText.setBackgroundResource(R.drawable.button_bad)
        editText.error = message
    }

    fun toNormal(editText: EditText) {
        editText.setBackgroundResource(R.drawable.button_normal)
    }

    fun toFun(editText: EditText) {
        editText.setBackgroundResource(R.drawable.button_fun)
    }
}