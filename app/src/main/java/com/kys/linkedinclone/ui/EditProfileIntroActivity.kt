package com.kys.linkedinclone.ui

import android.os.Bundle
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import com.kys.linkedinclone.Constants.Companion.USER_CONSTANT
import com.kys.linkedinclone.databinding.ActivityEditProfileIntroBinding

class EditProfileIntroActivity: AppCompatActivity() {

    private lateinit var binding: ActivityEditProfileIntroBinding

    private lateinit var editTextFirstName: EditText
    private lateinit var editTextLastName:EditText
    private lateinit var editTextHeadline:EditText
    private lateinit var editTextPosition:EditText
    private lateinit var editTextEducation:EditText
    private lateinit var editTextLocation:EditText
    private lateinit var stringUserName: String
    private lateinit var stringUserImgUrl:String
    private lateinit var stringUserLocation:String
    private lateinit var ref: DatabaseReference
    private lateinit var user: FirebaseUser
    private lateinit var saveBtn: TextView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditProfileIntroBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        user = FirebaseAuth.getInstance().currentUser!!
        ref = FirebaseDatabase.getInstance().reference.child(USER_CONSTANT).child(user.uid)
        editTextFirstName = binding.editFirstName
        editTextLastName = binding.editLastName
        editTextHeadline = binding.editHeadline
        editTextPosition = binding.editPosition
        editTextEducation = binding.editEducation
        editTextLocation = binding.editLocation
        saveBtn = binding.saveBtn


        // Get Data From Activity
        val intent = intent
        stringUserName = intent.getStringExtra("user_name")!!
        stringUserImgUrl = intent.getStringExtra("user_imgUrl")!!
        stringUserLocation = intent.getStringExtra("user_location")!!
        val split = stringUserName.split(" ".toRegex()).dropLastWhile { it.isEmpty() }
            .toTypedArray()
        editTextFirstName.setText(split[0])
        editTextLastName.setText(split[1])
        editTextLocation.setText(stringUserLocation)


        saveBtn.setOnClickListener {
            ref.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val map: MutableMap<String, Any> =
                        HashMap()
                    map["education"] = editTextEducation.text.toString()
                }

                override fun onCancelled(error: DatabaseError) {}
            })
        }
    }
}