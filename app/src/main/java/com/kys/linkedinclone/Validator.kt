package com.kys.linkedinclone

import android.text.TextUtils
import android.util.Patterns
import java.util.regex.Matcher
import java.util.regex.Pattern


class Validator(private val email_phone:CharSequence?, private val password:CharSequence?) {
    fun result():Boolean = isEmailValid() && isPasswordValid()
    private fun isEmailValid(): Boolean {
        return if (TextUtils.isEmpty(email_phone)) {
            false
        } else {
            Patterns.EMAIL_ADDRESS.matcher(email_phone!!).matches()
        }
    }
//    private fun isPhoneNumberValid():Boolean {
//        val number = email_phone.toString()
//        return android.util.Patterns.PHONE.matcher(number).matches();
//    }
//    private fun isEmailOrPhoneValid():Boolean {
//        return (isEmailValid() || isPhoneNumberValid())
//    }
    private fun isPasswordValid(): Boolean {
        val pattern: Pattern
        val PASSWORD_PATTERN = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{4,}$"
        pattern = Pattern.compile(PASSWORD_PATTERN)
        val matcher: Matcher? = password?.let { pattern.matcher(it) }
        return matcher!!.matches()
    }

}