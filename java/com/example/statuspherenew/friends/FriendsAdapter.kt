package com.example.statuspherenew.friends

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.statuspherenew.R

class FriendsAdapter(private val friends: List<Friend>, private val onClick: (Friend) -> Unit) :
    RecyclerView.Adapter<FriendsAdapter.FriendViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FriendViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_friend, parent, false)
        return FriendViewHolder(view, onClick)
    }

    override fun onBindViewHolder(holder: FriendViewHolder, position: Int) {
        val friend = friends[position]
        holder.bind(friend)
    }

    override fun getItemCount(): Int = friends.size

    class FriendViewHolder(view: View, val onClick: (Friend) -> Unit) : RecyclerView.ViewHolder(view) {
        private val imageView: ImageView = view.findViewById(R.id.friendImage)
        private val nameView: TextView = view.findViewById(R.id.friendName)
        private val descriptionView: TextView = view.findViewById(R.id.friendDescription)

        fun bind(friend: Friend) {
            imageView.setImageResource(friend.imageResId)
            nameView.text = friend.name
            descriptionView.text = friend.description
            itemView.setOnClickListener { onClick(friend) }
        }
    }
}