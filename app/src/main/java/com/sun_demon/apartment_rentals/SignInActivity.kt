package com.sun_demon.apartment_rentals

import android.content.Intent
import android.os.Bundle
import android.view.KeyEvent
import androidx.appcompat.app.AppCompatActivity
import com.sun_demon.apartment_rentals.application.Application
import com.sun_demon.apartment_rentals.tools.Tools.toastMessageLongTime
import com.sun_demon.apartment_rentals.tools.Tools.checkPassword
import com.sun_demon.apartment_rentals.tools.Tools.checkUser
import com.sun_demon.apartment_rentals.tools.Tools.isPhoneReserved
import com.sun_demon.apartment_rentals.tools.Tools.isPhoneValid
import com.sun_demon.apartment_rentals.tools.Tools.toPhoneDigits
import com.sun_demon.apartment_rentals.databinding.ActivitySignInBinding
import com.sun_demon.apartment_rentals.exception.user.PasswordException
import com.sun_demon.apartment_rentals.exception.user.PhoneException
import com.sun_demon.apartment_rentals.exception.user.PhoneNotReservedException
import com.sun_demon.apartment_rentals.exception.user.UserException
import com.sun_demon.apartment_rentals.tools.Tools.toBad
import com.sun_demon.apartment_rentals.tools.Tools.toFun
import com.sun_demon.apartment_rentals.tools.Tools.toNormal
import com.sun_demon.apartment_rentals.watcher.AfterChangedTextWatcher

class SignInActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySignInBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignInBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initListeners()
    }

    private fun initListeners() {
        initPhoneListener()
        initPasswordListener()

        initBackListener()
        initSignInListener()
        initSignUpListener()
    }

    private fun initPhoneListener() {
        binding.phone.addTextChangedListener(AfterChangedTextWatcher {
            binding.phone.let { view ->
                val phone = view.text.toString().toPhoneDigits()
                if (isPhoneValid(phone) && isPhoneReserved(phone))
                    toFun(view)
                else
                    toNormal(view)
            }
        })
    }

    private fun initPasswordListener() {
        binding.password.addTextChangedListener(AfterChangedTextWatcher {
            binding.password.let { view ->
                val password = view.text.toString()
                toNormal(view)
                try {
                    checkPassword(password)
                } catch (exc: PasswordException) {
                    view.error = exc.message
                }
            }
        })
    }

    private fun initBackListener() {
        binding.back.setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
        }
    }

    private fun initSignInListener() {
        binding.signIn.setOnClickListener {
            val phone = binding.phone.text.toString().toPhoneDigits()
            val password = binding.password.text.toString()

            try {
                checkUser(phone = phone, password = password)
                if (!isPhoneReserved(phone))
                    throw PhoneNotReservedException()
            } catch (exc: UserException) {
                when (exc) {
                    is PhoneException -> toBad(binding.phone, exc.message!!)
                    else -> toBad(binding.password, exc.message!!)
                }
                toastMessageLongTime(this, exc.message!!)
                return@setOnClickListener
            }

            val user =
                Application.manager
                    .getUsers()
                    .first { user -> user.phone == phone }

            if (user.password != password) {
                val message = PasswordException().message!!
                toBad(binding.password, message)
                toastMessageLongTime(this, message)
                return@setOnClickListener
            }

            Application.user = user
            startActivity(Intent(this, MainActivity::class.java))
        }
    }

    private fun initSignUpListener() {
        binding.signUp.setOnClickListener {
            startActivity(Intent(this, SignUpActivity::class.java))
        }
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        return if (keyCode == KeyEvent.KEYCODE_BACK)
            binding.back.callOnClick()
        else
            super.onKeyDown(keyCode, event)
    }
}