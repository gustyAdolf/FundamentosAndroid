package com.simplegapps.fundamentosandroid.common

import android.text.Editable
import android.text.TextWatcher

class TextChangedWatcher(private val onTextChanged: (newText: String) -> Unit): TextWatcher {
    override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

    }

    override fun afterTextChanged(p0: Editable?) {

    }

    override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
        p0.toString().let(onTextChanged)
    }
}