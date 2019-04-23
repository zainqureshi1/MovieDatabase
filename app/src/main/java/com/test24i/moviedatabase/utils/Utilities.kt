package com.test24i.moviedatabase.utils

import android.content.Context
import android.support.annotation.StringRes
import android.support.design.widget.Snackbar
import android.view.View
import android.view.inputmethod.InputMethodManager
import com.test24i.moviedatabase.R
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class Utilities {
    object DateUtils {
        private val API_DATE_FORMAT = "yyyy-MM-dd"
        fun dateToString(date: Date): String {
            val dateFormat = SimpleDateFormat(API_DATE_FORMAT, Locale.getDefault())
            return dateFormat.format(date)
        }
    }

}

fun View.hideKeyboard() {
    val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    imm.hideSoftInputFromWindow(windowToken, 0)
}

fun View.showSnackbar(text: String, @StringRes actionResId: Int, listener: (View) -> Unit) {
    Snackbar.make(this, text, Snackbar.LENGTH_LONG).setAction(actionResId, listener).show()
}

fun Context.isTablet() : Boolean {
    return resources.getBoolean(R.bool.isTablet)
}