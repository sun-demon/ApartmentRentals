package com.sun_demon.apartment_rentals.dialog

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Window
import com.sun_demon.apartment_rentals.MainActivity
import com.sun_demon.apartment_rentals.application.Application
import com.sun_demon.apartment_rentals.ad.filters.DistrictFilter
import com.sun_demon.apartment_rentals.databinding.DialogDistrictBinding

class DistrictDialog(
    context: Context
) : Dialog(context) {
    private lateinit var binding: DialogDistrictBinding
    private lateinit var filter: DistrictFilter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        binding = DialogDistrictBinding.inflate(layoutInflater)
        setContentView(binding.root)

        filter = Application.config.districtFilter.copy()

        initViews()
        initListeners()
    }

    private fun initViews() {
        initLenin()
        initMoscow()
        initOctober()
    }

    private fun initLenin() {
        binding.lenin.isChecked = filter.lenin
    }

    private fun initMoscow() {
        binding.moscow.isChecked = filter.moscow
    }

    private fun initOctober() {
        binding.october.isChecked = filter.october
    }

    private fun initListeners() {
        initLeninListener()
        initMoscowListener()
        initOctoberListener()

        initBackListener()
        initResetListener()
        initApplyListener()
    }

    private fun initLeninListener() {
        binding.lenin.setOnClickListener {
            filter.lenin = binding.lenin.isChecked
        }
    }

    private fun initMoscowListener() {
        binding.moscow.setOnClickListener {
            filter.moscow = binding.moscow.isChecked
        }
    }

    private fun initOctoberListener() {
        binding.october.setOnClickListener {
            filter.october = binding.october.isChecked
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
            config.districtFilter = DistrictFilter()
            Application.config = config
            dismiss()
            context.startActivity(Intent(context, MainActivity::class.java))
        }
    }

    private fun initApplyListener() {
        binding.apply.setOnClickListener {
            val config = Application.config
            config.districtFilter = filter
            Application.config = config
            dismiss()
            context.startActivity(Intent(context, MainActivity::class.java))
        }
    }
}