package com.sun_demon.apartment_rentals.dialog

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.Window
import android.widget.Button
import android.widget.TextView
import com.sun_demon.apartment_rentals.R
import com.sun_demon.apartment_rentals.databinding.DialogBinding

class Dialog(
    context: Context,
    val message: String,
    val onYes: (Dialog) -> (Unit),
    val onNo: (Dialog) -> (Unit)
) : Dialog(context) {
    private lateinit var binding: DialogBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        binding = DialogBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.text.text = message
        binding.yes.setOnClickListener { onYes(this) }
        binding.no.setOnClickListener { onNo(this) }
    }
}
