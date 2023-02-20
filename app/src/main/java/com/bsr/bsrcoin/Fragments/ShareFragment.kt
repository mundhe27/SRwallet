package com.bsr.bsrcoin.Fragments

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.content.ContextCompat
import androidx.core.text.isDigitsOnly
import androidx.core.view.setPadding
import androidx.fragment.app.Fragment
import com.bsr.bsrcoin.R
import com.bsr.bsrcoin.SharedPrefmanager
import com.bsr.bsrcoin.network.BShare
import com.bsr.bsrcoin.network.RetrofitBondAndDepositInstance
import com.bsr.bsrcoin.network.retrofitShareServices
import com.google.android.material.snackbar.Snackbar
import com.google.gson.GsonBuilder
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*
import javax.security.auth.callback.Callback


private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class ShareFragment : Fragment() {
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
        val view=inflater.inflate(R.layout.fragment_share, container, false)
        sharetable=view.findViewById(R.id.table_share_apply)
        init()
        parentFragmentManager.beginTransaction().replace(R.id.graphshareFrame,
            ShareTypeFragment()
        ).commit()

        return view
    }

    private fun init() {
        val params=TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT,
            TableRow.LayoutParams.WRAP_CONTENT)
        params.leftMargin=20
        params.rightMargin=20
        params.weight=1F
        val sharetype = arrayOf("Dcart", "Oil", "Petrol","Desail", "Tomato","Orange","Apple","Dcoin","Scoin","Mcoin","Tcoin")
        val amountList= arrayOf(5000,500000,10,92,6500,8200,22000,10000,15000,700,600)
        val tbrow=TableRow(context)
        val tv0=TextView(context)
        tv0.text=getString(R.string.serialNo)
        tv0.gravity= Gravity.CENTER
        tv0.setLayoutParams(params)
        tv0.textSize= 22.0F
        tv0.setPadding(10)
        tbrow.addView(tv0)
        val paramsz=TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT,
            TableRow.LayoutParams.MATCH_PARENT)
        paramsz.leftMargin=20
        val tv1 = TextView(context)
        tv1.text = getString(R.string.share)
        tv1.textSize= 22.0F
        tv1.gravity = Gravity.CENTER
        tv1.setPadding(10)
        tv1.setLayoutParams(paramsz)
        tbrow.addView(tv1)
        val tv2 = TextView(context)
        tv2.text = getString(R.string.amount)
        tv2.textSize= 22.0F
        tv2.setLayoutParams(params)
        tv2.setPadding(10)
        tbrow.addView(tv2)
        val q=TextView(context)
        q.text=getString(R.string.quantity)
        q.gravity= Gravity.CENTER
        q.textSize=22.0F
        q.layoutParams=params
        q.setPadding(10)
        tbrow.addView(q)
        val tv = TextView(context)
        tv.text = getString(R.string.apply)
        tv.gravity= Gravity.CENTER
        tv.textSize= 22.0F
        tv.setLayoutParams(paramsz)
        tv.setPadding(10)
        tbrow.addView(tv)
        sharetable.addView(tbrow)
        for(i in amountList.indices){
            val tbrow1=TableRow(context)
            tbrow1.weightSum=4F
            val tv3 = TextView(context)
            tv3.text = "${i+1}"
            tv3.textSize= 20.0F
            tv3.setPadding(10)
            tv3.gravity = Gravity.CENTER
            tv3.setLayoutParams(params)
            tbrow1.addView(tv3)
            val tv4 = TextView(context)
            tv4.text = sharetype[i]
            tv4.textSize= 20.0F
            tv4.setLayoutParams(paramsz)
            tv4.setPadding(10)
            tbrow1.addView(tv4)
            val tv5 = TextView(context)
            tv5.text = amountList[i].toString()
            tv5.gravity= Gravity.RIGHT
            tv5.setLayoutParams(params)
            tv5.textSize= 20.0F
            tv5.setPadding(10)
            tbrow1.addView(tv5)
            val qv=EditText(context)
            qv.layoutParams=params
            qv.textSize=20F
            qv.setPadding(10)
            qv.hint="Enter quantity"
            tbrow1.addView(qv)
            val button=Button(context)
            button.text = getString(R.string.apply)
            button.setOnClickListener {
                val quantity=qv.text.toString().trim { it <= ' ' }
                when{
                    quantity.isEmpty()->{
                        displayErrorSnackbar("Enter Quantity")
                    }
                    !quantity.isDigitsOnly()->{
                        displayErrorSnackbar("Enter valid quantity")
                    }
                    quantity.toInt()<=0->{
                        displayErrorSnackbar("Quantity should be greater than 0")
                    }
                    else->{
                        val date = Calendar.getInstance().time
                        val formatter = SimpleDateFormat.getDateTimeInstance() //or use getDateInstance()
                        val formatedDate = formatter.format(date)
                        val user_id= SharedPrefmanager.getInstance(requireContext().applicationContext).keyId.toInt()
                        val call=
                            RetrofitBondAndDepositInstance.getretrofitBondAndDepositConnection().create(
                                retrofitShareServices::class.java).buyshare(
                                BShare(user_id,amountList[i]*quantity.toLong(),quantity.toLong(),formatedDate)
                            )
                        call.enqueue(object : Callback, retrofit2.Callback<ResponseBody> {
                            override fun onResponse(
                                call: Call<ResponseBody>,
                                response: Response<ResponseBody>
                            ) {
                                if(response.isSuccessful)
                                {

                                    Log.v("Api Call","${response.body()!!}")
                                    Toast.makeText(activity as Context, "Share Applied Successfully!", Toast.LENGTH_LONG)
                                        .show()
                                }
                                else
                                    Log.d("Api Call","Bad Response Code")
                            }

                            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                                Toast.makeText(activity as Context,"Exception Occurred: ${t.message} ",Toast.LENGTH_LONG).show()
                            }
                        })
                    }
                }

            }
            button.setLayoutParams(paramsz)


            tbrow1.addView(button)
            sharetable.addView(tbrow1)
        }
    }


    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment ShareFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            ShareFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
    private fun displayErrorSnackbar(text: String) {
        val snackbar = Snackbar.make(
            requireActivity().findViewById(android.R.id.content),
            text,
            Snackbar.LENGTH_LONG
        )
        val view = snackbar.view
        view.setBackgroundColor(ContextCompat.getColor(activity as Context, R.color.error))
        snackbar.show()

    }
}
