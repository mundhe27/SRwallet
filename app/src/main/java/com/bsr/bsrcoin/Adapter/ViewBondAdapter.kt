package com.bsr.bsrcoin.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bsr.bsrcoin.R
import com.bsr.bsrcoin.network.Bond

class ViewBondAdapter(private val allbond:ArrayList<Bond>, val context: Context): RecyclerView.Adapter<ViewBondAdapter.BondViewHolder>() {

    class BondViewHolder(view: View): RecyclerView.ViewHolder(view) {
        val userid=view.findViewById<TextView>(R.id.bond_i_user_id)
        val type=view.findViewById<TextView>(R.id.bond_i_type)
        val amount=view.findViewById<TextView>(R.id.bond_i_amount)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BondViewHolder {
        val view=
            LayoutInflater.from(parent.context).inflate(R.layout.view_bond,parent,false)
        return BondViewHolder(view)
    }


    override fun getItemCount(): Int {
        return allbond.size
    }

    override fun onBindViewHolder(holder: BondViewHolder, position: Int) {
        if (allbond.isEmpty()) {
            Toast.makeText(context, "No Bond Till Now", Toast.LENGTH_SHORT).show()
        }else{
        holder.userid.text=allbond[position].userId.toString()
        holder.type.text=allbond[position].bond_type
        holder.amount.text= allbond[position].bond_amount.toString()}
    }

}
