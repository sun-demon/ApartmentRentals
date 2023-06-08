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
import com.sun_demon.apartment_rentals.ad.filters.FloorsFilter
import com.sun_demon.apartment_rentals.ad.filters.Range
import com.sun_demon.apartment_rentals.application.Application
import com.sun_demon.apartment_rentals.databinding.DialogFloorBinding
import com.sun_demon.apartment_rentals.databinding.DialogPriceBinding
import com.sun_demon.apartment_rentals.databinding.DialogRoomsNumberBinding
import com.sun_demon.apartment_rentals.tools.Tools
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

class FloorDialog(
    context: Context
) : Dialog(context) {
    private lateinit var binding: DialogFloorBinding
    private lateinit var filter: FloorsFilter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        binding = DialogFloorBinding.inflate(layoutInflater)
        setContentView(binding.root)

        filter = Json.decodeFromString(Json.encodeToString(Application.config.ranges.floor))

        initViews()
        initListeners()
    }

    private fun initViews() {
        initFrom()
        initTo()
        initNotFirst()
        initNotLast()
    }

    private fun initFrom() {
        binding.from.setText(filter.from.toString())
    }

    private fun initTo() {
        binding.to.setText(filter.to.toString())
    }

    private fun initNotFirst() {
        binding.notFirst.isChecked = filter.notFirst
    }

    private fun initNotLast() {
        binding.notLast.isChecked = filter.notLast
    }

    private fun initListeners() {
        initFromListener()
        initToListener()
        initNotFirstListener()
        initNotLastListener()

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
                    filter.from = -1
                    return
                }
                if (!after.isDigitsOnly() || after.toInt() > 20)
                    binding.from.setText(before)
                else
                    filter.from = after.toInt()
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
                    filter.to = -1
                    return
                }
                if (!after.isDigitsOnly() || after.toInt() > 20)
                    binding.to.setText(before)
                else
                    filter.to = after.toInt()
                binding.to.setBackgroundResource(R.drawable.button_normal)
            }
        })
    }

    private fun initNotFirstListener() {
        binding.notFirst.setOnClickListener {
            filter.notFirst = binding.notFirst.isChecked
        }
    }

    private fun initNotLastListener() {
        binding.notLast.setOnClickListener {
            filter.notLast = binding.notLast.isChecked
        }
    }

    private fun initBackListener() {
        binding.back.setOnClickListener {
            dismiss()
        }
    }

    private fun initResetListener() {
        binding.reset.setOnClickListener {
            val config = Application.config
            config.ranges.floor = FloorsFilter()
            Application.config = config
            dismiss()
            context.startActivity(Intent(context, MainActivity::class.java))
        }
    }

    private fun initApplyListener() {
        binding.apply.setOnClickListener {
            if (filter.from !in 1..20 || filter.to !in 1..20) {
                Tools.toastMessageLongTime(context, context.getString(R.string.message_floor_range))
                return@setOnClickListener
            }
            if (filter.from > filter.to) {
                Tools.toastMessageLongTime(context, context.getString(R.string.message_from_to_not_order))
                return@setOnClickListener
            }
            val config = Application.config
            config.ranges.floor = filter
            Application.config = config
            dismiss()
            context.startActivity(Intent(context, MainActivity::class.java))
        }
    }
}