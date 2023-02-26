package com.kys.linkedinclone.ui

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import com.kys.linkedinclone.Constants
import com.kys.linkedinclone.adapters.MessageUserAdapter
import com.kys.linkedinclone.databinding.ActivityMessageUsersBinding
import com.kys.linkedinclone.models.UserModel
import java.util.*

class MessageUsersActivity: AppCompatActivity() {
    lateinit var list: MutableList<UserModel?>
    private lateinit var databaseReference: DatabaseReference
    lateinit var user: FirebaseUser
    lateinit var adapter: MessageUserAdapter
    lateinit var recyclerView: RecyclerView
    private lateinit var binding: ActivityMessageUsersBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMessageUsersBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        recyclerView = binding.userRecycler //findViewById(R.id.user_recycler)
        user = FirebaseAuth.getInstance().currentUser!!
        databaseReference = FirebaseDatabase.getInstance().reference
        list = ArrayList<UserModel?>()

        //User RecyclerView
        recyclerView.setHasFixedSize(true)
        recyclerView.layoutManager = LinearLayoutManager(this)

        //Function
        readUsers()
    }

    //----------------------------------Read Users--------------------------------//
    private fun readUsers() {
        databaseReference.child(Constants.USER_CONSTANT).addValueEventListener(object : ValueEventListener {
            @SuppressLint("NotifyDataSetChanged")
            override fun onDataChange(snapshot: DataSnapshot) {
                list.clear()
                for (dataSnapshot in snapshot.children) {
                    val model: UserModel? =
                        dataSnapshot.child(Constants.INFO).getValue(UserModel::class.java)
                    if (!model?.getKey().equals(user.uid)) {
                        list.add(model)
                    }
                }
                list.reverse()
                adapter = MessageUserAdapter(this@MessageUsersActivity, list)
                recyclerView.adapter = adapter
                adapter.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {}
        })
    }
}