package com.kys.linkedinclone.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.kys.linkedinclone.R
import com.kys.linkedinclone.models.UserModel

class MessageUserAdapter(val context: Context, val list: MutableList<UserModel?>) :
    RecyclerView.Adapter<MessageUserAdapter.MyViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view: View = LayoutInflater.from(context).inflate(R.layout.card_useritem, parent, false)
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.name.text = list[position]!!.getUsername()
        Glide.with(context).load(list[position]!!.getImageUrl()).into(holder.userImage)
    }

    override fun getItemCount(): Int {
        return list.size
    }


    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var name: TextView
        var userImage: ImageView

        init {
            name = itemView.findViewById<TextView>(R.id.item_text)
            userImage = itemView.findViewById<ImageView>(R.id.item_image)
        }
    }


    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun setHasStableIds(hasStableIds: Boolean) {
        super.setHasStableIds(hasStableIds)
    }
}


