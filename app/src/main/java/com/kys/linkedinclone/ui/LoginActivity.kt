package com.kys.linkedinclone.ui

import android.content.ContentValues
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.EditText
import android.widget.RelativeLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.database.*
import com.kys.linkedinclone.Validator
import com.kys.linkedinclone.databinding.ActivityLoginBinding
import com.kys.linkedinclone.models.UserModel

class LoginActivity : AppCompatActivity() {

//    private lateinit var edtEmail: EditText
//    private lateinit var edtPassword: EditText
    private lateinit var googleSignInBtn: RelativeLayout
    private lateinit var facebookSignInBtn: RelativeLayout
    private lateinit var continueBtn: RelativeLayout
    private lateinit var binding: ActivityLoginBinding


    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        auth = FirebaseAuth.getInstance()

        continueBtn = binding.continueBtn
        facebookSignInBtn = binding.facebookSignInBtn
        googleSignInBtn = binding.googleSignInBtn

    }

    override fun onStart() {
        super.onStart()
        if (auth.currentUser != null) {
            startActivity(Intent(this@LoginActivity, HomeActivity::class.java))
            finish()
        }
    }

    override fun onResume() {
        super.onResume()

        binding.joinBtn.setOnClickListener {
            startActivity(Intent(this@LoginActivity, JoinLinkedInActivity::class.java))
        }
        continueBtn.setOnClickListener {
            onContinueBtnClick()
        }
        googleSignInBtn.setOnClickListener {
            onGoogleSignInBtnClick()
        }
        facebookSignInBtn.setOnClickListener {
            onFacebookSignInBtnClick()
        }
    }

    private fun onFacebookSignInBtnClick() {
        TODO("Not yet implemented")
    }

    private fun onGoogleSignInBtnClick() {
    }

    private fun onContinueBtnClick() {
        continueBtn.isClickable = false
        val email:String = binding.edtEmail.text.toString()
        val password:String = binding.edtPassword.text.toString()

        val v = Validator(email,password).result()
        if(v){
            auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        // Sign in success, update UI with the signed-in user's information
                        Log.d(ContentValues.TAG, "signInWithEmail:success")
                        Toast.makeText(this, "Welcome User", Toast.LENGTH_LONG).show()
                        //val user = auth.currentUser
                        val intent = Intent(this, HomeActivity::class.java)
                        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                        startActivity(intent)
                    } else {
                        // If sign in fails, display a message to the user.
                        Log.w(ContentValues.TAG, "signInWithEmail:failure", task.exception)
                        Toast.makeText(baseContext, "" + task.exception, Toast.LENGTH_LONG).show() //Authentication failed.
                        continueBtn.isClickable = true
                    }
                }
        }else{
            Toast.makeText(applicationContext,"Enter all the credentials correctly.",Toast.LENGTH_LONG).show()
            continueBtn.isClickable = true
        }
    }
}