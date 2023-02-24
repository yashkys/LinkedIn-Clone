package com.kys.linkedinclone.models

import android.os.Parcel
import android.os.Parcelable

class UserModel() : Parcelable{

    private lateinit var username:String
    private lateinit var emailAddress:String
    private lateinit var imageUrl:String
    private lateinit var key:String
    private lateinit var token:String
    private lateinit var headline:String
    private lateinit var location:String

    constructor(parcel: Parcel) : this() {
        username = parcel.readString().toString()
        emailAddress = parcel.readString().toString()
        imageUrl = parcel.readString().toString()
        key = parcel.readString().toString()
        token = parcel.readString().toString()
        headline = parcel.readString().toString()
        location = parcel.readString().toString()
    }

    constructor(username: String?,emailAddress: String?,imageUrl: String?,key: String?, token: String?, location: String?,headline: String?,about: String?) : this() {
        this.username = username!!
        this.emailAddress = emailAddress!!
        this.imageUrl = imageUrl!!
        this.key = key!!
        this.token = token!!
        this.location = location!!
        this.headline = headline!!
    }
    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(username)
        parcel.writeString(emailAddress)
        parcel.writeString(imageUrl)
        parcel.writeString(key)
        parcel.writeString(token)
        parcel.writeString(headline)
        parcel.writeString(location)

    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<UserModel> {
        override fun createFromParcel(parcel: Parcel): UserModel {
            return UserModel(parcel)
        }

        override fun newArray(size: Int): Array<UserModel?> {
            return arrayOfNulls(size)
        }
    }

    fun getHeadline(): String { return headline }
    fun getLocation(): String { return location }
    fun setLocation(location: String?) { this.location = location!! }
    fun getUsername(): String { return username }
    fun setUsername(username: String?) { this.username = username!! }
    fun getEmailAddress(): String { return emailAddress }
    fun setEmailAddress(emailAddress: String?) { this.emailAddress = emailAddress!! }
    fun getImageUrl(): String { return imageUrl }
    fun setImageUrl(imageUrl: String?) { this.imageUrl = imageUrl!! }
    fun getKey(): String { return key }
    fun setKey(key: String?) { this.key = key!! }

}