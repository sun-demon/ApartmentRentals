package com.sun_demon.apartment_rentals.dialog

import android.app.AlertDialog
import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.DialogFragment
import com.sun_demon.apartment_rentals.AdsActivity
import com.sun_demon.apartment_rentals.R
import com.sun_demon.apartment_rentals.application.Application
import java.lang.IllegalStateException

class ExitDialog : DialogFragment() {
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            val builder = AlertDialog.Builder(it)
            builder.setTitle(R.string.attention)
                .setMessage("Вы уверены, что хотите выйти?")
                .setPositiveButton(R.string.yes) { _, _ ->
                    Log.d("SELECT", "${getString(R.string.yes)} button in exit dialog is clicked")
                    Application.user = null
                    startActivity(Intent(it, AdsActivity::class.java))
                }
                .setNegativeButton(R.string.no) { dialog, _ ->
                    Log.d("SELECT", "${getString(R.string.no)} button in exit button dialog is clicked")
                    dialog.cancel()
                }
            builder.create()
        } ?: throw IllegalStateException("Activity can't be null")
    }
}