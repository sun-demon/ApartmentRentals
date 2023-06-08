package com.sun_demon.apartment_rentals.watcher

import android.text.Editable
import android.text.TextWatcher

class AfterChangedTextWatcher(
    val onChanged: (s: CharSequence) -> Unit
): TextWatcher {
    override fun afterTextChanged(s: Editable) { return onChanged(s) }
    override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
    override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
}