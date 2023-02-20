package com.bsr.bsrcoin.Fragments

import android.annotation.SuppressLint
import android.app.ProgressDialog
import android.content.Context
import android.os.Bundle
import android.view.*
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.AppCompatButton
import androidx.fragment.app.Fragment
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.bsr.bsrcoin.MysqlConst.Constants
import com.bsr.bsrcoin.R
import com.bsr.bsrcoin.SharedPrefmanager
import com.bsr.bsrcoin.databinding.FragmentChequeBinding
import org.json.JSONException
import org.json.JSONObject

class ChequeFragment : Fragment() {
    private var cheques = 0
    private lateinit var binding: FragmentChequeBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_cheque, container, false)
        binding = FragmentChequeBinding.bind(view)
        val progressDialog = ProgressDialog(requireContext())
        progressDialog.setCancelable(false)
        progressDialog.setMessage("Loading...")
        progressDialog.show()

        Volley.newRequestQueue(requireContext()).add(
            @SuppressLint("SetTextI18n")
            object : StringRequest(
                Method.POST,
                Constants.url_read + SharedPrefmanager.getInstance(requireContext().applicationContext).keyId,
                { s ->
                    progressDialog.dismiss()
                    try{
                        val obj = JSONObject(s).getJSONArray("user").getJSONObject(0).getString("cheque")
                        cheques = obj.toInt()
                        view.findViewById<TextView>(R.id.text_cheque_count).text = "Cheques Available : $cheques"
                    } catch(e: JSONException){ e.stackTrace }
                },
                { e ->
                    e.stackTrace
                    progressDialog.dismiss()
                }
            ) {
                override fun getParams(): MutableMap<String, String> {
                    return HashMap()
                }
            }
        )
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setHasOptionsMenu(true)
        val viewCheque = view.findViewById<TextView>(R.id.txt_cview)
        val sendCheque = view.findViewById<TextView>(R.id.txt_csend)
        viewCheque.setTextColor(resources.getColor(R.color.green))
        parentFragmentManager.beginTransaction().replace(R.id.fragment_cheque_container, ViewChequeFragment()).commit()

        viewCheque.setOnClickListener {
            viewCheque.setTextColor(resources.getColor(R.color.green))
            sendCheque.setTextColor(resources.getColor(R.color.black))
            parentFragmentManager.beginTransaction().replace(R.id.fragment_cheque_container, ViewChequeFragment()).commit()
        }

        sendCheque.setOnClickListener {
            if(cheques > 0) {
                sendCheque.setTextColor(resources.getColor(R.color.green))
                viewCheque.setTextColor(resources.getColor(R.color.black))
                parentFragmentManager.beginTransaction().replace(R.id.fragment_cheque_container, SendChequeFragment()).commit()
            } else {
                Toast.makeText(requireContext(), "You don't have any cheque to send! \n Please add cheques", Toast.LENGTH_LONG).show()
            }
        }

    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.cheque_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            R.id.addCheque -> {
                val d_view = LayoutInflater.from(requireContext()).inflate(R.layout.add_cheque_dialog, requireActivity().findViewById(R.id.add_cheque_layout))

                val amt_edit = d_view.findViewById<EditText>(R.id.edit_cheque_no)
                val add = d_view.findViewById<AppCompatButton>(R.id.addcheque_btn)
                val cancel = d_view.findViewById<AppCompatButton>(R.id.cancelcheque_btn)
                val spin = d_view.findViewById<Spinner>(R.id.walletspin)
                val wallets = arrayOf("Select Wallet", "Nwallet", "Cwallet")
                spin.adapter = ArrayAdapter(activity as Context, android.R.layout.simple_spinner_dropdown_item, wallets)
                val dialog = AlertDialog.Builder(requireContext())
                    .setView(d_view)
                    .create()
                dialog.setCancelable(false)

                cancel.setOnClickListener{
                    dialog.dismiss()
                }

                add.setOnClickListener {
                    val warray = arrayOf("","wallet1","wallet3")
                    val wallet = warray[spin.selectedItemPosition]
                    val qty = amt_edit.text.toString()

                    if(wallet != "") {
                        if(qty != ""){
                            val progressDialog = ProgressDialog(requireContext())
                            progressDialog.setCancelable(false)
                            progressDialog.setMessage("Loading...")
                            progressDialog.show()

                            Volley.newRequestQueue(requireContext()).add(
                                object : StringRequest(
                                    Method.POST,
                                    Constants.url_add_cheque,
                                    { s ->
                                        progressDialog.dismiss()
                                        try{
                                            val obj = JSONObject(s)
                                            Toast.makeText(requireContext(), obj.getString("message"), Toast.LENGTH_LONG).show()
                                            dialog.dismiss()
                                            if(!obj.getBoolean("emsg")){
                                                cheques = qty.toInt()
                                                binding.textChequeCount.text = "Cheques Available : $cheques"
                                            }
                                        } catch(e: JSONException){ e.stackTrace }
                                    },
                                    { e ->
                                        e.stackTrace
                                        dialog.dismiss()
                                        progressDialog.dismiss()
                                    }
                                ) {
                                    override fun getParams(): MutableMap<String, String> {
                                        val params : MutableMap<String, String> = HashMap()
                                        params["userId"] = SharedPrefmanager.getInstance(requireContext().applicationContext).keyId
                                        params["ccount"] = qty
                                        params["walletName"] = wallet
                                        return params
                                    }
                                }
                            )
                        } else {
                            amt_edit.error = "Please Enter valid Number"
                        }
                    } else{
                        Toast.makeText(requireContext(), "Select Wallet", Toast.LENGTH_LONG).show()
                    }
                }
                dialog.show()
                return true
            }
            else -> return super.onOptionsItemSelected(item)
        }
    }
}