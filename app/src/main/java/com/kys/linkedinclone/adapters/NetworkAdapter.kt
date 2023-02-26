package com.kys.linkedinclone.adapters

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.kys.linkedinclone.R
import com.kys.linkedinclone.models.UserModel
import com.kys.linkedinclone.ui.CustomUserActivity

class NetworkAdapter(val context: Context, private var list: MutableList<UserModel>): RecyclerView.Adapter<NetworkAdapter.NetworkViewHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): NetworkAdapter.NetworkViewHolder {
        return NetworkViewHolder(LayoutInflater.from(context).inflate(R.layout.card_network, parent, false))
    }

    override fun onBindViewHolder(holder: NetworkAdapter.NetworkViewHolder, position: Int) {
        holder.name.text = list[position].getUsername()
        holder.headline.text = list[position].getHeadline()

        Glide.with(context).load(list[position].getImageUrl()).into(holder.userImage)

        holder.itemView.setOnClickListener {
            val intent:Intent = Intent (context, CustomUserActivity::class.java)
            intent.putExtra("user_data", list[position])
            context.startActivity(intent)
        }

    }

    override fun getItemCount(): Int {
        return list.size
    }

    inner class NetworkViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var name: TextView
        var headline:TextView
        var userImage: ImageView

        init{
            name = itemView.findViewById(R.id.txt_name)
            userImage = itemView.findViewById(R.id.profileImg)
            headline = itemView.findViewById(R.id.text_headline)
        }
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun setHasStableIds(hasStableIds: Boolean) {
        super.setHasStableIds(hasStableIds)
    }

}
