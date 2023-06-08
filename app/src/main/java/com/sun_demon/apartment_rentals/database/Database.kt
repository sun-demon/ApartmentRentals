package com.sun_demon.apartment_rentals.database

import android.content.Context
import android.util.Log
import com.sun_demon.apartment_rentals.ad.Ad
import com.sun_demon.apartment_rentals.user.User
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.io.File

@Serializable
class Database {
    @Transient
    private var context: Context? = null
    @Transient
    private val databaseFileName = "database.json"
    @Transient
    private val photosDirectoryName="photos"

    var nextUserID: Int = 0
    var users: ArrayList<User> = ArrayList()

    var nextAdID: Int = 0
    var ads: ArrayList<Ad> = ArrayList()

    var nextPhotoID: Int = 0

    constructor(context: Context) {
        this.context = context
        Log.d("APP_PATH", context.getExternalFilesDir("/")!!.absolutePath)
        val photoFolder = File(context.getExternalFilesDir("/"), photosDirectoryName)
        if (!photoFolder.exists())
            photoFolder.mkdirs()
        Log.d("PHOTOS_PATH", photoFolder.path)

        val databaseFile = File(context.getExternalFilesDir("/"), databaseFileName)

        if (!databaseFile.exists() || !databaseFile.isFile)
            return
        val instance: Database = Json.decodeFromString(databaseFile.readText())
        this.nextUserID = instance.nextUserID
        this.users = instance.users
        this.nextAdID = instance.nextAdID
        this.ads = instance.ads
        this.nextPhotoID = instance.nextPhotoID
    }

    fun photosFolder() =
        File(context!!.getExternalFilesDir("/"), photosDirectoryName)

    fun save() {
        try {
            val fileDatabase = File(context!!.getExternalFilesDir("/"), databaseFileName)
            Log.d("SAVING_DATABASE","")

            Log.d("NEXT_USER_ID", nextUserID.toString())
            Log.d("USERS","")
            users.mapIndexed { i, user -> Log.d("USER_$i", Json.encodeToString(user)) }

            Log.d("NEXT_AD_ID", nextAdID.toString())
            Log.d("ADS","")
            ads.mapIndexed { i, ad -> Log.d("AD_$i", Json.encodeToString(ad)) }

            Log.d("NEXT_PHOTO_ID", nextPhotoID.toString())

            fileDatabase.writeText(Json.encodeToString(this))
        } catch (exc: Exception) {
            Log.e("SAVING_DATABASE", exc.message.toString())
            exc.printStackTrace()
        }
    }
}