package com.bsr.bsrcoin.Fragments

import android.app.ProgressDialog
import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bsr.bsrcoin.Adapter.ViewDepositAdapter
import com.bsr.bsrcoin.R
import com.bsr.bsrcoin.SharedPrefmanager
import com.bsr.bsrcoin.network.Deposit
import com.bsr.bsrcoin.network.DepositList
import com.bsr.bsrcoin.network.RetrofitBondAndDepositInstance
import com.bsr.bsrcoin.network.retrofitDepositService
import retrofit2.Call
import retrofit2.Response
import javax.security.auth.callback.Callback


class fund_deposit : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_view_deposit, container, false)
        view.findViewById<TextView>(R.id.deposit_info).visibility = View.GONE
        return view

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val allDeposit = ArrayList<Deposit>()
        val depositAdapter = ViewDepositAdapter(allDeposit,requireContext(),1)
        val recyclerView = view.findViewById<RecyclerView>(R.id.depositviewRecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = depositAdapter

        val progressDialog = ProgressDialog(requireContext())
        progressDialog.setCancelable(false)
        progressDialog.setMessage("Loading Deposits..")
        progressDialog.show()

        val call=
            RetrofitBondAndDepositInstance.getretrofitBondAndDepositConnection().create(
                retrofitDepositService::class.java).getDepositbyuserid(
                SharedPrefmanager.getInstance(requireContext().applicationContext).keyId.toInt()
            )
        call.enqueue(object : Callback, retrofit2.Callback<DepositList> {
            override fun onResponse(
                call: Call<DepositList>,
                response: Response<DepositList>
            ) {
                progressDialog.dismiss()
                if(response.isSuccessful)
                {
                    Log.v("Api Call","${response.body()!!}")
                    if (response.body()!!.deposit == null
                    ) {
                        view.findViewById<ConstraintLayout>(R.id.noitem).visibility = View.VISIBLE
                    }
                    else {
                        view.findViewById<ConstraintLayout>(R.id.noitem).visibility = View.GONE
                        for (i in response.body()!!.deposit) {
                            allDeposit.add(
                                Deposit(
                                    i.userId,
                                    i.deposit_amount,
                                    i.deposit_type,
                                    i.rate_of_Interest,
                                    i.duration,
                                    i.expiry,
                                    i.depositId,
                                    i.renewedOn
                                )
                            )
                            Log.e("TAG", "onResponse: $allDeposit", )
                        }
                        depositAdapter.notifyDataSetChanged()}
                }
                else
                    Log.d("Api Call","Bad Response Code")
            }


            override fun onFailure(call: Call<DepositList>, t: Throwable) {
                Toast.makeText(activity as Context,"Exception Occurred: ${t.message} ", Toast.LENGTH_LONG).show()
            }
        })

    }
}