package com.sun_demon.apartment_rentals.application

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import com.sun_demon.apartment_rentals.ad.filters.Config
import com.sun_demon.apartment_rentals.database.DatabaseManager
import com.sun_demon.apartment_rentals.user.User
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.io.File

class Application private constructor(private val context: Context) {
    private val userFileName = "user.json"
    private val appManager = DatabaseManager(context)

    var appUser: User? = readUser()
        set(value) {
            writeUser(value)
            field = value
        }
    var appConfig: Config = Config()

    private fun readUser(): User? {
        val userFile = File(context.getExternalFilesDir("/"), userFileName)
        if (userFile.exists() && userFile.isFile) {
            val userJson = userFile.readText()
            Log.d("READ_USER", userJson)
            return Json.decodeFromString(userJson)
        }
        return null
    }

    private fun writeUser(user: User?) {
        val userFile = File(context.getExternalFilesDir("/"), userFileName)
        val userJson = Json.encodeToString(user)
        Log.d("WRITE_USER", userJson)
        userFile.writeText(userJson)
    }

    companion object {
        @SuppressLint("StaticFieldLeak")
        private var instance: Application? = null

        fun init (context: Context) {
            if (instance == null)
                instance = Application(context)
        }

        val manager: DatabaseManager
            get() {
                if (instance == null) {
                    Log.e("APPLICATION", "Singleton wasn't initialized with fun \"init(Context)\"")
                    throw UninitializedPropertyAccessException("Application object doesn't initialized with fun \"init(Context)\"")
                } else {
                    return instance!!.appManager
                }
            }

        var user: User?
            get() {
                if (instance == null) {
                    Log.e("APPLICATION", "Singleton wasn't initialized with fun \"init(Context)\"")
                    throw UninitializedPropertyAccessException("Application object doesn't initialized with fun \"init(Context)\"")
                } else {
                    return instance!!.appUser
                }
            }
            set(value) {
                if (instance == null) {
                    Log.e("APPLICATION", "Singleton wasn't initialized with fun \"init(Context)\"")
                    throw UninitializedPropertyAccessException("Application object doesn't initialized with fun \"init(Context)\"")
                } else {
                    instance!!.appUser = value
                }
            }

        var config: Config
            get() {
                if (instance == null) {
                    Log.e("APPLICATION", "Singleton wasn't initialized with fun \"init(Context)\"")
                    throw UninitializedPropertyAccessException("Application object doesn't initialized with fun \"init(Context)\"")
                } else {
                    return instance!!.appConfig
                }
            }
            set(value) {
                if (instance == null) {
                    Log.e("APPLICATION", "Singleton wasn't initialized with fun \"init(Context)\"")
                    throw UninitializedPropertyAccessException("Application object doesn't initialized with fun \"init(Context)\"")
                } else {
                    instance!!.appConfig = value
                }
            }
    }
}