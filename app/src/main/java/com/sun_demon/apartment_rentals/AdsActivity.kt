package com.sun_demon.apartment_rentals

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import com.sun_demon.apartment_rentals.ad.Ad
import com.sun_demon.apartment_rentals.application.Application
import com.sun_demon.apartment_rentals.tools.Tools.toDateTime
import com.sun_demon.apartment_rentals.databinding.ActivityAdsBinding

class AdsActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAdsBinding
    private lateinit var ads: ArrayList<Ad>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAdsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initMyAds()
        initMyAdsView()

        binding.createAd.setOnClickListener {
            startActivity(Intent(this, AdEditorActivity::class.java))
        }

        binding.main.setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
        }

        binding.profile.setOnClickListener {
            startActivity(Intent(this, ProfileActivity::class.java))
        }
    }

    private fun initMyAds() {
        Application.manager.getAds().let { allAds ->
            ads = allAds.filter { ad ->
                ad.userID == Application.user!!.id
            }.sortedWith(
                compareByDescending { it.dateTime }
            ).let { stream ->
                ArrayList(stream)
            }
        }
    }

    private fun initMyAdsView() {
        ads.forEach { ad ->
            val view = createMyAdView(ad)
            binding.ads.addView(view, binding.ads.childCount)
        }
    }

    @SuppressLint("InflateParams")
    private fun createMyAdView(ad: Ad): View {
        val inflater = LayoutInflater.from(this).inflate(R.layout.row_my_ad, null)

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

        inflater.findViewById<ImageButton>(R.id.edit).setOnClickListener {
            val intent = Intent(this, AdEditorActivity::class.java)
            intent.putExtra("adID", ad.id)
            startActivity(intent)
        }

        return inflater
    }
}