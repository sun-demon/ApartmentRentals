package com.sun_demon.apartment_rentals

import org.junit.Assert.*
import org.junit.Test
import java.util.regex.Pattern

class RegexUnitTest {
    @Test
    fun matches_isCorrect() {
        assertFalse("""^(?=.*\d)(?=.*\w)(?!.*\s).{8,20}$""".toRegex() matches "123ws dfewwsd")
    }
}