package com.bsr.bsrcoin.Fragments

import android.annotation.SuppressLint
import android.app.ProgressDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.bsr.bsrcoin.Adapter.ViewInsuranceAdapter
import com.bsr.bsrcoin.Models.LoanModel
import com.bsr.bsrcoin.MysqlConst.Constants
import com.bsr.bsrcoin.R
import com.bsr.bsrcoin.SharedPrefmanager
import org.json.JSONException
import org.json.JSONObject

class ViewInsuranceFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_view_insurance, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val loanModels = ArrayList<LoanModel>()
        val insuranceAdapter = ViewInsuranceAdapter(requireContext(), loanModels, requireActivity())
        val recyclerView = view.findViewById<RecyclerView>(R.id.vins)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = insuranceAdapter

        val progressDialog = ProgressDialog(requireContext())
        progressDialog.setCancelable(false)
        progressDialog.setMessage("Loading Insurances..")
        progressDialog.show()

        Volley.newRequestQueue(requireContext()).add(
            @SuppressLint("NotifyDataSetChanged")
            object : StringRequest(
                Method.POST,
                Constants.url_insurance_view,
                { s ->
                    progressDialog.dismiss()
                    try {
                        val obj = JSONObject(s)
                        val array = obj.getJSONArray("loan")
                        for (i in 0 until array.length()) {
                            val loan = array.getJSONObject(i)
                            loanModels.add(
                                LoanModel(
                                    loan.getString("userID"),
                                    loan.getString("loanId"),
                                    loan.getString("loan_type"),
                                    loan.getString("duration"),
                                    loan.getString("loan_amount"),
                                    "",
                                    loan.getString("agent_name"),
                                    loan.getString("status"),
                                    loan.getString("update"),
                                    loan.getString("reqinr"),
                                    loan.getString("image")
                                )
                            )
                        }
                        insuranceAdapter.notifyDataSetChanged()

                    } catch (e: JSONException) { e.stackTrace}
                },
                {
                    progressDialog.dismiss()
                }
            ){
                override fun getParams(): MutableMap<String, String> {
                    val params: MutableMap<String, String> = HashMap()
                    when {
                        SharedPrefmanager.getInstance(requireContext().applicationContext).isAgent -> {
                            params["name"] = SharedPrefmanager.getInstance(requireContext().applicationContext).keyUsernameName
                            params["id"] = ""
                        }
                        SharedPrefmanager.getInstance(requireContext().applicationContext).isAdmin -> {
                            params["admin"] = "yes"
                        }
                        else -> {
                            params["name"] = ""
                            params["id"] = SharedPrefmanager.getInstance(requireContext().applicationContext).keyId
                        }
                    }
                    return params
                }
            }
        )

    }
}