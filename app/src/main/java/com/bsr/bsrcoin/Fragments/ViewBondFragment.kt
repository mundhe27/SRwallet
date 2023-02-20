package com.bsr.bsrcoin.Fragments

import android.app.ProgressDialog
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.view.marginRight
import androidx.core.view.marginStart
import androidx.core.view.setMargins
import androidx.core.view.setPadding
import androidx.fragment.app.Fragment
import com.bsr.bsrcoin.R
import com.bsr.bsrcoin.network.Bond
import com.bsr.bsrcoin.network.BondList
import com.bsr.bsrcoin.network.RetrofitBondAndDepositInstance
import com.bsr.bsrcoin.network.retrofitBondService
import retrofit2.Call
import retrofit2.Response
import javax.security.auth.callback.Callback


private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class ViewBondFragment : Fragment() {
    private var param1: String? = null
    private var param2: String? = null
    private lateinit var bondtable: TableLayout
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
        val view=inflater.inflate(R.layout.fragment_view_bond, container, false)
        bondtable = view.findViewById(R.id.table_bond)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val allBonds = ArrayList<Bond>()
        val sview=view.findViewById<androidx.appcompat.widget.SearchView>(com.bsr.bsrcoin.R.id.sview)

       showDefaultBond(allBonds)
        sview.setOnClickListener {
            sview.setOnQueryTextListener(object :SearchView.OnQueryTextListener,
                androidx.appcompat.widget.SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(p0: String?): Boolean {
                    Log.d("SearchFragment","onQueryTextSubmit call")
                    val p=p0.toString().trim {it<=' '}
                    if(p.isEmpty()){
                        allBonds.clear()
                        Log.d("SearchFragment","onQueryTextsubmit")
                        showDefaultBond(allBonds)
                    }
                    else{
                        allBonds.clear()
                        val progressDialog = ProgressDialog(requireContext())
                        progressDialog.setCancelable(false)
                        progressDialog.setMessage("Loading Bonds..")
                        progressDialog.show()

                        val call=
                            RetrofitBondAndDepositInstance.getretrofitBondAndDepositConnection().create(
                                retrofitBondService::class.java).getallBond()
                        call.enqueue(object : Callback, retrofit2.Callback<BondList> {
                            override fun onResponse(
                                call: Call<BondList>,
                                response: Response<BondList>
                            ) {
                                progressDialog.dismiss()
                                if(response.isSuccessful)
                                {
                                    Log.v("Api Call","${response.body()!!}")
                                    if (response.body()!!.bond == null
                                    ) {
                                        Toast.makeText(context, "No Bond Till Now", Toast.LENGTH_SHORT).show()
                                    }
                                    else {
                                        val list=response.body()!!.bond
                                        for (i in list) {
                                            if(i.bond_type.lowercase().startsWith(p.lowercase())){
                                                allBonds.add(
                                                    Bond(
                                                        i.userId,
                                                        i.bond_name,
                                                        i.bond_amount,
                                                        i.bond_type,
                                                        ""
                                                    )
                                                )
                                            }
                                        }
                                        if(allBonds.isEmpty()) Toast.makeText(context, "No Bond Till Now StartWith $p0", Toast.LENGTH_SHORT).show()
                                        else init(allBonds)
                                        }

                                }
                                else
                                    Log.d("Api Call","Bad Response Code")
                            }


                            override fun onFailure(call: Call<BondList>, t: Throwable) {
                                Toast.makeText(activity as Context,"Exception Occurred: ${t.message} ", Toast.LENGTH_LONG).show()
                            }
                        })

                    }
                    return true
                }

                override fun onQueryTextChange(p0: String?): Boolean {
                    Log.d("SearchFragment","onQueryTextChange call")
                    val p=p0.toString().trim {it<=' '}
                    if(p.isEmpty()){
                        allBonds.clear()
                        Log.d("SearchFragment","onQueryTextChange ")
                        showDefaultBond(allBonds)
                    }
                    Log.d("SearchFragment","onQueryTextChange complete")
                    return true
                }

            })
    }
    }

     private fun init(allBonds: ArrayList<Bond>) {
         bondtable.removeAllViews()
        if(allBonds.size!=0){
            val params=TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT,
                TableRow.LayoutParams.WRAP_CONTENT)
            params.leftMargin=20
            params.rightMargin=20
        val tbrow0 = TableRow(context)
            tbrow0.weightSum= 3F
        val tv0 = TextView(context)
        tv0.text = getString(R.string.serialNo)
            tv0.gravity=Gravity.CENTER
            tv0.setLayoutParams(params)
            tv0.textSize= 22.0F
        tv0.setPadding(10)
        tbrow0.addView(tv0)

        val tv1 = TextView(context)
        tv1.text = getString(R.string.Bond)
            tv1.textSize= 22.0F
            tv1.gravity = Gravity.CENTER
        tv1.setPadding(10)
            tv1.setLayoutParams(params)
        tbrow0.addView(tv1)
        val tv2 = TextView(context)
        tv2.text = getString(R.string.amount)
            tv2.gravity=Gravity.RIGHT
            tv2.textSize= 22.0F
            tv2.setPadding(10)
            tv2.setLayoutParams(params)
        tbrow0.addView(tv2)
        bondtable.addView(tbrow0)
        for(i in 0 until allBonds.size){
            val tbrow1=TableRow(context)
            val tv3 = TextView(context)
            tv3.text = "${i+1}"
            tv3.textSize= 20.0F
            tv3.setPadding(5)
            tv3.setLayoutParams(params)
            tv3.gravity = Gravity.CENTER
            tbrow1.addView(tv3)
            val tv4 = TextView(context)
            tv4.text = allBonds[i].bond_name
            tv4.textSize= 20.0F
            tv4.setLayoutParams(params)
            tv4.setPadding(10)
            tbrow1.addView(tv4)
            val tv5 = TextView(context)
            tv5.text = allBonds[i].bond_amount.toString()
            tv5.gravity=Gravity.RIGHT
            tv5.setLayoutParams(params)
            tv5.textSize= 20.0F
            tv5.setPadding(5)
            tbrow1.addView(tv5)
            bondtable.addView(tbrow1)
        }
        }
    }

    private fun showDefaultBond(allBonds:ArrayList<Bond>) {
        val progressDialog = ProgressDialog(requireContext())
        progressDialog.setCancelable(false)
        progressDialog.setMessage("Loading Bonds..")
        progressDialog.show()

        val call=
            RetrofitBondAndDepositInstance.getretrofitBondAndDepositConnection().create(
                retrofitBondService::class.java).getallBond(
 )
        call.enqueue(object : Callback, retrofit2.Callback<BondList> {
            override fun onResponse(
                call: Call<BondList>,
                response: Response<BondList>
            ) {
                progressDialog.dismiss()
                if(response.isSuccessful)
                {
                    Log.v("Api Call","${response.body()!!}")
                    if (response.body()!!.bond == null
                    ) {
                        Toast.makeText(context, "No Bond Till Now", Toast.LENGTH_SHORT).show()
                    }
                    else {
                        val list=response.body()!!.bond

                        for (i in list) {
                            allBonds.add(
                                Bond(
                                    i.userId,
                                    i.bond_name,
                                    i.bond_amount,
                                    i.bond_type,
                                    ""
                                )
                            )
                        }
                        init(allBonds)
                    }
                }
                else
                    Log.d("Api Call","Bad Response Code")
            }


            override fun onFailure(call: Call<BondList>, t: Throwable) {
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
         * @return A new instance of fragment ViewBondFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            ViewBondFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}