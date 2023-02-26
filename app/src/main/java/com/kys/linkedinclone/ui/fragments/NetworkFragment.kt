package com.kys.linkedinclone.ui.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import com.kys.linkedinclone.Constants.Companion.INFO
import com.kys.linkedinclone.Constants.Companion.REQUEST
import com.kys.linkedinclone.Constants.Companion.USER_CONSTANT
import com.kys.linkedinclone.adapters.NetworkAdapter
import com.kys.linkedinclone.adapters.RequestAdapter
import com.kys.linkedinclone.databinding.FragmentNetworkBinding
import com.kys.linkedinclone.models.RequestModel
import com.kys.linkedinclone.models.UserModel
import java.util.*

class NetworkFragment : Fragment() {


    lateinit var list: MutableList<RequestModel>
    lateinit var connectionList: MutableList<UserModel>
    lateinit var adapter: NetworkAdapter
    lateinit var requestAdapter: RequestAdapter
    lateinit var recyclerView: RecyclerView
    lateinit var requestRecyclerView:RecyclerView
    private lateinit var ref: DatabaseReference
    lateinit var user: FirebaseUser
    private var requestList: MutableList<String>? = null
    private lateinit var binding: FragmentNetworkBinding


    override fun onCreateView(inflater: LayoutInflater,container: ViewGroup?,savedInstanceState: Bundle?): View {
        binding = FragmentNetworkBinding.inflate(inflater)
        val view = binding.root
        //val view: View = inflater.inflate(R.layout.fragment_network, container, false)
        recyclerView = binding.recyclerNetwork //view.findViewById<RecyclerView>(R.id.recycler_network)
        requestRecyclerView = binding.requestRecyclerView //view.findViewById<RecyclerView>(R.id.request_recyclerView)
        user = FirebaseAuth.getInstance().currentUser!!
        ref = FirebaseDatabase.getInstance().reference
        list = ArrayList()
        requestList = ArrayList()
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //Network RecyclerView
        recyclerView.setHasFixedSize(true)
        recyclerView.layoutManager = GridLayoutManager(requireContext(), 2)
        recyclerView.isNestedScrollingEnabled = false

        //Request RecyclerView
        requestRecyclerView.setHasFixedSize(true)
        requestRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        requestRecyclerView.isNestedScrollingEnabled = false

        //Functions
        readUsers()
        getAllUsersId()
    }

    //--------------------------------Get All Users Id--------------------------------//
    private fun getAllUsersId() {
        requestList = ArrayList()
        val reference =
            FirebaseDatabase.getInstance().reference.child(USER_CONSTANT).child(user.uid)
                .child(REQUEST)
        reference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                (requestList as ArrayList<String>).clear()
                for (dataSnapshot in snapshot.children) {
                    (requestList as ArrayList<String>).add(dataSnapshot.key!!)
                }
                readRequest()
            }

            override fun onCancelled(error: DatabaseError) {}
        })
    }

    //----------------------------------Read Request--------------------------------//
    private fun readRequest() {
        ref.child(USER_CONSTANT).addListenerForSingleValueEvent(object : ValueEventListener {
            @SuppressLint("NotifyDataSetChanged")
            override fun onDataChange(snapshot: DataSnapshot) {
                list.clear()
                for (id in requestList!!) {
                    val model: RequestModel = snapshot.child(id).child(INFO).getValue(RequestModel::class.java)!!
                    list.add(model)
                }
                list.reverse()
                requestAdapter = RequestAdapter(activity!!, list)
                requestRecyclerView.adapter = requestAdapter
                adapter.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {}
        })
    }

    //----------------------------------Read Users--------------------------------//
    private fun readUsers() {
        connectionList = ArrayList()
        ref.child(USER_CONSTANT).addListenerForSingleValueEvent(object : ValueEventListener {
            @SuppressLint("NotifyDataSetChanged")
            override fun onDataChange(snapshot: DataSnapshot) {
                connectionList.clear()
                for (dataSnapshot in snapshot.children) {
                    val model = dataSnapshot.child(INFO).getValue(UserModel::class.java)
                    if (model!!.getKey() != user.uid) {
                        connectionList.add(model)
                    }
                }
                list.reverse()
                adapter = NetworkAdapter(activity!!, connectionList)
                recyclerView.adapter = adapter
                adapter.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {}
        })
    }
}