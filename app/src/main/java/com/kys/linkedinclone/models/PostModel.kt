package com.kys.linkedinclone.models

class PostModel {

    private lateinit var imgUrl:String
    private lateinit var description:String
    private lateinit var key:String
    private lateinit var userName:String
    private lateinit var userProfile:String


    fun getUsername(): String {
        return userName
    }

    fun setUsername(username: String) {
        userName = username
    }

    fun getUserProfile(): String {
        return userProfile
    }

    fun getImgUrl(): String {
        return imgUrl
    }

    fun getDescription(): String {
        return description
    }

    fun getKey(): String {
        return key
    }
}
