package com.kys.linkedinclone.ui

import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.kys.linkedinclone.Constants.Companion.REQUEST
import com.kys.linkedinclone.Constants.Companion.USER_CONSTANT
import com.kys.linkedinclone.R
import com.kys.linkedinclone.databinding.ActivityCustomUserBinding
import com.kys.linkedinclone.models.UserModel
import de.hdodenhof.circleimageview.CircleImageView

class CustomUserActivity: AppCompatActivity() {

    private lateinit var binding: ActivityCustomUserBinding

    private lateinit var profileImg: CircleImageView
    private lateinit var txtName: TextView
    private lateinit var txtTitle:TextView
    private lateinit var txtLocation:TextView
    private lateinit var itemSearchInput:TextView
    private lateinit var userEmail:TextView
    private lateinit var profileLink:TextView
    private lateinit var connectBtn: CardView
    lateinit var database: DatabaseReference
    lateinit var auth: FirebaseAuth
    lateinit var user: FirebaseUser
    private lateinit var backBtn: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCustomUserBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        auth = FirebaseAuth.getInstance()
        user = auth.currentUser!!
        database = FirebaseDatabase.getInstance().reference


        // Get Data From Adapter
        val intent = intent
        val userModel: UserModel = intent.getParcelableExtra("user_data")!!
        txtName = binding.txtName
        txtTitle = binding.txtHeadline
        txtLocation = binding.txtLocation
        profileImg = binding.profileImg
        itemSearchInput = binding.itemSearchInput
        connectBtn = binding.connectBtn
        userEmail = binding.userEmail
        backBtn = binding.btnBack
        profileLink = binding.profileLink

        backBtn.setOnClickListener { finish() }

        itemSearchInput.text = userModel.getUsername()
        txtName.text = userModel.getUsername()
        txtLocation.text = userModel.getLocation()
        userEmail.text = userModel.getEmailAddress()
        txtTitle.text = userModel.getHeadline()
        profileLink.text = String.format(
            "%s%s%s",
            "https://www.linkedin.com/in/",
            userModel.getUsername(),
            "-a785i1b7/"
        )
        Glide.with(this).load(userModel.getImageUrl()).into(profileImg)

        // Send Connection Request
        connectBtn.setOnClickListener {
            database.child(USER_CONSTANT).child(userModel.getKey()).child(REQUEST).child(user.uid)
                .setValue(true)
            connectBtn.setCardBackgroundColor(ContextCompat.getColor(this, R.color.gray))
            connectBtn.isEnabled = false
        }
    }


}
