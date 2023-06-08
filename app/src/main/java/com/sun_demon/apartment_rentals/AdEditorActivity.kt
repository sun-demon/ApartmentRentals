package com.sun_demon.apartment_rentals

import android.app.Activity
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.AdapterView.OnItemSelectedListener
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts.StartActivityForResult
import androidx.appcompat.app.AppCompatActivity
import androidx.core.text.isDigitsOnly
import com.sun_demon.apartment_rentals.ad.Ad
import com.sun_demon.apartment_rentals.ad.District
import com.sun_demon.apartment_rentals.application.Application
import com.sun_demon.apartment_rentals.tools.Tools
import com.sun_demon.apartment_rentals.tools.Tools.checkAd
import com.sun_demon.apartment_rentals.databinding.ActivityAdEditorBinding
import com.sun_demon.apartment_rentals.dialog.Dialog
import com.sun_demon.apartment_rentals.exception.ad.AdException
import com.sun_demon.apartment_rentals.exception.ad.FloorException
import com.sun_demon.apartment_rentals.exception.ad.FloorsNumberException
import com.sun_demon.apartment_rentals.exception.ad.PriceException
import com.sun_demon.apartment_rentals.exception.ad.RoomsNumberException
import com.sun_demon.apartment_rentals.exception.ad.TotalAreaException
import com.sun_demon.apartment_rentals.tools.Tools.toBad
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.lang.NullPointerException

class AdEditorActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAdEditorBinding
    private lateinit var ad: Ad

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAdEditorBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initViews()
        initListeners()
    }

    private fun initViews() {
        ad = if (intent.hasExtra("adID")) {
            val adID = intent.getIntExtra("adID", 0)
            val originalAd = Application.manager.getAds().first { ad -> ad.id == adID }
            Json.decodeFromString(Json.encodeToString(originalAd))
        } else
            Ad(userID = Application.user!!.id)

        initDistrictView()
        initFloorView()
        initRoomsNumberView()
        initTotalAreaView()
        initFloorsNumberView()
        initPossibleWithChildrenView()
        initPhotosView()
        initPriceView()

        initTrashView()
    }

    private fun initDistrictView() {
        val district = ad.district
        when (district) {
            District.LENIN -> 0
            District.MOSCOW -> 1
            District.OCTOBER -> 2
        }.let { position ->
            binding.district.setSelection(position)
        }
    }

    private fun initFloorView() {
        val floor = ad.floor
        binding.floor.setText(
            if (floor == -1)
                ""
            else
                floor.toString()
        )
    }

    private fun initRoomsNumberView() {
        val roomsNumber = ad.roomsNumber
        binding.roomsNumber.setText(
            if (roomsNumber == -1)
                ""
            else
                roomsNumber.toString()
        )
    }

    private fun initTotalAreaView() {
        val totalArea = ad.totalArea
        binding.totalArea.setText(
            if (totalArea < 0)
                ""
            else
                totalArea.toString()
        )
    }

    private fun initFloorsNumberView() {
        val floorsNumber = ad.floorsNumber
        binding.floorsNumber.setText(
            if (floorsNumber == -1)
                ""
            else
                floorsNumber.toString()
        )
    }

    private fun initPossibleWithChildrenView() {
        val possibleWithChildren = ad.possibleWithChildren
        when (possibleWithChildren) {
            false -> binding.notPossibleWithChildren
            true -> binding.possibleWithChildren
        }.isChecked = true
    }

    private fun initPhotosView() {
        val photos = ad.photos
        arrayListOf(
            binding.photo0,
            binding.photo1,
            binding.photo2,
            binding.photo3
        ).forEachIndexed { i, view ->
            if (photos[i] != -1)
                view.setImageURI(Application.manager.getPhoto(photos[i]))
        }
    }

    private fun initPriceView() {
        val price = ad.price
        binding.price.setText(
            if (price == -1)
                ""
            else
                price.toString()
        )
    }

    private fun initTrashView() {
        if (!intent.hasExtra("adID"))
            binding.trash.visibility = View.GONE
    }

    private fun initListeners() {
        initDistrictListener()
        initFloorListener()
        initRoomsNumberListener()
        initTotalAreaListener()
        initFloorsNumberListener()
        initPossibleWithChildrenListener()
        initPhotosListeners()
        initPriceListener()

        initBackListener()
        initDeleteListener()
        initContinueListener()
    }

    private fun initDistrictListener() {
        binding.district.onItemSelectedListener = object: OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {}
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                if (parent != null) {
                    (parent.getChildAt(0) as TextView).setTextColor(Color.WHITE)
                    ad.district = District.values().first { district ->
                        district.string == binding.district.selectedItem.toString()
                    }
                }
            }
        }
    }

    private fun initFloorListener() {
        binding.floor.addTextChangedListener(object : TextWatcher { var before: String = ""
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) { before = s.toString()}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable?) {
                val after = s.toString()
                if (after == "") {
                    binding.floor.setBackgroundResource(R.drawable.button_bad)
                    ad.floor = -1
                    return
                }
                if (after.toInt() > 20)
                    binding.floor.setText(before)
                else
                    ad.floor = after.toInt()
                binding.floor.setBackgroundResource(R.drawable.button_normal)
            }
        })
    }

    private fun initRoomsNumberListener() {
        binding.roomsNumber.addTextChangedListener(object : TextWatcher { var before: String = ""
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) { before = s.toString()}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable?) {
                val after = s.toString()
                if (after == "") {
                    binding.roomsNumber.setBackgroundResource(R.drawable.button_bad)
                    ad.roomsNumber = -1
                    return
                }
                if (after.toInt() > 20)
                    binding.roomsNumber.setText(before)
                else
                    ad.roomsNumber = after.toInt()
                binding.roomsNumber.setBackgroundResource(R.drawable.button_normal)
            }
        })
    }

    private fun initTotalAreaListener() {
        binding.totalArea.addTextChangedListener(object : TextWatcher { var before: String = ""
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) { before = s.toString()}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable?) {
                val after = s.toString()
                if (after == "") {
                    binding.totalArea.setBackgroundResource(R.drawable.button_bad)
                    ad.totalArea = -1.0
                    return
                }
                if (!"""\d{0,2}(\.\d|\.)?""".toRegex().matches(after))
                    binding.totalArea.setText(before)
                else
                    ad.totalArea = after.toDouble()
                binding.totalArea.setBackgroundResource(R.drawable.button_normal)
            }
        })
    }

    private fun initFloorsNumberListener() {
        binding.floorsNumber.addTextChangedListener(object : TextWatcher { var before: String = ""
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) { before = s.toString()}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable?) {
                val after = s.toString()
                if (after == "") {
                    binding.floorsNumber.setBackgroundResource(R.drawable.button_bad)
                    ad.floorsNumber = -1
                    return
                }
                if (after.toInt() > 20)
                    binding.floorsNumber.setText(before)
                else
                    ad.floorsNumber = after.toInt()
                binding.floorsNumber.setBackgroundResource(R.drawable.button_normal)
            }
        })
    }

    private fun initPossibleWithChildrenListener() {
        binding.possibleWithChildren.setOnClickListener {
            ad.possibleWithChildren = true
        }
        binding.notPossibleWithChildren.setOnClickListener {
            ad.possibleWithChildren = false
        }
    }

    private fun initPhotosListeners() {
        arrayListOf(
            binding.photo0,
            binding.photo1,
            binding.photo2,
            binding.photo3
        ).mapIndexed { i, imageButton ->
            imageButton.setOnClickListener {
                intent.putExtra("photo", i)
                val intent = Intent(Intent.ACTION_PICK)
                intent.type = "image/*"
                photoActivityResultLauncher.launch(intent)
            }
        }
    }

    private val photoActivityResultLauncher = registerForActivityResult(StartActivityForResult()) {
        if (it.resultCode == Activity.RESULT_OK && it.data != null) {
            try {
                val i = intent.getIntExtra("photo", -1)
                if (i == -1)
                    throw RuntimeException("Invalid photo number by key \"photo\" in intent")
                val externUri: Uri = it.data!!.data ?: throw NullPointerException("Uri can't be null")
                val oldPhotoID = ad.photos[i]
                Application.manager.deletePhoto(oldPhotoID)
                val photoID = Application.manager.insertPhoto(externUri)
                val appUri = Application.manager.getPhoto(photoID)

                Log.d("PHOTO_CHANGE", "$i-st photo is changed")
                when (i) {
                    0 -> binding.photo0
                    1 -> binding.photo1
                    2 -> binding.photo2
                    else -> binding.photo3
                }.setImageURI(appUri)
                ad.photos[i] = photoID
            } catch (exc: Exception) {
                exc.printStackTrace()
                Log.w("PHOTO_NOT_CHANGE", "Photo not changed, message: \"${exc.message ?: ""}\"")
                Tools.toastMessageLongTime(applicationContext, getString(R.string.bad_getting_image))
            }
        } else {
            Log.w("PHOTO_NOT_CHANGE", "Result of getting is bad or data is null")
            Tools.toastMessageLongTime(applicationContext, getString(R.string.not_get_image))
        }
    }

    private fun initPriceListener() {
        binding.price.addTextChangedListener(object : TextWatcher { var before: String = ""
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) { before = s.toString()}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable?) {
                val after = s.toString()
                if (after == "") {
                    binding.price.setBackgroundResource(R.drawable.button_bad)
                    ad.price = -1
                    return
                }
                if (!after.isDigitsOnly() || after.length > 5)
                    binding.price.setText(before)
                else
                    ad.price = after.toInt()
                binding.price.setBackgroundResource(R.drawable.button_normal)
            }
        })
    }

    private fun initBackListener() {
        binding.back.setOnClickListener {
            if (intent.hasExtra("adID")) {
                val oldAd = Application.manager.getAds().first { it.id == ad.id }
                if (ad.contentEquals(oldAd)) {
                    startActivity(Intent(this, AdsActivity::class.java))
                    return@setOnClickListener
                }
            }
            val dialog = Dialog(this,
                message = getString(R.string.undo_changes),
                onYes = {
                    it.dismiss()
                    startActivity(Intent(this, AdsActivity::class.java))
                    finish()
                },
                onNo = {
                    it.dismiss()
                })
            dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            dialog.show()
        }
    }

    private fun initDeleteListener() {
        binding.trash.setOnClickListener {
            val dialog = Dialog(this,
                message = getString(R.string.message_delete_ad),
                onYes = {
                    if (intent.hasExtra("adID")) {
                        Application.manager.deleteAd(ad.id)
                    }
                    it.dismiss()
                    startActivity(Intent(this, AdsActivity::class.java))
                    finish()
                },
                onNo = {
                    it.dismiss()
                })
            dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            dialog.show()
        }
    }

    private fun initContinueListener() {
        binding.continueTo.setOnClickListener {
            try {
                checkAd(ad)
            } catch (exc: AdException) {
                when (exc) {
                    is FloorException -> binding.floor
                    is RoomsNumberException -> binding.roomsNumber
                    is TotalAreaException -> binding.totalArea
                    is FloorsNumberException -> binding.floorsNumber
                    is PriceException -> binding.price
                    else -> {
                        Tools.toastMessageLongTime(applicationContext, exc.message ?: "")
                        return@setOnClickListener
                    }
                }.let { view -> toBad(view, exc.message ?: "") }
                Tools.toastMessageLongTime(applicationContext, exc.message ?: "")
                return@setOnClickListener
            }
            ad.dateTime = System.currentTimeMillis()
            if (intent.hasExtra("adID"))
                Application.manager.deleteAd(ad.id)
            Application.manager.insertAd(ad)
            startActivity(Intent(this, AdsActivity::class.java))
        }
    }
}