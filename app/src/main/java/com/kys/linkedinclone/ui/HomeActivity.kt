package com.kys.linkedinclone.ui

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import com.kys.linkedinclone.AppSharedPreferences
import com.kys.linkedinclone.Constants
import com.kys.linkedinclone.R
import com.kys.linkedinclone.UniversalImageLoaderClass
import com.kys.linkedinclone.databinding.ActivityHomeBinding
import com.kys.linkedinclone.models.UserModel
import com.kys.linkedinclone.ui.fragments.HomeFragment
import com.kys.linkedinclone.ui.fragments.JobsFragment
import com.kys.linkedinclone.ui.fragments.NetworkFragment
import com.kys.linkedinclone.ui.fragments.NotificationFragment
import com.nostra13.universalimageloader.core.ImageLoader

class HomeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHomeBinding

    private lateinit var drawerLayout: DrawerLayout
    private lateinit var profileImg:ImageView
    private lateinit var messageBtn:ImageView
    private lateinit var navImg:ImageView
    private lateinit var navCloseImg:ImageView
    private lateinit var viewProfileBtn: TextView
    private lateinit var navName: TextView
    private lateinit var bottomNavigationView: BottomNavigationView
    private lateinit var mNavigationView: NavigationView
    private var selectedFragment: Fragment?=null
    private lateinit var appSharedPreferences: AppSharedPreferences
    private lateinit var userReference: DatabaseReference
    private lateinit var user: FirebaseUser
    private lateinit var model:UserModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        appSharedPreferences = AppSharedPreferences(this)
        user = FirebaseAuth.getInstance().currentUser!!
        userReference = FirebaseDatabase.getInstance().reference.child(Constants.USER_CONSTANT).child(user.uid)
        drawerLayout = binding.drawerLayout
        profileImg = drawerLayout.findViewById(R.id.drawer_user_img)
        messageBtn = findViewById(R.id.message_btn)
        mNavigationView = binding.navView
        bottomNavigationView = binding.bottomNavigationBar

        val universalImageLoaderClass = UniversalImageLoaderClass(this)
        ImageLoader.getInstance().init(universalImageLoaderClass.getConfig())

        val header = mNavigationView.getHeaderView(0)
        navName = header.findViewById(R.id.user_name)
        navImg = header.findViewById(R.id.user_img)
        navCloseImg = header.findViewById(R.id.close_img)

        viewProfileBtn = header.findViewById(R.id.view_profile_btn)
    }

    override fun onResume() {
        super.onResume()

        Glide.with(this).load(appSharedPreferences.getImgUrl()).into(profileImg)
        Glide.with(this).load(appSharedPreferences.getImgUrl()).into(navImg)
        navName.text = appSharedPreferences.getUserName()

        profileImg.setOnClickListener {
            if (!drawerLayout.isDrawerOpen(GravityCompat.START))
                drawerLayout.openDrawer(GravityCompat.START)
            else drawerLayout.closeDrawer(GravityCompat.END)
        }

        messageBtn.setOnClickListener {
            val intent = Intent(this@HomeActivity, MessageUsersActivity::class.java)
            startActivity(intent)
        }

        viewProfileBtn.setOnClickListener{
            startActivity(Intent(this@HomeActivity, ProfileActivity::class.java))
        }

        navCloseImg.setOnClickListener {
            if(drawerLayout.isDrawerOpen((GravityCompat.START)))
                drawerLayout.closeDrawer(GravityCompat.START)
        }


        // Get Data from Firebase
        userReference.child("Info").addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                model = snapshot.getValue(UserModel::class.java)!!
                appSharedPreferences.setUsername(model.getUsername())
                appSharedPreferences.setImgUrl(model.getImageUrl())
            }
            override fun onCancelled(error: DatabaseError) {}
        })

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.nav_home -> {
                selectedFragment = HomeFragment()
            }
            R.id.nav_network -> {
                selectedFragment = NetworkFragment()
            }
            R.id.nav_post -> {
                selectedFragment = null
                startActivity(Intent(this@HomeActivity, SharePostActivity::class.java))
                overridePendingTransition(R.anim.slide_up, R.anim.slide_down)
            }
            R.id.nav_notification -> {
                selectedFragment = NotificationFragment()
            }
            else -> {
                selectedFragment = JobsFragment()
            }
        }
        selectedFragment?.let {
            supportFragmentManager.beginTransaction()
                .replace(R.id.frame_layout, it)
                .commit()
        }
        return true
    }

//    @Deprecated("Deprecated in Java")
//    override fun onBackPressed() {
//        val mBottomNavigationView: BottomNavigationView = findViewById(R.id.bottom_navigation_bar)
//        if (mBottomNavigationView.selectedItemId == R.id.nav_home) {
//            super.onBackPressed()
//            finish()
//        } else {
//            mBottomNavigationView.selectedItemId = R.id.nav_home
//        }
//    }

}