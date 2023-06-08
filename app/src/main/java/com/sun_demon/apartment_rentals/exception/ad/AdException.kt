package com.sun_demon.apartment_rentals.exception.ad

open class AdException
    (message: String = "Неверный формат объявления"): IllegalArgumentException(message)

class FloorException
    (message: String = "Номер этажа должен быть больше 0"): AdException(message)
class RoomsNumberException
    (message: String = "Количество комнат должно быть больше 0"): AdException(message)
class TotalAreaException
    (message: String = "Общая площадь должна быть больше 10 м²"): AdException(message)
class FloorsNumberException
    (message: String = "Количество этажей в доме должно быть не меньше этажа квартиры"): AdException(message)
class PhotosException
    (message: String = "Необходимо выбрать все 4 фотографии"): AdException(message)
class PriceException
    (message: String = "Необходимо ввести цену месячной аренды"): AdException(message)