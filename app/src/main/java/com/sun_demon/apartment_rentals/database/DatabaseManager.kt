package com.sun_demon.apartment_rentals.database

import android.annotation.SuppressLint
import android.content.Context
import android.net.Uri
import androidx.core.net.toUri
import com.sun_demon.apartment_rentals.ad.Ad
import com.sun_demon.apartment_rentals.user.User
import java.io.File

class DatabaseManager(val context: Context) : UserController, AdController, PhotoController {
    private val database = Database(context)

    override fun insertUser(user: User): Int {
        user.id = database.nextUserID++
        database.users.add(user)
        save()
        return user.id
    }
    override fun deleteUser(id: Int) {
        database.users.removeAll { it.id == id }
        save()
    }
    override fun updateUser(user: User) {
        deleteUser(user.id)
        database.users.add(user)
        save()
    }
    override fun getUsers(): ArrayList<User> = database.users


    override fun insertAd(ad: Ad): Int {
        ad.id = database.nextAdID++
        database.ads.add(ad)
        save()
        return ad.id
    }
    override fun deleteAd(id: Int) {
        val ad = database.ads.first { it.id == id }
        database.ads.remove(ad)
        save()
    }
    override fun updateAd(ad: Ad) {
        deleteAd(ad.id)
        database.ads.add(ad)
        save()
    }
    override fun getAds(): ArrayList<Ad> = database.ads


    @SuppressLint("Recycle")
    override fun insertPhoto(uri: Uri): Int {
        val id = database.nextPhotoID++
        val fileName = id.toString()
        val newFile = File(database.photosFolder(), fileName)
        newFile.createNewFile()
        val inputStream = context.contentResolver.openInputStream(uri)
        val outputStream = newFile.outputStream()
        inputStream!!.copyTo(outputStream)
        inputStream.close()
        outputStream.close()
        return id
    }

    override fun deletePhoto(id: Int) {
        val fileName = id.toString()
        val file = File(database.photosFolder(), fileName)
        file.delete()
    }

    override fun getPhoto(id: Int): Uri {
        val fileName = id.toString()
        val file = File(database.photosFolder(), fileName)
        return file.toUri()
    }

    
    private fun save() {
        database.save()
    }
}