package com.bsr.bsrcoin.Fragments

import android.app.ProgressDialog
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bsr.bsrcoin.R
import com.bsr.bsrcoin.SharedPrefmanager
import com.bsr.bsrcoin.network.RetrofitBondAndDepositInstance
import com.bsr.bsrcoin.network.Transaction
import com.bsr.bsrcoin.network.TransactionList
import com.bsr.bsrcoin.network.retrofitTransactionServices
import retrofit2.Call
import retrofit2.Response
import javax.security.auth.callback.Callback

class TransactionsFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return inflater.inflate(R.layout.fragment_transactions, container, false)
    }
       override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
            super.onViewCreated(view, savedInstanceState)
            val alltransaction = ArrayList<Transaction>()
            val Adapter = CustomAdapter(alltransaction)
           val recyclerview = view.findViewById<RecyclerView>(R.id.transactions)
           recyclerview.layoutManager = LinearLayoutManager(requireContext())
           recyclerview.adapter = Adapter

            val progressDialog = ProgressDialog(requireContext())
            progressDialog.setCancelable(false)
            progressDialog.setMessage("Loading Transactions..")
            progressDialog.show()


           if(SharedPrefmanager.getInstance(context).isLoggedIN) {
               val id : String? = SharedPrefmanager.getInstance(context).keyId

            Log.e("Key ID",id.toString())
            val call=
                RetrofitBondAndDepositInstance.getretrofitBondAndDepositConnection().create(
                    retrofitTransactionServices::class.java).gettransactionbyuserid(id)

            call.enqueue(object : Callback, retrofit2.Callback<TransactionList> {
                override fun onResponse(
                    call: Call<TransactionList>,
                    response: Response<TransactionList>
                ) {
                    progressDialog.dismiss()
                    if(response.isSuccessful)
                    {
                        Log.v("Api Call","${response.body()!!}")
                        if (response.body()!!.transaction == null
                        ) {
                            Toast.makeText(context, "No Transactions Till Now", Toast.LENGTH_SHORT).show()
                        }
                        else {
                            for (i in response.body()!!.transaction) {
                                alltransaction.add(
                                    Transaction(
                                        i.from_userId,
                                        i.to_userId,
                                        i.from_walletId,
                                        i.to_walletId,
                                        i.from_account,
                                        i.to_account,
                                        i.transaction_type,
                                        i.transaction_amount,
                                        i.transaction_time,
                                        i.description
                                    )
                                )
                            }
                            Adapter.notifyDataSetChanged()}
                    }
                    else
                        Log.d("Api Call","Bad Response Code")
                }


                override fun onFailure(call: Call<TransactionList>, t: Throwable) {
                    Toast.makeText(activity as Context,"Exception Occurred: ${t.message} ", Toast.LENGTH_LONG).show()
                }
            })
           }
        }


}



