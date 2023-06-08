package com.sun_demon.apartment_rentals

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.KeyEvent
import com.sun_demon.apartment_rentals.databinding.ActivityAboutDeveloperBinding

class AboutDeveloperActivity : AppCompatActivity() {
    lateinit var binding: ActivityAboutDeveloperBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAboutDeveloperBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.back.setOnClickListener {
            startActivity(Intent(this, ProfileActivity::class.java))
        }
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        return if (keyCode == KeyEvent.KEYCODE_BACK)
            binding.back.callOnClick()
        else
            super.onKeyDown(keyCode, event)
    }
}