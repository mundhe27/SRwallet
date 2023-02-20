package com.bsr.bsrcoin.Fragments

import android.app.AlertDialog
import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.bsr.bsrcoin.Database.TransactionDao
import com.bsr.bsrcoin.Database.TransactionEntity
import com.bsr.bsrcoin.R
import java.text.SimpleDateFormat
import java.util.*

class HomeFragment : Fragment() {

    var db: TransactionDao? = null
    var courses = arrayOf<String?>("INR", "USD", "JPY", "GBP", "AED", "AUD")
    var wallet = arrayOf<String?>("SR-COIN", "D-CART", "C-CART")

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // Inflate the layout for this fragment
        val v = inflater.inflate(R.layout.fragment_home, container, false)


        val send_money_button = v.findViewById<Button>(R.id.send_money_btn)
        val amount = v.findViewById<EditText>(R.id.send_amount)
        val account_number = v.findViewById<EditText>(R.id.account_number_of_beneficiary)
        val remark = v.findViewById<EditText>(R.id.remark)
        val spin = v.findViewById<Spinner>(R.id.currency)

        spin.adapter = ArrayAdapter<String>(activity as Context, android.R.layout.simple_spinner_dropdown_item, courses)
        var selectedcurrency: String ?= courses[0]
        spin.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                    selectedcurrency= courses[p2]
                }

                override fun onNothingSelected(p0: AdapterView<*>?) {

                }

            }

        val spin2 = v.findViewById<Spinner>(R.id.wallet1)

        spin2.adapter = ArrayAdapter<String>(activity as Context, android.R.layout.simple_spinner_dropdown_item, wallet)
        var selectedwallet1: String ?= wallet[0]
        var selectedwallet2: String ?= wallet[0]

        spin2.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                    selectedwallet1 = wallet[p2]
                    set_note(v,selectedwallet1,selectedwallet2)
                }

                override fun onNothingSelected(p0: AdapterView<*>?) {

                }

            }

        val spin3 = v.findViewById<Spinner>(R.id.waller2)

        spin3.adapter = ArrayAdapter<String>(activity as Context, android.R.layout.simple_spinner_dropdown_item, wallet)

        spin3.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                    selectedwallet2=wallet[p2]
                    set_note(v,selectedwallet1,selectedwallet2)
                }

                override fun onNothingSelected(p0: AdapterView<*>?) {

                }

            }


        send_money_button.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                val amount_s = amount.text.toString().trim()
                val account_s = account_number.text.toString().trim()
                val remark_s = remark.text.toString().trim()

                if (amount_s.isEmpty()) {
                    amount.setError("Empty Field")

                } else if (account_s.isEmpty()) {
                    account_number.setError("Empty Field")
                } else if (remark_s.isEmpty()) {
                    remark.setError("Empty Field")

                } else {

                    val builder = AlertDialog.Builder(context)
                    builder.setTitle("Send Money Alert")
                    builder.setMessage("Are you sure about this transaction?")
                    builder.setIcon(android.R.drawable.ic_dialog_alert)
                    builder.setPositiveButton(android.R.string.yes) { dialog, which ->

                        val date = Calendar.getInstance().time
                        val formatter = SimpleDateFormat.getDateTimeInstance() //or use getDateInstance()
                        val formatedDate = formatter.format(date)
                        val parsedInt = account_s.toLong()
                        val double1 = amount_s.toDouble()

                        Log.v("------->",formatedDate);

                        /*val te=TransactionEntity(transactionId = 3984,double1,formatedDate,parsedInt,parsedInt);

                        db?.insertTransaction(te)

                        Toast.makeText(
                            context,
                            "Transaction Successful..., See details on transaction page!",
                            Toast.LENGTH_SHORT
                        ).show()
                        amount.setText("")
                        account_number.setText("")
                        remark.setText("")*/

                    }
                    builder.setNegativeButton(android.R.string.no) { dialog, which ->
                        Toast.makeText(
                            context,
                            android.R.string.no, Toast.LENGTH_SHORT
                        ).show()
                    }
                    builder.show()



                }
            }
        })


        return v;
    }

    private fun set_note(view: View, selectedwallet1: String?, selectedwallet2: String?) {

        val note = view.findViewById<TextView>(R.id.note)

        if(selectedwallet1.equals(selectedwallet2))
        {
            note.setText("**NOTE: 0% fee & Direct Transaction!")
        }
        else if(selectedwallet1.equals("SR-COIN") && selectedwallet2.equals("C-CART"))
        {
            note.setText("**NOTE: 2% fee & Transaction has to verify from admin/agent!")
        }
        else if(selectedwallet1.equals("C-CART") && selectedwallet2.equals("SR-COIN"))
        {
            note.setText("**NOTE: 1% fee & Transaction has to verify from admin/agent!")
        }
        else if(selectedwallet1.equals("SR-COIN") && selectedwallet2.equals("D-CART"))
        {
            note.setText("**NOTE: 2% fee & Transaction has to verify from admin/agent!")
        }
        else if(selectedwallet1.equals("D-CART") && selectedwallet2.equals("SR-COIN"))
        {
            note.setText("**NOTE: 1% fee & Transaction has to verify from admin/agent!")
        }
        else if(selectedwallet1.equals("D-CART") && selectedwallet2.equals("C-CART"))
        {
            note.setText("**NOTE: 2% fee & Transaction has to verify from admin/agent!")
        }
        else if(selectedwallet1.equals("C-CART") && selectedwallet2.equals("D-CART"))
        {
            note.setText("**NOTE: 1% fee & Transaction has to verify from admin/agent!")
        }

    }
}