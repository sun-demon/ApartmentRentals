package com.sun_demon.apartment_rentals

import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.sun_demon.apartment_rentals.application.Application
import com.sun_demon.apartment_rentals.tools.Tools
import com.sun_demon.apartment_rentals.tools.Tools.toPhoneFormat
import com.sun_demon.apartment_rentals.databinding.ActivityProfileBinding
import com.sun_demon.apartment_rentals.dialog.Dialog

class ProfileActivity : AppCompatActivity() {
    private lateinit var binding: ActivityProfileBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.phone.text = Application.user!!.phone.toPhoneFormat()

        binding.exit.setOnClickListener {
            val dialog = Dialog(this, getString(R.string.do_you_wont_exit_from_account),
                onYes = {
                    Application.user = null
                    it.dismiss()
                    startActivity(Intent(this, SignInActivity::class.java))
                },
                onNo = {
                    it.dismiss()
                })
            dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            dialog.show()
        }

        binding.aboutDeveloper.setOnClickListener {
            startActivity(Intent(this, AboutDeveloperActivity::class.java))
        }

        binding.changePassword.setOnClickListener {
            Tools.toastMessageLongTime(this, "In develop")
            // TODO( start change password activity )
        }

        binding.main.setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
        }

        binding.myAds.setOnClickListener {
            startActivity(Intent(this, AdsActivity::class.java))
        }
    }
}