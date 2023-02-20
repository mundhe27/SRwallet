package com.bsr.bsrcoin.Adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bsr.bsrcoin.ViewModels.UserAgentViewModel
import com.bsr.bsrcoin.R

class UserAgentAdapter(private val UserAdminList: List<UserAgentViewModel>) : RecyclerView.Adapter<UserAgentAdapter.UserAgentViewHolder>() {

    class UserAgentViewHolder(ItemView: View) : RecyclerView.ViewHolder(ItemView) {
//        val ivUser: ImageView = itemView.findViewById(R.id.ivUser)
        val tvUser: TextView = itemView.findViewById(R.id.tvUser)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserAgentViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.user_agent_single, parent, false)

        return UserAgentViewHolder(view)
    }

    override fun onBindViewHolder(holder: UserAgentViewHolder, position: Int) {
        val ItemsViewModel = UserAdminList[position]
//        holder.ivUser.setImageResource(ItemsViewModel.image)
        holder.tvUser.text = ItemsViewModel.userOrAgent
    }

    override fun getItemCount(): Int {
        return UserAdminList.size
    }


}
