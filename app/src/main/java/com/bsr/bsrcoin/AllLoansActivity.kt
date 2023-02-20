package com.bsr.bsrcoin

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bsr.bsrcoin.Adapter.LoanInsuranceAdapter
import com.bsr.bsrcoin.ViewModels.LoansViewModel

class AllLoansActivity : AppCompatActivity() {
    lateinit var recyclerLoans:RecyclerView
    lateinit var recyclerLoanAdapter:LoanInsuranceAdapter
    lateinit var recyclerLayoutManager:RecyclerView.LayoutManager
    lateinit var loansViewModel: LoansViewModel
    val listLoans= arrayListOf<LoanOrInsurance>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_all_loans)
        recyclerLoans=findViewById(R.id.recyclerLoans)
        recyclerLayoutManager=LinearLayoutManager(this@AllLoansActivity)
        recyclerLoanAdapter=LoanInsuranceAdapter(this@AllLoansActivity)

        recyclerLoans.layoutManager=recyclerLayoutManager
        recyclerLoans.adapter=recyclerLoanAdapter

        loansViewModel=ViewModelProvider(this,ViewModelProvider.AndroidViewModelFactory.getInstance(application)).get(LoansViewModel::class.java)
        loansViewModel.allLoans.observe(this,{list->
            list.let{
                for (element in it)
                {
                    listLoans.add(
                        LoanOrInsurance(
                        element.LoanId,element.amount,element.loanType,element.agentName,element.rateInterest,element.duration,"Loan")
                    )
                }
                recyclerLoanAdapter.updateList(listLoans)
            }
        })
    }
}