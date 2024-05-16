package com.example.dopaminemoa.presentation.util

import android.app.Activity
import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.Fragment

object UtilityKeyboard {
    /**
     * Fragment에서 사용할 수 있는 keyboard를 숨겨주는 함수입니다.
     * 아래의 Context.hideKeyboard를 호출하여 처리합니다.
     */
    fun Fragment.hideKeyboard() {
        view?.let { activity?.hideKeyboard(it) }
    }

    fun Context.hideKeyboard(view: View) {
        val inputMethodManager = getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
    }
}