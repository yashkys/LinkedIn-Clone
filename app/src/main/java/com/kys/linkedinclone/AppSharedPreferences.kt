package com.kys.linkedinclone

import android.content.Context
import android.content.SharedPreferences

class AppSharedPreferences(context: Context) {

    private var sharedPreference: SharedPreferences? = null

    private var editor: SharedPreferences.Editor? = null

    init{
        val Pref_Name = "Login_ID"
        sharedPreference = context.getSharedPreferences(Pref_Name, Context.MODE_PRIVATE)
        editor = sharedPreference?.edit()
        editor?.apply()
    }

    private var userName:String = "userName"
    private var imgUrl: String = "imageUrl"

    fun setUsername(username: String?) {
        editor!!.putString(username, username)
        editor!!.apply()
    }

    fun getUserName(): String? {
        return sharedPreference!!.getString(this.userName, null)
    }

    fun setImgUrl(imgUrl: String?) {
        editor!!.putString(this.imgUrl, imgUrl)
        editor!!.apply()
    }

    fun getImgUrl(): String? {
        return sharedPreference!!.getString(imgUrl, null)
    }


}
