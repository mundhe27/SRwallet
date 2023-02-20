package com.bsr.bsrcoin.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bsr.bsrcoin.R
import com.bsr.bsrcoin.network.Share

class ViewShareAdapter(private val allshare:ArrayList<Share>, val context: Context): RecyclerView.Adapter<ViewShareAdapter.ShareViewHolder>() {

    class ShareViewHolder(view: View): RecyclerView.ViewHolder(view) {
        val userid=view.findViewById<TextView>(R.id.share_i_user_id)
        val type=view.findViewById<TextView>(R.id.share_i_type)
        val amount=view.findViewById<TextView>(R.id.share_i_amount)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ShareViewHolder {
        val view=
            LayoutInflater.from(parent.context).inflate(R.layout.view_share,parent,false)
        return ShareViewHolder(view)
    }


    override fun getItemCount(): Int {
        return allshare.size
    }

    override fun onBindViewHolder(holder: ShareViewHolder, position: Int) {

        if (allshare.isEmpty()) {
            Toast.makeText(context, "No share Till Now", Toast.LENGTH_SHORT).show()
        }else{
        holder.userid.text=allshare[position].userId.toString()
        holder.type.text=allshare[position].share_type
        holder.amount.text= allshare[position].share_amount.toString()}
    }

}