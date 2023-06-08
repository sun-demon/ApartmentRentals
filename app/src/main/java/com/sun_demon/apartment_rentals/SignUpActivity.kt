package com.sun_demon.apartment_rentals

import android.content.Intent
import android.os.Bundle
import android.view.KeyEvent
import androidx.appcompat.app.AppCompatActivity
import com.sun_demon.apartment_rentals.application.Application
import com.sun_demon.apartment_rentals.tools.Tools
import com.sun_demon.apartment_rentals.tools.Tools.checkPassword
import com.sun_demon.apartment_rentals.tools.Tools.checkUser
import com.sun_demon.apartment_rentals.tools.Tools.isPhoneReserved
import com.sun_demon.apartment_rentals.tools.Tools.toPhoneDigits
import com.sun_demon.apartment_rentals.user.User
import com.sun_demon.apartment_rentals.databinding.ActivitySingUpBinding
import com.sun_demon.apartment_rentals.exception.user.PasswordException
import com.sun_demon.apartment_rentals.exception.user.PhoneException
import com.sun_demon.apartment_rentals.exception.user.PhoneReservedException
import com.sun_demon.apartment_rentals.exception.user.RepeatPasswordException
import com.sun_demon.apartment_rentals.exception.user.UserException
import com.sun_demon.apartment_rentals.tools.Tools.toBad
import com.sun_demon.apartment_rentals.tools.Tools.toFun
import com.sun_demon.apartment_rentals.tools.Tools.toNormal
import com.sun_demon.apartment_rentals.watcher.AfterChangedTextWatcher

class SignUpActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySingUpBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySingUpBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initListeners()
    }

    private fun initListeners() {
        initPhoneListener()
        initPasswordListener()
        initRepeatPasswordListener()

        initBackListener()
        initSignUpListener()
        initSignInListener()
    }

    private fun initPhoneListener() {
        binding.phone.addTextChangedListener(AfterChangedTextWatcher {
            binding.phone.let { view ->
                val phone = view.text.toString().toPhoneDigits()
                if (Tools.isPhoneValid(phone)) {
                    if (isPhoneReserved(phone))
                        toBad(view, PhoneReservedException().message!!)
                    else
                        toFun(view)
                } else {
                    toNormal(view)
                }
            }
        })
    }

    private fun initPasswordListener() {
        binding.password.addTextChangedListener(AfterChangedTextWatcher {
            val repeatPassword = binding.repeatPassword.text.toString()
            binding.password.let { view ->
                val password = view.text.toString()
                try {
                    checkPassword(password)
                    if (password == repeatPassword) {
                        toFun(view)
                        toFun(binding.repeatPassword)
                    } else {
                        toNormal(view)
                        binding.repeatPassword.error = RepeatPasswordException().message
                        toNormal(binding.repeatPassword)
                    }
                } catch (exc: PasswordException) {
                    toNormal(view)
                    view.error = exc.message
                    toNormal(binding.repeatPassword)
                }
            }
        })
    }

    private fun initRepeatPasswordListener() {
        binding.repeatPassword.addTextChangedListener(AfterChangedTextWatcher {
            val password = binding.password.text.toString()
            binding.repeatPassword.let { view ->
                val repeatPassword = view.text.toString()
                try {
                    checkPassword(repeatPassword)
                    if (password == repeatPassword) {
                        toFun(view)
                        toFun(binding.password)
                    } else
                        throw RepeatPasswordException()
                } catch (exc: PasswordException) {
                    toNormal(view)
                    view.error = RepeatPasswordException().message
                    toNormal(binding.password)
                }
            }
        })
    }

    private fun initBackListener() {
        binding.back.setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
        }
    }

    private fun initSignUpListener() {
        binding.signUp.setOnClickListener {
            val phone = binding.phone.text.toString().toPhoneDigits()
            val password = binding.password.text.toString()
            val repeatPassword = binding.repeatPassword.text.toString()

            try {
                checkUser(phone = phone, password = password)
                if (password != repeatPassword)
                    throw RepeatPasswordException()
                if (isPhoneReserved(phone))
                    throw PhoneReservedException()
            } catch (exc: UserException) {
                when (exc) {
                    is PhoneException -> toBad(binding.phone, exc.message!!)
                    is PasswordException -> toBad(binding.password, exc.message!!)
                }
                return@setOnClickListener
            }

            val user = User(phone = phone, password = password)

            user.id = Application.manager.insertUser(user)

            Application.user = user
            startActivity(Intent(this, MainActivity::class.java))
        }
    }

    private fun initSignInListener() {
        binding.signIn.setOnClickListener {
            startActivity(Intent(this, SignInActivity::class.java))
        }
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        return if (keyCode == KeyEvent.KEYCODE_BACK)
            binding.back.callOnClick()
        else
            super.onKeyDown(keyCode, event)
    }
}