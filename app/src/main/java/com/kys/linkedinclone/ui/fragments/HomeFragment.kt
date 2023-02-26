package com.kys.linkedinclone.ui.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import com.kys.linkedinclone.Constants
import com.kys.linkedinclone.Constants.Companion.INFO
import com.kys.linkedinclone.Constants.Companion.USER_CONSTANT
import com.kys.linkedinclone.R
import com.kys.linkedinclone.adapters.PostAdapter
import com.kys.linkedinclone.models.PostModel
import com.kys.linkedinclone.models.UserModel
import java.util.*

class HomeFragment : Fragment() {

    private lateinit var user: FirebaseUser
    private lateinit var list: MutableList<PostModel>
    private lateinit var adapter: PostAdapter
    private lateinit var postRecyclerView: RecyclerView
    private lateinit var databaseReference: DatabaseReference
    private lateinit var followingList: MutableList<String>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view: View = inflater.inflate(R.layout.fragment_home, container, false)
        postRecyclerView = view.findViewById(R.id.post_recycler_view)
        user = FirebaseAuth.getInstance().currentUser!!
        databaseReference = FirebaseDatabase.getInstance().reference
        databaseReference.keepSynced(true)
        list = ArrayList()
        followingList = ArrayList()
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        adapter = context?.let { PostAdapter(it,list) }!!
        postRecyclerView.setHasFixedSize(true)
        postRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        postRecyclerView.isNestedScrollingEnabled = false

        readPosts()
        getAllUsersId()
    }

    private fun readPosts() {
        databaseReference.child(Constants.ALL_POSTS).addListenerForSingleValueEvent(object: ValueEventListener {
            @SuppressLint("NotifyDataSetChanged")
            override fun onDataChange(snapshot: DataSnapshot) {
                list.clear()
                for (dataSnapshot in snapshot.children) {
                    val model = dataSnapshot.child(INFO).getValue(PostModel::class.java)
                    list.add(model!!)
                }
                list.reverse()
                postRecyclerView.adapter = adapter
                adapter.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {

            }
        })
    }

    private fun getAllUsersId() {
        followingList = ArrayList()
        databaseReference.child(USER_CONSTANT).addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                followingList.clear()
                var model: UserModel?
                snapshot.children.forEach { dataSnapshot ->
                    model = dataSnapshot.child(INFO).getValue(UserModel::class.java)
                    assert(model != null)
                    if (!model?.getKey().equals(user.uid)) {
                        followingList.add(dataSnapshot.key!!)
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {}
        })
    }
}
