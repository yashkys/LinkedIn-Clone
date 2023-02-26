package com.kys.linkedinclone.ui

import android.content.Intent
import android.os.Bundle
import android.widget.EditText
import android.widget.FrameLayout
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.kys.linkedinclone.Constants
import com.kys.linkedinclone.R
import com.kys.linkedinclone.databinding.ActivityLocationBinding

class LocationActivity : AppCompatActivity() {


    private lateinit var binding: ActivityLocationBinding

    private lateinit var editRegion: EditText
    private lateinit var editHeadline: EditText
    private lateinit var continueBtn: FrameLayout
    private lateinit var auth: FirebaseAuth
    private lateinit var databaseReference: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLocationBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        editRegion = findViewById(R.id.editRegion)
        editHeadline = findViewById(R.id.edit_headline)
        continueBtn = findViewById(R.id.continue_btn)
        auth = FirebaseAuth.getInstance()
        databaseReference = FirebaseDatabase.getInstance().reference.child(Constants.USER_CONSTANT)

        //Continue Button
        continueBtn.setOnClickListener {
            val map: MutableMap<String, Any> =
                HashMap()
            map["location"] = editRegion.text.toString()
            map["headline"] = editHeadline.text.toString()
            databaseReference.child(auth.currentUser!!.uid).child(Constants.INFO)
                .updateChildren(map)
                .addOnCompleteListener {
                    startActivity(Intent(this@LocationActivity, HomeActivity::class.java))
                    finish()
                }
        }
    }
}
