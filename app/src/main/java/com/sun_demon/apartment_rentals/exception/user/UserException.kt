package com.sun_demon.apartment_rentals.exception.user

open class UserException(message: String) : IllegalArgumentException(message)

// User
open class PhoneException
    (message: String = "Ошибка номера телефона")
    : UserException(message)
open class PasswordException
    (message: String = "Ошибка пароля")
    : UserException(message)

// User -> Phone
class PhoneFormatException
    (message: String = "Неверный формат номера телефона")
    : PhoneException(message)
class PhoneReservedException
    (message: String = "Данный номер телефона уже занят")
    : PhoneException(message)
class PhoneNotReservedException
    (message: String = "Данный номер телефона не зарегистрирован")
    : PhoneException(message)

// User -> Password
open class PasswordFormatException
    (message: String = "Неверный формат пароля")
    : PasswordException(message)
class PasswordLengthException
    (message: String = "Длина пароля должна быть от 8 до 40 символов")
    : PasswordException(message)
class PasswordAbcException
    (message: String = "Пароль должени содержать букву латинского алфафита")
    : PasswordException(message)
class PasswordNotDigitException
    (message: String = "Пароль должен содержать цуфру")
    : PasswordException(message)
class PasswordSpaceException
    (message: String = "Пароль не должен содержать пробельные символы")
    : PasswordException(message)
class RepeatPasswordException
    (message: String = "Пароли не совпадают")
    : PasswordException(message)