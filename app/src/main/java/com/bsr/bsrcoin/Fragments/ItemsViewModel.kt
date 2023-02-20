package com.bsr.bsrcoin.Fragments


import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bsr.bsrcoin.R
import com.bsr.bsrcoin.network.Transaction

class CustomAdapter(private val mList: List<Transaction>) : RecyclerView.Adapter<CustomAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.transaction_single, parent, false)
        return ViewHolder(view)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val ItemsViewModel = mList[position]
        holder.paidto.text="Paid to-"+ItemsViewModel.to_account.toString()
        holder.recievedfrom.text="Received From-"+ItemsViewModel.from_account.toString()
        holder.type.text=ItemsViewModel.transaction_type
        holder.description.text=ItemsViewModel.description
        holder.timeandDate.text = ItemsViewModel.transaction_time
        holder.amount.text=ItemsViewModel.transaction_amount.toString()
    }

    override fun getItemCount(): Int {
        return mList.size
    }

    class ViewHolder(ItemView: View) : RecyclerView.ViewHolder(ItemView) {
        val paidto: TextView = itemView.findViewById(R.id.paid_to)
        val recievedfrom: TextView = itemView.findViewById(R.id.recieved_from)
        val type: TextView = itemView.findViewById(R.id.Transactiontype)
        val description: TextView = itemView.findViewById(R.id.description)
        val timeandDate: TextView = itemView.findViewById(R.id.Date)
        val amount:TextView=itemView.findViewById(R.id.amount)

    }
}