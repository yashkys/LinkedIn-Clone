package com.kys.linkedinclone.ui

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import com.kys.linkedinclone.Constants.Companion.INFO
import com.kys.linkedinclone.Constants.Companion.USER_CONSTANT
import com.kys.linkedinclone.databinding.ActivityProfileBinding
import com.kys.linkedinclone.models.UserModel
import de.hdodenhof.circleimageview.CircleImageView

class ProfileActivity: AppCompatActivity() {
    private lateinit var imgEditAbout: ImageView
    private lateinit var imgEditProfile:ImageView
    private lateinit var btnBack:ImageView
    private lateinit var editAboutLayout: RelativeLayout
    lateinit var model: UserModel
    lateinit var name: TextView
    lateinit var location:TextView
    lateinit var aboutTxt:TextView
    lateinit var headlineTxt:TextView
    lateinit var profileImageView: CircleImageView
    lateinit var userRef: DatabaseReference
    lateinit var user: FirebaseUser
    lateinit var connectionsTxt: TextView
    lateinit var editTextAbout: EditText
    lateinit var searchInput:EditText
    private lateinit var saveAboutBtn: CardView
    private lateinit var binding: ActivityProfileBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        user = FirebaseAuth.getInstance().currentUser!!
        userRef = FirebaseDatabase.getInstance().reference.child(USER_CONSTANT).child(user.uid)
        imgEditAbout = binding.imgEdit
        saveAboutBtn = binding.saveBtn
        editTextAbout = binding.aboutEdittext
        aboutTxt = binding.aboutTxt
        connectionsTxt = binding.connections
        imgEditProfile = binding.editProfile
        editAboutLayout = binding.editAboutLayout
        name = binding.txtName
        headlineTxt = binding.headlineTxt
        location = binding.txtLocation
        profileImageView = binding.profileImg
        btnBack = binding.btnBack
        searchInput = binding.itemSearchInput
        model = UserModel()


        // Back Button
        btnBack.setOnClickListener { onBackPressed() }

        //Get Data from Firebase
        userRef.addValueEventListener(object : ValueEventListener {
            @SuppressLint("SetTextI18n")
            override fun onDataChange(snapshot: DataSnapshot) {
                model = snapshot.child(INFO).getValue(UserModel::class.java)!!
                name.text = model.getUsername()
                location.text = model.getLocation()
                headlineTxt.text = model.getHeadline()
                searchInput.setText(model.getUsername())
                Glide.with(applicationContext).load(model.getImageUrl()).into(profileImageView)
                if (snapshot.child("Data").child("about").exists()) {
                    aboutTxt.text = snapshot.child("Data").child("about").getValue(
                        String::class.java
                    )
                    aboutTxt.setLines(3)
                } else {
                    aboutTxt.text = String.format("%s", "Add a summary about yourself")
                    aboutTxt.setLines(1)
                }
                connectionsTxt.text = snapshot.child("Connections").childrenCount.toString() + " connections"
            }

            override fun onCancelled(error: DatabaseError) {}
        })


        // Save Button
        saveAboutBtn.setOnClickListener {
            userRef.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    userRef.child("Data").child("about")
                        .setValue(editTextAbout.text.toString())
                        .addOnCompleteListener {
                            if (editAboutLayout.visibility == View.VISIBLE) {
                                startActivity(
                                    Intent(
                                        this@ProfileActivity,
                                        ProfileActivity::class.java
                                    )
                                )
                            }
                        }
                }

                override fun onCancelled(error: DatabaseError) {}
            })
        }


        // Edit Profile
        imgEditAbout.setOnClickListener {
            editAboutLayout.visibility = View.VISIBLE
        }
        imgEditProfile.setOnClickListener {
            val intent = Intent(
                this@ProfileActivity,
                EditProfileIntroActivity::class.java
            )
            intent.putExtra("user_name", model.getUsername())
            intent.putExtra("user_imgUrl", model.getImageUrl())
            intent.putExtra("user_location", model.getLocation())
            startActivity(intent)
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        if (editAboutLayout.visibility == View.VISIBLE) {
            startActivity(Intent(this@ProfileActivity, ProfileActivity::class.java))
        }
    }
}