package com.example.statuspherenew.groups
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.statuspherenew.R


class GroupsAdapter(private val groups: List<Group>) :
    RecyclerView.Adapter<GroupsAdapter.GroupViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GroupViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.group_item, parent, false)
        return GroupViewHolder(view)
    }

    override fun onBindViewHolder(holder: GroupViewHolder, position: Int) {
        holder.bind(groups[position])
    }

    override fun getItemCount() = groups.size

    class GroupViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(group: Group) {
            itemView.findViewById<TextView>(R.id.groupTitle).text = group.name
            itemView.findViewById<TextView>(R.id.groupDescription).text = group.description
        }
    }
}
