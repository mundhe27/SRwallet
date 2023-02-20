package com.bsr.bsrcoin.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bsr.bsrcoin.LoanOrInsurance
import com.bsr.bsrcoin.R

class LoanInsuranceAdapter(context: Context):RecyclerView.Adapter<LoanInsuranceAdapter.LoanInsuranceViewHolder>(){
    private val loan_insuranceList= arrayListOf<LoanOrInsurance>()
    class LoanInsuranceViewHolder(view: View):RecyclerView.ViewHolder(view){
        val tvLoan_Insurance:TextView=view.findViewById(R.id.tvLoan_Insurance)
        val tvAmount:TextView=view.findViewById(R.id.tvAmount)
        val tvID:TextView=view.findViewById(R.id.tvID)
        val tvAgent:TextView=view.findViewById(R.id.tvAgent)
        val tvType:TextView=view.findViewById(R.id.tvType)
        val tvDuration:TextView=view.findViewById(R.id.tvDuration)
        val tvInterest:TextView=view.findViewById(R.id.tvInterest)
        val Accept:TextView=view.findViewById(R.id.Accept)
        val Reject:TextView=view.findViewById(R.id.Reject)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LoanInsuranceViewHolder {
        val view=LayoutInflater.from(parent.context).inflate(R.layout.loan_insurance_single,parent,false)
        return LoanInsuranceViewHolder(view)
    }

    override fun onBindViewHolder(holder: LoanInsuranceViewHolder, position: Int) {
        val loanOrInsurance=loan_insuranceList[position]
        holder.tvLoan_Insurance.text=loanOrInsurance.from
        holder.tvAmount.text=loanOrInsurance.amount.toString()
        holder.tvID.text=loanOrInsurance.Id.toString()
        holder.tvAgent.text=loanOrInsurance.agentName
        holder.tvType.text=loanOrInsurance.Type
        holder.tvDuration.text=loanOrInsurance.duration
        holder.tvInterest.text=loanOrInsurance.rateInterest?.toString()
        holder.Accept.setOnClickListener {
            //TODO: Code to be executed when loan/insurance accepted
        }
        holder.Reject.setOnClickListener {
            //TODO:Code to be executed when loan/insurance rejected
        }
    }

    override fun getItemCount(): Int {
        return loan_insuranceList.size
    }
    fun updateList(updateList:List<LoanOrInsurance>){
        loan_insuranceList.clear()
        loan_insuranceList.addAll(updateList)
        notifyDataSetChanged()
    }
}