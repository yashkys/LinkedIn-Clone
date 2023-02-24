package com.kys.linkedinclone.ui

import android.content.ContentValues
import android.content.Intent
import android.content.IntentSender.SendIntentException
import android.os.Bundle
import android.util.Log
import android.widget.RelativeLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.auth.api.identity.BeginSignInRequest
import com.google.android.gms.auth.api.identity.Identity
import com.google.android.gms.auth.api.identity.SignInClient
import com.google.android.gms.auth.api.identity.SignInCredential
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.common.api.CommonStatusCodes
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.*
import com.google.firebase.database.*
import com.kys.linkedinclone.R
import com.kys.linkedinclone.Validator
import com.kys.linkedinclone.databinding.ActivityJoinLinkedInBinding
import com.kys.linkedinclone.models.UserModel

class JoinLinkedInActivity : AppCompatActivity() {

    private lateinit var googleSignUpBtn: RelativeLayout
    private lateinit var facebookSignUpBtn: RelativeLayout
    private lateinit var continueBtn: RelativeLayout
    private lateinit var binding: ActivityJoinLinkedInBinding
    private var auth: FirebaseAuth? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityJoinLinkedInBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        auth = FirebaseAuth.getInstance()

        continueBtn = binding.continueBtn
        facebookSignUpBtn = binding.facebookSignUpBtn
        googleSignUpBtn = binding.googleSignUpBtn
    }

    override fun onResume() {
        super.onResume()

        binding.btnSignIn.setOnClickListener {
            startActivity(Intent(this@JoinLinkedInActivity, LoginActivity::class.java))
        }
        continueBtn.setOnClickListener {
            onContinueBtnClick()
        }
        googleSignUpBtn.setOnClickListener {
            onGoogleSignUpBtnClick()
        }
        facebookSignUpBtn.setOnClickListener {
            onFacebookSignUpBtnClick()
        }
    }

    private fun onFacebookSignUpBtnClick() {
        TODO("Not yet implemented")
    }

    private fun onGoogleSignUpBtnClick() {

    }

    private fun onContinueBtnClick() {
        continueBtn.isClickable = false
        val email:String = binding.edtEmail.text.toString()
        val password:String = binding.edtPassword.text.toString()

        val v = Validator(email,password).result()
        if(v){
            //Signup
            auth!!.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener { task: Task<AuthResult?> ->
                    if (task.isSuccessful) {
//                    progressBar.setVisibility(View.GONE);
                        Toast.makeText(this, "Thank you for joining.", Toast.LENGTH_SHORT).show()
                        val intent = Intent(this, HomeActivity::class.java)
                        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                        startActivity(intent)
                    } else {
//                    progressBar.setVisibility(View.GONE);
                        Log.w(ContentValues.TAG, "signUpWithEmail:failure", task.exception)
                        Toast.makeText(baseContext,  "" + task.exception, Toast.LENGTH_LONG).show() //"Authentication failed.\n" +
                        continueBtn.isClickable = true
                    }
                }
        }else{
            Toast.makeText(applicationContext,"Enter all the credentials correctly.", Toast.LENGTH_LONG).show()
        }
    }
}