package com.sun_demon.apartment_rentals.dialog

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Window
import com.sun_demon.apartment_rentals.MainActivity
import com.sun_demon.apartment_rentals.application.Application
import com.sun_demon.apartment_rentals.ad.filters.Sorting
import com.sun_demon.apartment_rentals.databinding.DialogSortingBinding

class SortingDialog(
    context: Context
) : Dialog(context) {
    private lateinit var binding: DialogSortingBinding
    private lateinit var sorting: Sorting

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        binding = DialogSortingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        sorting = Application.config.sorting

        initViews()
        initListeners()
    }

    private fun initViews() {
        when (sorting) {
            Sorting.DATE -> binding.byDefault
            Sorting.PRICE_IN_ASCENDING -> binding.byPriceAscending
            Sorting.PRICE_IN_DESCENDING -> binding.byPriceDescending
        }.isChecked = true
    }

    private fun initListeners() {
        initByDateListener()
        initByPriceAscendingListener()
        initByPriceDescendingListener()

        initBackListener()
        initResetListener()
        initApplyListener()
    }

    private fun initByDateListener() {
        binding.byDefault.setOnClickListener {
            sorting = Sorting.DATE
        }
    }

    private fun initByPriceAscendingListener() {
        binding.byPriceAscending.setOnClickListener {
            sorting = Sorting.PRICE_IN_ASCENDING
        }
    }

    private fun initByPriceDescendingListener() {
        binding.byPriceDescending.setOnClickListener {
            sorting = Sorting.PRICE_IN_DESCENDING
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
            config.sorting = Sorting.DATE
            Application.config = config
            dismiss()
            context.startActivity(Intent(context, MainActivity::class.java))
        }
    }

    private fun initApplyListener() {
        binding.apply.setOnClickListener {
            val config = Application.config
            config.sorting = sorting
            Application.config = config
            dismiss()
            context.startActivity(Intent(context, MainActivity::class.java))
        }
    }
}