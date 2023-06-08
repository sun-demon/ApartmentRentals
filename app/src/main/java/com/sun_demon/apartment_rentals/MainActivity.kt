package com.sun_demon.apartment_rentals

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.sun_demon.apartment_rentals.ad.Ad
import com.sun_demon.apartment_rentals.ad.District
import com.sun_demon.apartment_rentals.application.Application
import com.sun_demon.apartment_rentals.ad.filters.Sorting
import com.sun_demon.apartment_rentals.tools.Tools.toDateTime
import com.sun_demon.apartment_rentals.tools.Tools.toPhoneFormat
import com.sun_demon.apartment_rentals.tools.Tools.toastMessageLongTime
import com.sun_demon.apartment_rentals.databinding.ActivityMainBinding
import com.sun_demon.apartment_rentals.dialog.DistrictDialog
import com.sun_demon.apartment_rentals.dialog.FloorDialog
import com.sun_demon.apartment_rentals.dialog.PriceDialog
import com.sun_demon.apartment_rentals.dialog.RoomsNumberDialog
import com.sun_demon.apartment_rentals.dialog.SortingDialog
import com.sun_demon.apartment_rentals.dialog.TotalAreaDialog

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var ads: ArrayList<Ad>

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.AppTheme)
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        Application.init(applicationContext)

        initAds()
        initViews()
        initListeners()
    }

    private fun initAds() {
        val config = Application.config
        val allAds = Application.manager.getAds()
        ads = allAds.filter { ad ->
            when (ad.district) {
                District.LENIN -> config.districtFilter.lenin
                District.MOSCOW -> config.districtFilter.moscow
                District.OCTOBER -> config.districtFilter.october
            }
        }.filter { ad ->
            ad.price in config.ranges.price.from..config.ranges.price.to
        }.filter { ad ->
            ad.roomsNumber in config.ranges.roomsNumber.from..config.ranges.roomsNumber.to
        }.filter { ad ->
            ad.totalArea in config.ranges.totalArea.from..config.ranges.totalArea.to
        }.filter { ad ->
            ad.floor in config.ranges.floor.from..config.ranges.floor.to
        }.filter { ad ->
            if (config.ranges.floor.notFirst)
                ad.floor != 1
            else
                true
        }.filter { ad ->
            if (config.ranges.floor.notLast)
                ad.floor != ad.floorsNumber
            else
                true
        }.sortedWith (
            when (config.sorting) {
                Sorting.DATE -> compareByDescending { it.dateTime }
                Sorting.PRICE_IN_ASCENDING -> compareBy{ it.price }
                Sorting.PRICE_IN_DESCENDING -> compareByDescending { it.price }
            }
        ).let { stream ->
            ArrayList(stream)
        }
    }

    private fun initViews() {
        initSortingView()
        initAdsView()
    }

    private fun initSortingView() {
        when (Application.config.sorting) {
            Sorting.DATE -> R.drawable.sort_date
            Sorting.PRICE_IN_ASCENDING -> R.drawable.sort_in_ascending
            Sorting.PRICE_IN_DESCENDING -> R.drawable.sort_in_descending
        }.let { drawableID ->
            binding.sorting.setCompoundDrawablesWithIntrinsicBounds(drawableID, 0, 0, 0)
        }
    }

    private fun initAdsView() {
        ads.forEach { ad ->
            val view = createAdView(ad)
            binding.ads.addView(view, binding.ads.childCount)
        }
    }

    @SuppressLint("InflateParams")
    private fun createAdView(ad: Ad): View {
        val inflater = LayoutInflater.from(this).inflate(R.layout.ad, null)

        arrayOf(R.id.photo0, R.id.photo1, R.id.photo2, R.id.photo3).mapIndexed { i, innerID ->
            inflater.findViewById<ImageView>(innerID)
                .setImageURI(Application.manager.getPhoto(ad.photos[i]))
        }

        inflater.findViewById<TextView>(R.id.price).text = ad.price.toString().replaceFirst("""(\d{2})(\d{3})""", "$1 $2")
        inflater.findViewById<TextView>(R.id.price_per_square).text = String.format("%.1f", ad.pricePerSquare)
        inflater.findViewById<TextView>(R.id.rooms_number).text = ad.roomsNumber.toString()
        inflater.findViewById<TextView>(R.id.total_area).text = String.format("%.1f", ad.totalArea)
        inflater.findViewById<TextView>(R.id.floor).text = ad.floor.toString()
        inflater.findViewById<TextView>(R.id.floors_number).text = ad.floorsNumber.toString()
        inflater.findViewById<TextView>(R.id.district).text = ad.district.string
        inflater.findViewById<TextView>(R.id.date_time).text = ad.dateTime.toDateTime()
        if (!ad.possibleWithChildren)
            inflater.findViewById<ImageView>(R.id.child).visibility = View.GONE

        inflater.findViewById<ImageButton>(R.id.call).setOnClickListener {
            val phone =
                Application.manager
                    .getUsers()
                    .first { user -> user.id == ad.userID }
                    .phone
            val phoneFormated = phone.toPhoneFormat()
            toastMessageLongTime(applicationContext, phoneFormated)
        }

        return inflater
    }

    private fun initListeners() {
        initConfigListeners()
        initActivityListeners()
    }

    private fun initConfigListeners() {
        initDistrictListener()
        initSortingListener()
        initPriceListener()
        initRoomsNumberListener()
        initTotalAreaListener()
        initFloorListener()
    }

    private fun initDistrictListener() {
        binding.district.setOnClickListener {
            val dialog = DistrictDialog(this)
            dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            dialog.show()
        }
    }

    private fun initSortingListener() {
        binding.sorting.setOnClickListener {
            val dialog = SortingDialog(this)
            dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            dialog.show()
        }
    }

    private fun initPriceListener() {
        binding.price.setOnClickListener {
            val dialog = PriceDialog(this)
            dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            dialog.show()
        }
    }

    private fun initRoomsNumberListener() {
        binding.roomsNumber.setOnClickListener {
            val dialog = RoomsNumberDialog(this)
            dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            dialog.show()
        }
    }

    private fun initTotalAreaListener() {
        binding.totalArea.setOnClickListener {
            val dialog = TotalAreaDialog(this)
            dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            dialog.show()
        }
    }

    private fun initFloorListener() {
        binding.floor.setOnClickListener {
            val dialog = FloorDialog(this)
            dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            dialog.show()
        }
    }

    private fun initActivityListeners() {
        initMyAdsListener()
        initProfileListener()
    }

    private fun initMyAdsListener() {
        binding.myAds.setOnClickListener {
            when (Application.user) {
                null -> SignInActivity::class.java
                else -> AdsActivity::class.java
            }.let { nextActivityClass ->
                startActivity(Intent(this, nextActivityClass))
            }
        }
    }

    private fun initProfileListener() {
        binding.profile.setOnClickListener {
            when (Application.user) {
                null -> SignInActivity::class.java
                else -> ProfileActivity::class.java
            }.let { nextActivityClass ->
                startActivity(Intent(this, nextActivityClass))
            }
        }
    }

//    private fun updateSortingView() {
//        binding.sorting
//            .setCompoundDrawablesRelativeWithIntrinsicBounds(
//                when (config.sorting) {
//                    Sorting.DATE -> R.drawable.sort_date
//                    Sorting.PRICE_IN_ASCENDING -> R.drawable.sort_in_ascending
//                    Sorting.PRICE_IN_DESCENDING -> R.drawable.sort_in_descending
//                }, 0, 0, 0
//            )
//    }
//
//    private fun updateView() {
//        updateAdsView()
//        updateSortingView()
//    }
}
