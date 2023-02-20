package com.bsr.bsrcoin

import android.app.AlertDialog
import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.Toast
import com.bsr.bsrcoin.databinding.FragmentConvertorBinding


class Convertor : Fragment(),View.OnClickListener {
    private val TAG = "Convertor"
    var wallets = arrayOf<String?>("SR-COIN", "D-CART", "C-CART", "N-CART", "T-CART", "M-CART")
    var currencies = arrayOf<String?>("INR", "BSR")
    var currencies1 = arrayOf<String?>("BSR", "INR")
    private lateinit var binding: FragmentConvertorBinding
    private lateinit var amnt: EditText

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentConvertorBinding.inflate(inflater, container, false)
        amnt = binding.amount

        binding.wallet.adapter = ArrayAdapter<String>(
            activity as Context,
            android.R.layout.simple_spinner_dropdown_item, wallets
        )
        binding.convertMoneyBtn.setOnClickListener(this)

var selectedWallet : String? = wallets[0]
        binding.wallet.onItemSelectedListener
        object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                selectedWallet = wallets[p2]
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {

            }

        }
        var fromCurr: String? = currencies1[0]
        binding.curr1.adapter = ArrayAdapter<String>(
            activity as Context,
            android.R.layout.simple_spinner_dropdown_item, currencies1
        )
        binding.curr1.onItemSelectedListener
        object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                fromCurr = currencies1[p2]
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {

            }

        }

        var toCurr: String? = currencies[0]
        binding.curr2.adapter = ArrayAdapter<String>(
            activity as Context,
            android.R.layout.simple_spinner_dropdown_item, currencies
        )
        binding.curr2.onItemSelectedListener
        object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                toCurr = currencies[p2]
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {

            }

        }

        binding.unit50.setOnClickListener(this)
        binding.unit100.setOnClickListener(this)
        binding.unit150.setOnClickListener(this)
        binding.unit200.setOnClickListener(this)

        return binding.root
    }

    override fun onClick(p0: View?) {
        when (p0!!.id) {
            R.id.unit50 -> {
                var amount = amnt.text.toString()
                if (amount == "") amount = "0"
                amnt.setText((amount.toInt() + 50).toString())
            }
            R.id.unit100 -> {
                var amount = amnt.text.toString()
                if (amount == "") amount = "0"
                amnt.setText((amount.toInt() + 100).toString())
            }
            R.id.unit150 -> {
                var amount = amnt.text.toString()
                if (amount == "") amount = "0"
                amnt.setText((amount.toInt() + 150).toString())
            }
            R.id.unit200 -> {
                var amount = amnt.text.toString()
                if (amount == "") amount = "0"
                amnt.setText((amount.toInt() + 200).toString())
            }
            R.id.convert_money_btn -> {
                Log.e(TAG, "onClick: ${amnt.equals("")}", )
                if (amnt.text.toString().trim().equals("")) {
                    amnt.setError("required field!")
                    amnt.requestFocus()
                } else if (binding.remark.text.toString().trim().equals("")) {
                    binding.remark.setError("required field!")
                    binding.remark.requestFocus()
                } else  {
                    val builder = AlertDialog.Builder(context)
                    builder.setTitle("Transaction Status")
                    builder.setMessage("Are you sure you want to proceed?")
                    builder.setIcon(android.R.drawable.alert_light_frame)
                    builder.setPositiveButton("Yes") { dialog, which ->
                        Toast.makeText(context, "Conversion request sent!!", Toast.LENGTH_SHORT)
                            .show()
                    }
                    builder.setNegativeButton("No") { dialog, which ->
                        Toast.makeText(
                            context,
                            android.R.string.no, Toast.LENGTH_SHORT
                        ).show()
                    }
                    builder.show()
                }

            }
        }


    }
}