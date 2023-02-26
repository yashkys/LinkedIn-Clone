package com.kys.linkedinclone.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.*
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.github.pgreze.reactions.PopupGravity
import com.github.pgreze.reactions.ReactionPopup
import com.github.pgreze.reactions.ReactionsConfig
import com.github.pgreze.reactions.ReactionsConfigBuilder
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.kys.linkedinclone.AppSharedPreferences
import com.kys.linkedinclone.Constants.Companion.ALL_POSTS
import com.kys.linkedinclone.R
import com.kys.linkedinclone.models.PostModel
import de.hdodenhof.circleimageview.CircleImageView
import java.text.SimpleDateFormat
import java.util.*

class PostAdapter(val context: Context, var list: List<PostModel>):
    RecyclerView.Adapter<PostAdapter.PostViewHolder>() {

    private val strings = arrayOf("Like", "Celebrate", "Support", "Love", "Insightful", "Idea")
    private var appSharedPreferences: AppSharedPreferences? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostViewHolder {
        return PostViewHolder(LayoutInflater.from(context).inflate(R.layout.card_post, parent, false))
    }

    override fun getItemCount(): Int {
        return list.size
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onBindViewHolder(holder: PostViewHolder, @SuppressLint("RecyclerView") position: Int) {
        val user = FirebaseAuth.getInstance().currentUser
        FirebaseDatabase.getInstance().reference
        appSharedPreferences = AppSharedPreferences(context)
        val gestureDetector = GestureDetector(context, object: GestureDetector.SimpleOnGestureListener(){
            override fun onDown(e: MotionEvent): Boolean {
                return true
            }

            override fun onSingleTapUp(e: MotionEvent): Boolean {
                addLike(user!!.uid, list[position].getKey(), holder)
                holder.like_clr.setTextColor(ContextCompat.getColor(context, R.color.main_color))
                return super.onSingleTapUp(e)
            }

            override fun onLongPress(e: MotionEvent) {
                val config: ReactionsConfig = ReactionsConfigBuilder(context)
                    .withReactions(
                        intArrayOf(
                            R.drawable.ic_link_like,
                            R.drawable.ic_link_celebrate,
                            R.drawable.ic_link_care,
                            R.drawable.ic_link_love,
                            R.drawable.ic_link_idea,
                            R.drawable.ic_link_curious
                        )
                    )
                    .withPopupAlpha(255)
                    .withReactionTexts { strings[position] }
                    .withTextBackground(ColorDrawable(Color.WHITE))
                    .withTextColor(Color.BLACK)
                    .withPopupGravity(PopupGravity.PARENT_RIGHT)
                    .withTextSize(context.resources.getDimension(R.dimen.reactions_text_size))
                    .build()
                val popup = ReactionPopup(context, config) {
                    // true is closing popup, false is requesting a new selection
                }
                popup.reactionSelectedListener?.let {
                    when (position) {
                        0 -> holder.ic_like.setImageResource(R.drawable.ic_link_like)
                        1 -> holder.ic_like.setImageResource(R.drawable.ic_link_celebrate)
                        2 -> holder.ic_like.setImageResource(R.drawable.ic_link_care)
                        3 -> holder.ic_like.setImageResource(R.drawable.ic_link_love)
                        4 -> holder.ic_like.setImageResource(R.drawable.ic_link_idea)
                        else -> holder.ic_like.setImageResource(R.drawable.ic_link_curious)
                    }
                        position != 3

                }
                addLike(user!!.uid, list[position].getKey(), holder)
                holder.btn_like.setOnTouchListener(popup)
                super.onLongPress(e)
            }
        })

        holder.btn_like.setOnTouchListener { _, motionEvent ->
            gestureDetector.onTouchEvent(
                motionEvent
            )
        }


        holder.desc.setLinkTextColor(context.getColor(R.color.main_color))
        holder.desc.text = list[position].getDescription()


        Glide.with(context).load(list[position].getUserProfile()).into(holder.userImage)
        holder.userName.text = list[position].getUsername()

        if (list[position].getImgUrl() == "") {
            holder.ll.visibility = View.GONE
        } else {
            Glide.with(context).load(list[position].getImgUrl()).into(holder.postImage)
            holder.ll.visibility = View.VISIBLE
        }

        val userKey = user!!.uid
        val postKey = list[position].getKey()

        val likeRef =
            FirebaseDatabase.getInstance().reference.child(ALL_POSTS).child(postKey).child("Likes")
        likeRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                for (snapshot1 in snapshot.children) {
                    if (snapshot1.key == userKey) {
                        holder.ic_like.setImageDrawable(
                            ContextCompat.getDrawable(
                                context,
                                R.drawable.ic_link_like
                            )
                        )
                        holder.like_clr.setTextColor(
                            ContextCompat.getColor(
                                context,
                                R.color.main_color
                            )
                        )
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {}
        })

        isLikes(holder.likesTxt, holder.commentTxt, postKey)

    }

    private fun isLikes(textView: TextView, commentcount: TextView, postkey: String) {
        val databaseReference = FirebaseDatabase.getInstance().reference
            .child(ALL_POSTS)
            .child(postkey)
        databaseReference.addValueEventListener(object : ValueEventListener {
            @SuppressLint("SetTextI18n")
            override fun onDataChange(snapshot: DataSnapshot) {
                textView.text = snapshot.child("Likes").childrenCount.toString() + ""
                commentcount.text = snapshot.child("Comments").childrenCount.toString() + " comments"
            }

            override fun onCancelled(error: DatabaseError) {}
        })
    }

    private fun addLike(
        uid: String,
        key: String,
        holder: PostViewHolder
    ) {
        val ref: DatabaseReference = FirebaseDatabase.getInstance().reference.child(ALL_POSTS).child(key).child("Likes")
        val sdf = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault())
        val date = Date()
        val map: HashMap<String, Any?> = HashMap<String, Any?>()
        map["time"] = sdf.format(date)
        map["username"] = appSharedPreferences!!.getUserName()
        map["imgUrl"] = appSharedPreferences!!.getImgUrl()
        ref.child(uid).setValue(map).addOnSuccessListener {
            holder.ic_like.setImageDrawable(
                ContextCompat.getDrawable(context, R.drawable.ic_link_like)
            )
        }
    }


    inner class PostViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var desc:TextView
        var postImage:ImageView
        var ic_like:ImageView
        var btn_like:LinearLayout
        var ll:CardView
        var userImage:CircleImageView
        var userName:TextView
        var likesTxt:TextView
        var commentTxt:TextView
        var like_clr:TextView

        init{
            desc = itemView.findViewById(R.id.text_1)
            postImage = itemView.findViewById(R.id.post_img)
            btn_like = itemView.findViewById(R.id.btn_like)
            ll = itemView.findViewById(R.id.ll)
            ic_like = itemView.findViewById(R.id.ic_like)
            userImage = itemView.findViewById(R.id.button_image)
            userName = itemView.findViewById(R.id.username)
            likesTxt = itemView.findViewById(R.id.likesTxt)
            commentTxt = itemView.findViewById(R.id.commentTxt)
            like_clr = itemView.findViewById(R.id.like_clr)

        }


    }
}
