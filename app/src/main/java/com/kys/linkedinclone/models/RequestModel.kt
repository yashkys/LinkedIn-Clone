package com.kys.linkedinclone.models

class RequestModel {
    private var username: String? = null
    private val emailAddress: String? = null
    private val imageUrl: String? = null
    private val key: String? = null
    private val token: String? = null
    private val headline: String? = null

    fun getHeadline(): String? {
        return headline
    }

    fun getLocation(): String? {
        return location
    }

    fun setLocation(location: String?) {
        this.location = location
    }

    private var location: String? = null

    fun getUsername(): String? {
        return username
    }

    fun setUsername(username: String?) {
        this.username = username
    }

    fun getImageUrl(): String? {
        return imageUrl
    }

    fun getKey(): String? {
        return key
    }
}
