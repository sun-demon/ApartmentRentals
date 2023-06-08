package com.sun_demon.apartment_rentals.dialog

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.Window
import androidx.core.text.isDigitsOnly
import com.sun_demon.apartment_rentals.MainActivity
import com.sun_demon.apartment_rentals.R
import com.sun_demon.apartment_rentals.ad.filters.DistrictFilter
import com.sun_demon.apartment_rentals.ad.filters.Range
import com.sun_demon.apartment_rentals.application.Application
import com.sun_demon.apartment_rentals.databinding.DialogPriceBinding
import com.sun_demon.apartment_rentals.databinding.DialogTotalAreaBinding
import com.sun_demon.apartment_rentals.tools.Tools
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

class TotalAreaDialog(
    context: Context
) : Dialog(context) {
    private lateinit var binding: DialogTotalAreaBinding
    private lateinit var range: Range<Double>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        binding = DialogTotalAreaBinding.inflate(layoutInflater)
        setContentView(binding.root)

        range = Json.decodeFromString(Json.encodeToString(Application.config.ranges.totalArea))

        initViews()
        initListeners()
    }

    private fun initViews() {
        initFrom()
        initTo()
    }

    private fun initFrom() {
        binding.from.setText(range.from.toString())
    }

    private fun initTo() {
        binding.to.setText(range.to.toString())
    }

    private fun initListeners() {
        initFromListener()
        initToListener()

        initBackListener()
        initResetListener()
        initApplyListener()
    }

    private fun initFromListener() {
        binding.from.addTextChangedListener(object : TextWatcher { var before: String = ""
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) { before = s.toString()}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable?) {
                val after = s.toString()
                if (after == "") {
                    binding.from.setBackgroundResource(R.drawable.button_bad)
                    range.from = -1.0
                    return
                }
                if (!"""\d{0,2}(\.\d|\.)?""".toRegex().matches(after))
                    binding.from.setText(before)
                else
                    range.from = after.toDouble()
                binding.from.setBackgroundResource(R.drawable.button_normal)
            }
        })
    }

    private fun initToListener() {
        binding.to.addTextChangedListener(object : TextWatcher { var before: String = ""
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) { before = s.toString()}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable?) {
                val after = s.toString()
                if (after == "") {
                    binding.to.setBackgroundResource(R.drawable.button_bad)
                    range.to = -1.0
                    return
                }
                if (!"""\d{0,2}(\.\d|\.)?""".toRegex().matches(after))
                    binding.to.setText(before)
                else
                    range.to = after.toDouble()
                binding.to.setBackgroundResource(R.drawable.button_normal)
            }
        })
    }

    private fun initBackListener() {
        binding.back.setOnClickListener {
            dismiss()
        }
    }

    private fun initResetListener() {
        binding.reset.setOnClickListener {
            val config = Application.config
            config.ranges.totalArea = Range(10.0, 99.9)
            Application.config = config
            dismiss()
            context.startActivity(Intent(context, MainActivity::class.java))
        }
    }

    private fun initApplyListener() {
        binding.apply.setOnClickListener {
            if (range.from !in 9.99..100.0 || range.to !in 9.99..100.0) {
                Tools.toastMessageLongTime(context, context.getString(R.string.fields_not_full))
                return@setOnClickListener
            }
            if (range.from > range.to) {
                Tools.toastMessageLongTime(context, context.getString(R.string.message_from_to_not_order))
                return@setOnClickListener
            }
            val config = Application.config
            config.ranges.totalArea = range
            Application.config = config
            dismiss()
            context.startActivity(Intent(context, MainActivity::class.java))
        }
    }
}