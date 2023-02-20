package com.bsr.bsrcoin.Fragments

import android.app.ProgressDialog
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TableLayout
import android.widget.TableRow
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.core.view.setPadding
import androidx.fragment.app.Fragment
import com.bsr.bsrcoin.R
import com.bsr.bsrcoin.network.*
import retrofit2.Call
import retrofit2.Response
import javax.security.auth.callback.Callback

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class ViewShareFragment : Fragment() {
    private var param1: String? = null
    private var param2: String? = null
    private lateinit var sharetable:TableLayout
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view=inflater.inflate(R.layout.fragment_view_share, container, false)
        sharetable=view.findViewById<TableLayout>(R.id.table_share)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val allshare = ArrayList<Share>()
        val sview=view.findViewById<androidx.appcompat.widget.SearchView>(R.id.searchview)

        showDefaultBond(allshare)
        sview.setOnClickListener {
 sview.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(p0: String?): Boolean {
                    Log.d("SearchFragment", "onQueryTextSubmit call")
                    val p = p0.toString().trim { it <= ' ' }
                    if (p.isEmpty()) {
                        allshare.clear()
                        Log.d("SearchFragment", "onQueryTextsubmit")
                        showDefaultBond(allshare)
                    } else {
                        allshare.clear()
                        val progressDialog = ProgressDialog(requireContext())
                        progressDialog.setCancelable(false)
                        progressDialog.setMessage("Loading Shares..")
                        progressDialog.show()

                        val call =
                            RetrofitBondAndDepositInstance.getretrofitBondAndDepositConnection()
                                .create(
                                    retrofitShareServices::class.java
                                ).getallShare()
                        call.enqueue(object : Callback, retrofit2.Callback<shareList> {
                            override fun onResponse(
                                call: Call<shareList>,
                                response: Response<shareList>
                            ) {
                                progressDialog.dismiss()
                                if (response.isSuccessful) {
                                    Log.v("Api Call", "${response.body()!!}")
                                    if (response.body()!!.share == null
                                    ) {
                                        Toast.makeText(
                                            context,
                                            "No Share Till Now",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    } else {
                                        val list = response.body()!!.share
                                        for (i in list) {
                                            if (i.share_type.lowercase()
                                                    .startsWith(p!!.lowercase())
                                            ) {
                                                allshare.add(
                                                    Share(
                                                        i.userId,
                                                        i.share_name,
                                                        i.share_amount,
                                                        i.share_type,
                                                        ""
                                                    )
                                                )
                                            }
                                        }
                                        if (allshare.isEmpty()) Toast.makeText(
                                            context,
                                            "No Share Till Now StartWith $p0",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                        else init(allshare)
                                    }
                                } else
                                    Log.d("Api Call", "Bad Response Code")
                            }


                            override fun onFailure(call: Call<shareList>, t: Throwable) {
                                Toast.makeText(
                                    activity as Context,
                                    "Exception Occurred: ${t.message} ",
                                    Toast.LENGTH_LONG
                                ).show()
                            }
                        })

                    }
                    return true
                }

                override fun onQueryTextChange(p0: String?): Boolean {
                    Log.d("SearchFragment", "onQueryTextChange call")
                    val p = p0.toString().trim { it <= ' ' }
                    if (p.isEmpty()) {
                        allshare.clear()
                        Log.d("SearchFragment", "onQueryTextChange ")
                        showDefaultBond(allshare)
                    }
                    Log.d("SearchFragment", "onQueryTextChange complete")
                    return true
                }

            })
        }


    }
    private fun init(allshare: ArrayList<Share>) {
        sharetable.removeAllViews()
        if(allshare.size!=0){
            val params= TableRow.LayoutParams(
                TableRow.LayoutParams.WRAP_CONTENT,
                TableRow.LayoutParams.WRAP_CONTENT)
            params.leftMargin=20
            params.rightMargin=20
            val tbrow0 = TableRow(context)
            tbrow0.weightSum= 3F
            val tv0 = TextView(context)
            tv0.text = getString(R.string.serialNo)
            tv0.gravity= Gravity.CENTER
            tv0.setLayoutParams(params)
            tv0.textSize= 22.0F
            tv0.setPadding(10)
            tbrow0.addView(tv0)

            val tv1 = TextView(context)
            tv1.text = getString(R.string.share)
            tv1.textSize= 22.0F
            tv1.gravity = Gravity.CENTER
            tv1.setPadding(10)
            tv1.setLayoutParams(params)
            tbrow0.addView(tv1)
            val tv2 = TextView(context)
            tv2.text = getString(R.string.amount)
            tv2.gravity= Gravity.RIGHT
            tv2.textSize= 22.0F
            tv2.setPadding(10)
            tv2.setLayoutParams(params)
            tbrow0.addView(tv2)
            sharetable.addView(tbrow0)
            for(i in 0 until allshare.size){
                val tbrow1= TableRow(context)
                val tv3 = TextView(context)
                tv3.text = "${i+1}"
                tv3.textSize= 20.0F
                tv3.setPadding(5)
                tv3.setLayoutParams(params)
                tv3.gravity = Gravity.CENTER
                tbrow1.addView(tv3)
                val tv4 = TextView(context)
                tv4.text = allshare[i].share_name
                tv4.textSize= 20.0F
                tv4.setLayoutParams(params)
                tv4.setPadding(10)
                tbrow1.addView(tv4)
                val tv5 = TextView(context)
                tv5.text = allshare[i].share_amount.toString()
                tv5.gravity= Gravity.RIGHT
                tv5.setLayoutParams(params)
                tv5.textSize= 20.0F
                tv5.setPadding(5)
                tbrow1.addView(tv5)
                sharetable.addView(tbrow1)
            }
        }
    }
    private fun showDefaultBond(allshare:ArrayList<Share>) {


        val progressDialog = ProgressDialog(requireContext())
        progressDialog.setCancelable(false)
        progressDialog.setMessage("Loading Shares..")
        progressDialog.show()

        val call=
            RetrofitBondAndDepositInstance.getretrofitBondAndDepositConnection().create(
                retrofitShareServices::class.java).getallShare(
)
        call.enqueue(object : Callback, retrofit2.Callback<shareList> {
            override fun onResponse(
                call: Call<shareList>,
                response: Response<shareList>
            ) {
                progressDialog.dismiss()
                if(response.isSuccessful)
                {
                    Log.v("Api Call","${response.body()!!}")
                    if (response.body()!!.share == null
                    ) {
                        Toast.makeText(context, "No share Till Now", Toast.LENGTH_SHORT).show()
                    }
                    else {
                        for (i in response.body()!!.share) {
                            allshare.add(
                                Share(
                                    i.userId,
                                    i.share_name,
                                    i.share_amount,
                                    i.share_type,
                                    ""
                                )
                            )
                        }
                        init(allshare)
                                            }
                }
                else
                    Log.d("Api Call","Bad Response Code")
            }


            override fun onFailure(call: Call<shareList>, t: Throwable) {
                Toast.makeText(activity as Context,"Exception Occurred: ${t.message} ", Toast.LENGTH_LONG).show()
            }
        })
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment ViewShareFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            ViewShareFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}