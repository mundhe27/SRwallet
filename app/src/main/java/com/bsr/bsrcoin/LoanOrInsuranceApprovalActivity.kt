package com.bsr.bsrcoin

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class LoanOrInsuranceApprovalActivity : AppCompatActivity() {
    private lateinit var recyclerview : RecyclerView
    private lateinit var loan_insuranceList : ArrayList<LoanOrInsurance>
    lateinit var id_array : Array<Int>
    lateinit var amount_array: Array<Float>
    lateinit var type_array: Array<String>
    lateinit var agent_array: Array<String>
//     val rateId:
//    var durationId: String
//   var fromId: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.loan_insurance_recyclerview)

        recyclerview = findViewById<RecyclerView>(R.id.recyclerview)
        recyclerview.layoutManager = LinearLayoutManager(this)

        val data = ArrayList<LoanOrInsurance>()

//        for (i in 1..20) {
//            data.add(LoanOrInsurance( "Item " + i))
//        }
//        val adapter = LoanInsuranceAdapter(data)
//        recyclerview.adapter = adapter
    }
}