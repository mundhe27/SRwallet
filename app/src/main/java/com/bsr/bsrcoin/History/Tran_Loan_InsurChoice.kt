package com.bsr.bsrcoin.History

import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.bsr.bsrcoin.R
import com.bsr.bsrcoin.Register

class Tran_Loan_InsurChoice : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.tran_loan_insur)
        val loan = findViewById<TextView>(R.id.tvLoan);
        val insurance = findViewById<TextView>(R.id.tvInsurance);
        val transaction = findViewById<TextView>(R.id.tvTransaction);
    }
    private fun onClickTransaction(){
        val intent = Intent(this, Register::class.java)  // go to transaction page of user/agent
        startActivity(intent)
    }
    private fun onClickLoan(){
        val intent = Intent(this, Register::class.java)  // go to loan page of user/agent
        startActivity(intent)
    }
    private fun onClickInsurance(){
        val intent = Intent(this, Register::class.java)  // go to insurance page of user/agent
        startActivity(intent)
    }
}