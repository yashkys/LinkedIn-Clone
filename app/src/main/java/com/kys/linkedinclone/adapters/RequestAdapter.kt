package com.kys.linkedinclone.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.kys.linkedinclone.Constants.Companion.CONNECTIONS
import com.kys.linkedinclone.Constants.Companion.REQUEST
import com.kys.linkedinclone.Constants.Companion.USER_CONSTANT
import com.kys.linkedinclone.R
import com.kys.linkedinclone.models.RequestModel

class RequestAdapter(val context: Context, private var list: MutableList<RequestModel>) : RecyclerView.Adapter<RequestAdapter.RequestViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RequestViewHolder {
        return RequestViewHolder(LayoutInflater.from(context).inflate(R.layout.card_network_request, parent, false))
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: RequestViewHolder, position: Int) {
        holder.name.text = list[position].getUsername()
        holder.headline.text = list[position].getHeadline()

        Glide.with(context).load(list[position]).into(holder.userImage)
        val user: FirebaseUser = FirebaseAuth.getInstance().currentUser!!
        val databaseReference: DatabaseReference = FirebaseDatabase.getInstance().reference.child(USER_CONSTANT).child(user.uid)

        holder.connectOk.setOnClickListener {
            databaseReference.child(REQUEST).child(list[position].getKey()!!).removeValue()
            databaseReference.child(CONNECTIONS).child(list[position].getKey()!!).setValue(true)
        }

        holder.connectCancel.setOnClickListener {
            databaseReference.child(list[position].getKey()!!).removeValue()
        }
    }

    inner class RequestViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var name:TextView
        var headline:TextView
        var userImage:ImageView
        var connectOk:CardView
        var connectCancel:CardView
        init{
            name=itemView.findViewById(R.id.item_text)
            userImage = itemView.findViewById(R.id.item_image)
            connectOk = itemView.findViewById(R.id.connect_ok)
            connectCancel = itemView.findViewById(R.id.connect_cancel)
            headline = itemView.findViewById(R.id.item_headline)
        }
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

}
