package com.bsr.bsrcoin.Fragments

import android.annotation.SuppressLint
import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.bsr.bsrcoin.MainActivity
import com.bsr.bsrcoin.MysqlConst.Constants
import com.bsr.bsrcoin.R
import com.bsr.bsrcoin.payments.PayUsingRazorpay
import org.json.JSONObject

class AddMoneytoWalletFragment : Fragment() {
    private lateinit var coinprice : ArrayList<String>
    private val note : String = "One Bsr Coin = "
    private val wallet_s = arrayOf("Select Wallet","N-wallet","T-wallet","C-wallet","D-wallet","M-wallet")
    private val currency_s = arrayOf("Select Currency","INR","BSR")

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        coinprice = ArrayList()
        coinprice.add("0")

        val progressBar = ProgressDialog(requireContext())
        progressBar.setMessage("Loading")
        progressBar.setCancelable(false)
        progressBar.show()
        Volley.newRequestQueue(requireContext()).add(
            object : StringRequest(
                Method.GET,
                Constants.url_get_coin_price,
                { s->
                    progressBar.dismiss()
                    val array = JSONObject(s).getJSONArray("coin")
                    for (i in 0 until array.length()) {
                        coinprice.add(array.getJSONObject(i).getString("coin_inr_value"))
                    }
                },
                { progressBar.dismiss() }
            ){}
        )

        return inflater.inflate(R.layout.fragment_add_moneyto_wallet, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val wallet = view.findViewById<Spinner>(R.id.walletName)
        val currency = view.findViewById<Spinner>(R.id.currency)
        val amount = view.findViewById<EditText>(R.id.edit_amount)
        val t_note = view.findViewById<TextView>(R.id.notetext)
        val addMoney = view.findViewById<Button>(R.id.addMoneyNow)
        var selectedCurrency : String = currency_s[0]
        var selectedWallet : String = wallet_s[0]

        wallet.adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_dropdown_item, wallet_s)
        currency.adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_dropdown_item, currency_s)
        t_note.visibility = View.GONE
        currency.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                selectedCurrency = currency_s[p2]
                if(p2 == 2) {
                    t_note.visibility = View.VISIBLE
                }else {
                    t_note.visibility = View.GONE
                }
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
                TODO("Not yet implemented")
            }
        }

        wallet.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            @SuppressLint("SetTextI18n")
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                selectedWallet = wallet_s[p2]
                val t = coinprice[p2]
                t_note.text = "$note $t INR"
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
                TODO("Not yet implemented")
            }
        }

        addMoney.setOnClickListener{
            var amount_s = amount.text.toString()
            val coin = amount.text.toString()
            if (currency.selectedItemPosition == 2)
                amount_s = (amount_s.toDouble() * coinprice[wallet.selectedItemPosition].toDouble()).toString()
            if (amount_s == "")
                amount.error = "Enter Amount"
            else if (selectedWallet == wallet_s[0])
                Toast.makeText(requireContext(), "Please Select Wallet", Toast.LENGTH_SHORT).show()
            else if (selectedCurrency == currency_s[0])
                Toast.makeText(requireContext(), "Please Select Currency", Toast.LENGTH_SHORT).show()
            else {
                startActivity(Intent((activity as MainActivity), PayUsingRazorpay::class.java)
                    .putExtra("name","Add Money To wallet")
                    .putExtra("amount", amount_s)
                    .putExtra("type", "add")
                    .putExtra("wid",wallet.selectedItemPosition.toString())
                    .putExtra("curr", currency_s[currency.selectedItemPosition])
                    .putExtra("coin",coin))


                amount.setText("")
                currency.setSelection(0)
                wallet.setSelection(0)
            }

        }
    }
}