package com.bsr.bsrcoin.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bsr.bsrcoin.Database.TransactionEntity
import com.bsr.bsrcoin.R

class TransactionsAdapter(context: Context,val Transactions:List<TransactionEntity>):RecyclerView.Adapter<TransactionsAdapter.TransactionViewHolder>() {
    class TransactionViewHolder(view: View):RecyclerView.ViewHolder(view)
    {
//        val paid_received:TextView=view.findViewById(R.id.paid_received)
//        val accNo:TextView=view.findViewById(R.id.accNo)
        val Date:TextView=view.findViewById(R.id.Date)
        val amount:TextView=view.findViewById(R.id.amount)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TransactionViewHolder {
        val view=LayoutInflater.from(parent.context).inflate(R.layout.transaction_single,parent,false)
        return TransactionViewHolder(view)
    }

    override fun onBindViewHolder(holder: TransactionViewHolder, position: Int) {
        TODO("Not yet implemented")
    }

    override fun getItemCount(): Int {
        return Transactions.size
    }
}