package com.kys.linkedinclone.ui

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity
import com.kys.linkedinclone.R

@SuppressLint("CustomSplashScreen")
class SplashScreenActivity : AppCompatActivity() {

    private val mHandler: Handler = Handler()
    private val mRunnable: Runnable = Runnable {
        val intent = Intent(this@SplashScreenActivity, LoginActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
        startActivity(intent)
        finish()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)

        mHandler.postDelayed(mRunnable, 1000)
    }

    override fun onDestroy() {
        super.onDestroy()
        mHandler.removeCallbacks(mRunnable)
    }

    override fun onBackPressed() {
        val i = Intent(Intent.ACTION_MAIN)
        i.addCategory(Intent.CATEGORY_HOME)
        i.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        startActivity(i)
    }
}