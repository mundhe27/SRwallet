package com.bsr.bsrcoin

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bsr.bsrcoin.Adapter.LoanInsuranceAdapter
import com.bsr.bsrcoin.ViewModels.InsuranceViewModel
import com.bsr.bsrcoin.network.Insurance
import com.bsr.bsrcoin.network.retroService
import com.bsr.bsrcoin.network.retrofitInstance
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AllInsuranceActivity : AppCompatActivity() {
    val insuranceList= arrayListOf<LoanOrInsurance>()
    lateinit var recyclerInsurance:RecyclerView
    lateinit var insuranceAdapter: LoanInsuranceAdapter
    lateinit var insuranceLayoutManager: RecyclerView.LayoutManager
    lateinit var insuranceViewModel: InsuranceViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_all_insurance)
        recyclerInsurance=findViewById(R.id.recyclerInsurance)
        insuranceLayoutManager=LinearLayoutManager(this@AllInsuranceActivity)
        insuranceAdapter= LoanInsuranceAdapter(this@AllInsuranceActivity)

        recyclerInsurance.layoutManager=insuranceLayoutManager
        recyclerInsurance.adapter=insuranceAdapter

//        insuranceViewModel=ViewModelProvider(this,ViewModelProvider.AndroidViewModelFactory.getInstance(application)).get(InsuranceViewModel::class.java)
//        insuranceViewModel.allInsurance.observe(this,{list->
//            list.let {
//                for(element in it)
//                {
//                    insuranceList.add(LoanOrInsurance(element.insuranceId,element.amount,element.insuranceType,element.agentName,null,element.duration,"Insurance"))
//                }
//                insuranceAdapter.updateList(insuranceList)
//            }
//        })

        val call=retrofitInstance.getretrofitConnection().create(retroService::class.java).getInsurance()
        call.enqueue(object :Callback<List<Insurance>>{
            override fun onResponse(
                call: Call<List<Insurance>>,
                response: Response<List<Insurance>>
            ) {
                if (response.isSuccessful)
                {
                    val list=response.body()!!
                    for (item in list)
                    {
                        //TODO: ADD the response to adapter
                        //insuranceList.add(LoanOrInsurance())
                    }
                }
            }

            override fun onFailure(call: Call<List<Insurance>>, t: Throwable) {
                Toast.makeText(this@AllInsuranceActivity,"Exception Occurred: ${t.message}",Toast.LENGTH_LONG).show()
            }
        })
    }
}