package com.bsr.bsrcoin.Fragments

import android.app.AlertDialog
import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.*
import com.android.volley.Request
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.bsr.bsrcoin.Database.TransactionDao
import com.bsr.bsrcoin.Noitification.FcmNotificationsSender
import com.bsr.bsrcoin.MysqlConst.Constants
import com.bsr.bsrcoin.R
import com.bsr.bsrcoin.SharedPrefmanager
import org.json.JSONArray
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

class SendMoneyFragment : Fragment(),View.OnClickListener {
    private val TAG = "SendMoneyFragment"
    var db: TransactionDao? = null
    var courses = arrayOf<String?>("INR","BSR")               //, "USD", "JPY", "GBP", "AED", "AUD")
    var wallet = arrayOf<String?>("NWallet", "TWallet", "CWallet", "DWallet", "MWallet")
    private lateinit var toSelfcheck: CheckBox
    private lateinit var beneAccNo: EditText
    private val userAccount = SharedPrefmanager.getInstance(context).keyAccount
    private val userId = SharedPrefmanager.getInstance(context).keyId
    private lateinit var amnt: EditText
    private lateinit var permissionBtn : Button
    private lateinit var remark : EditText
    //    private var case = 0
    private lateinit var sendBtn: Button
    private lateinit var handler: Handler
    private lateinit var progressBar: ProgressBar
    private val selectedWallets: ArrayList<String?> = ArrayList()



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        handler = Handler()
        // Inflate the layout for this fragment
         val v = inflater.inflate(R.layout.fragment_sendmoney, container, false)

        permissionBtn = v.findViewById<Button>(R.id.takePermission_btn)
        progressBar = v.findViewById(R.id.progressBar)
        sendBtn = v.findViewById<Button>(R.id.send_money_btn)
        toSelfcheck = v.findViewById(R.id.toSelf)
        beneAccNo = v.findViewById((R.id.account_number_of_beneficiary))
        amnt = v.findViewById(R.id.send_amount)
        //val rs50 =
        v.findViewById<Button>(R.id.Rs50).setOnClickListener(this)
        v.findViewById<Button>(R.id.Rs100).setOnClickListener(this)
        v.findViewById<Button>(R.id.Rs150).setOnClickListener(this)
        v.findViewById<Button>(R.id.Rs200).setOnClickListener(this)

        toSelfcheck.setOnCheckedChangeListener { compoundButton, isChecked ->
            if (isChecked) {

                v.findViewById<EditText>(R.id.account_number_of_beneficiary).setText(userAccount)
            } else {
                if (beneAccNo.text.toString().equals(userAccount)) {
                    toSelfcheck.isChecked = true
                }
            }
        }

        beneAccNo.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun afterTextChanged(p0: Editable?) {
//                val userInput: String? = p0.toString() ?: userAccount
                if (p0 != null) {

                    toSelfcheck.isChecked = p0.toString().equals(userAccount)
                } else {
                    beneAccNo.error = "empty field"
                }
            }

        })


//        val send_money_button = v.findViewById<Button>(R.id.send_money_btn)
//        val amount = v.findViewById<EditText>(R.id.send_amount)
//        val account_number = v.findViewById<EditText>(R.id.account_number_of_beneficiary)
        remark = v.findViewById<EditText>(R.id.remark)
        val spin = v.findViewById<Spinner>(R.id.currency)

        spin.adapter = ArrayAdapter<String>(
            activity as Context,
            android.R.layout.simple_spinner_dropdown_item,
            courses
        )
        var selectedcurrency: String? = courses[0]
        spin.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                    selectedcurrency = courses[p2]
                }

                override fun onNothingSelected(p0: AdapterView<*>?) {

                }

            }

        val spin2 = v.findViewById<Spinner>(R.id.wallet1)

        spin2.adapter = ArrayAdapter<String>(
            activity as Context,
            android.R.layout.simple_spinner_dropdown_item,
            wallet
        )

        var selectedwallet1 = wallet[0]
        var selectedwallet2 = wallet[0]
        selectedWallets.add(wallet[0])
        selectedWallets.add(wallet[0])


        spin2.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                    selectedWallets[0] = wallet[p2]
                    Log.e(TAG, "onItemSelected: ${selectedWallets[0]}" )
                    selectedwallet1 = wallet[p2]
                    setNoteButton(v, selectedWallets)
                }

                override fun onNothingSelected(p0: AdapterView<*>?) {

                }

            }

        val spin3 = v.findViewById<Spinner>(R.id.waller2)

        spin3.adapter = ArrayAdapter<String>(
            activity as Context,
            android.R.layout.simple_spinner_dropdown_item,
            wallet
        )

        spin3.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {

                    selectedWallets.set(1, wallet[p2])
                    Log.e(TAG, "onItemSelected: ${selectedWallets[1]}" )
                    selectedwallet2 = wallet[p2]
                    setNoteButton(v, selectedWallets)
                }

                override fun onNothingSelected(p0: AdapterView<*>?) {

                }

            }




        sendBtn.setOnClickListener {

            val im = requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            im.hideSoftInputFromWindow(sendBtn.windowToken,0)
            val amount_s = amnt.text.toString().trim()
            val account_s = beneAccNo.text.toString().trim()
            val remark_s = remark.text.toString().trim()

            if (amount_s.isEmpty()) {
                amnt.error = "Empty Field"

            } else if (account_s.isEmpty()) {
                beneAccNo.error = "Empty Field"
            } else if (remark_s.isEmpty()) {
                remark.error = "Empty Field"

            } else {

                val builder = AlertDialog.Builder(context)
                builder.setTitle("Send Money Alert")
                builder.setMessage("Are you sure about this transaction?")
                builder.setIcon(android.R.drawable.ic_dialog_alert)
                builder.setPositiveButton(android.R.string.yes) { dialog, which ->

                    val date = Calendar.getInstance().time
                    val formatter =
                        SimpleDateFormat.getDateTimeInstance() //or use getDateInstance()
                    val formatedDate = formatter.format(date)

                    val parsedInt = account_s.toLong()
                    val double1 = amount_s.toDouble()
                    progressBar.visibility=View.VISIBLE
                    val fromWallet = whichWallet(selectedWallets[0]!!)
                    val toWallet = whichWallet(selectedWallets[1]!!)
                    send_money(amount_s, account_s, remark_s, formatedDate, fromWallet, toWallet)
                    Log.v("------->", formatedDate);

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
        v.findViewById<Button>(R.id.takePermission_btn).setOnClickListener {
            val amount_s = amnt.text.toString().trim()
            val account_s = beneAccNo.text.toString().trim()
            val remark_s = remark.text.toString().trim()

            if (amount_s.isEmpty()) {
                amnt.error = "Empty Field"

            } else if (account_s.isEmpty()) {
                beneAccNo.error = "Empty Field"
            } else if (remark_s.isEmpty()) {
                remark.error = "Empty Field"

            } else {

                val builder = AlertDialog.Builder(context)
                builder.setTitle("Ask for Permission Alert")
                builder.setMessage("Are you sure about this transaction?")
                builder.setIcon(android.R.drawable.ic_dialog_alert)
                builder.setPositiveButton(android.R.string.yes) { dialog, which ->
                    val date = Calendar.getInstance().time
                    val formatter =
                        SimpleDateFormat.getDateTimeInstance() //or use getDateInstance()
                    val formatedDate = formatter.format(date)

                    progressBar.visibility = View.VISIBLE
                    val fromWallet = whichWallet(selectedWallets[0]!!)
                    val toWallet = whichWallet(selectedWallets[1]!!)
                    takePermission(amount_s, fromWallet, toWallet,userId,account_s, formatedDate)
                    permissionBtn.visibility=View.VISIBLE

//                    Log.v("------->", formatedDate);

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


        return v;
    }

    private fun takePermission(amountS: String, fromWallet: String,
                               toWallet: String, userId: String?, accountS: String, formatedDate: String) {

        val queue = Volley.newRequestQueue(context)
        var toUserId = ""



        val request = object : StringRequest(Method.POST,Constants.url_get_id,
            //response from the first request that is used to get the userId of the bene account
            {response ->
            Log.e(TAG, "sharedPreference userId: $userId ", )
            val res = JSONObject(response)
            toUserId = res.getString("userId")
            Log.e(TAG, "received userId: $toUserId" )

            val request2 = object : StringRequest(Request.Method.POST,Constants.url_take_permission,
                //response from the second request thatt is used to take the permission from the admin
                {response2->
                    val res1 = JSONObject(response2)
                    val permres = res1.getString("message")
                    confirmation(permres)
                    Log.e(TAG, "send_money: $permres was the msg sent by the server" )

                    if(permres.equals("Permission Asked successfully", ignoreCase = true)){
                        FcmNotificationsSender("cmsib4NqRsmNxiz7uAtEZ1:APA91bHzf48mNATMeCxx3wY-2Ud2Fh7ZxqaifGsoiQndc9NCoFooeEzqZoS8w5Do4vTukxHexm3Dg7AH_wOqyoUyKAmTwoxXBnnyP_Z_dsmbwW3Og0phQlRhDh4IlwYNqBVtvcla2K_0",
                            "Transaction alert","Request for Permission has been initiated",requireContext()).SendNotifications()
                    }

                },{ error->
                    setBlank()
                    Log.e(TAG, "takePermission: error" )
                    Toast.makeText(context,"Unfortunately some error occured please retry later!!",Toast.LENGTH_SHORT).show()}){
                override fun getParams(): MutableMap<String, String> {
                    Log.e(TAG, "getParams: I am in the getParams function", )
                    val hashMap = HashMap<String,String>()
                    hashMap.put("amount",amountS)
                    hashMap.put("fwalletName",fromWallet)
                    hashMap.put("twalletName",toWallet)
                    hashMap.put("from_id",userId!!)
                    hashMap.put("to_id",toUserId)
                    return hashMap
                }
            }
            queue.add(request2)



        }, { error -> Log.e(TAG, "takePermission: $error")
                setBlank()
        Toast.makeText(context,"Unfortunately some error occured please retry later!!",Toast.LENGTH_SHORT).show()}){
            override fun getParams(): MutableMap<String, String> {
                val hash = HashMap<String,String>()
                hash.put("account",accountS)
                return hash
            }
        }
        queue.add(request)



    }
    private fun setBlank(){
        beneAccNo.setText("")
        toSelfcheck.isChecked = false
        amnt.setText("")
        remark.setText("")
    }

    private fun confirmation(msg: String) {
        progressBar.visibility=View.GONE

        val builder = AlertDialog.Builder(context)
        builder.setTitle("Transaction Status")
        builder.setMessage(msg)
        builder.setIcon(R.drawable.ic_baseline_check_green)
        builder.setPositiveButton("Okay") { dialog, which ->

            parentFragmentManager.beginTransaction().replace(R.id.Frame, MyAccountFragment()).commit()


        }
        builder.setNegativeButton("New Transfer") { dialog, which ->
            setBlank()
        }
        builder.show()
    }

    private fun whichWallet(s: String): String {
        return when (s) {
            "NWallet" -> "wallet1"
            "TWallet" -> "wallet2"
            "CWallet" -> "wallet3"
            "DWallet" -> "wallet4"

            else -> "wallet5"
        }
    }

//    private fun set_note(view: View, selectedwallet1: String?, selectedwallet2: String?) {
//
//        val note = view.findViewById<TextView>(R.id.note)
//        val sendBtn = view.findViewById<Button>(R.id.send_money_btn)
//        val permissionBtn = view.findViewById<Button>(R.id.takePermission_btn)
//
//
//
//
//        if(selectedwallet1.equals("N-CART") && selectedwallet2.equals("T-CART")){
//
//        }
//        else if(selectedwallet1.equals(selectedwallet2))
//        {
//            note.setText("**NOTE: 0% fee & Direct Transaction!")
//            permissionBtn.visibility = View.GONE
//            sendBtn.visibility = View.VISIBLE
//        }
//        else if(selectedwallet1.equals("SR-COIN") && selectedwallet2.equals("C-CART"))
//        {
//            note.setText("**NOTE: 2% fee & Transaction has to verify from admin/agent!")
//            permissionBtn.visibility = View.VISIBLE
//            sendBtn.visibility = View.GONE
//
//        }
//        else if(selectedwallet1.equals("C-CART") && selectedwallet2.equals("SR-COIN"))
//        {
//            note.setText("**NOTE: 1% fee & Transaction has to verify from admin/agent!")
//            permissionBtn.visibility = View.VISIBLE
//            sendBtn.visibility = View.GONE
//        }
//        else if(selectedwallet1.equals("SR-COIN") && selectedwallet2.equals("D-CART"))
//        {
//            note.setText("**NOTE: 2% fee & Transaction has to verify from admin/agent!")
//            permissionBtn.visibility = View.VISIBLE
//            sendBtn.visibility = View.GONE
//        }
//        else if(selectedwallet1.equals("D-CART") && selectedwallet2.equals("SR-COIN"))
//        {
//            note.setText("**NOTE: 1% fee & Transaction has to verify from admin/agent!")
//            permissionBtn.visibility = View.VISIBLE
//            sendBtn.visibility = View.GONE
//        }
//        else if(selectedwallet1.equals("D-CART") && selectedwallet2.equals("C-CART"))
//        {
//            note.setText("**NOTE: 2% fee & Transaction has to verify from admin/agent!")
//            permissionBtn.visibility = View.VISIBLE
//            sendBtn.visibility = View.GONE
//        }
//        else if(selectedwallet1.equals("C-CART") && selectedwallet2.equals("D-CART"))
//        {
//            note.setText("**NOTE: 1% fee & Transaction has to verify from admin/agent!")
//            permissionBtn.visibility = View.VISIBLE
//            sendBtn.visibility = View.GONE
//        }
//
//        }
    fun setNoteButton(view: View, selectedWallets: ArrayList<String?>) {
        val note = view.findViewById<TextView>(R.id.note)


        Log.e(TAG, "setNoteButton: ${selectedWallets[0].equals(selectedWallets[1])}")

        if (selectedWallets.contains("NWallet") && selectedWallets.contains("TWallet")) {
            note.text = "**NOTE: 0.05% fee & Direct Transaction!"
            permissionBtn.visibility = View.GONE
            sendBtn.visibility = View.VISIBLE
//            case = 1
        } else if (selectedWallets.contains("CWallet") && (selectedWallets.contains("TWallet") || selectedWallets.contains(
                "NWallet"
            ) ||
                    selectedWallets.contains("DWallet") || selectedWallets.contains("MWallet"))
        ) {
//            case = 2
            permissionBtn.visibility = View.VISIBLE
            sendBtn.visibility = View.GONE
            note.text = "**NOTE: 3% fee & Transaction has to verify from admin/agent!"
        } else if (selectedWallets[0].equals(selectedWallets[1])) {
            Log.e(TAG, "setNoteButton: true")
            note.text = "**NOTE: 0% fee & Direct Transaction!"
            permissionBtn.visibility = View.GONE
            sendBtn.visibility = View.VISIBLE
//            case = 4
        } else if (!toSelfcheck.isChecked) {

            note.text = "**NOTE: 0.001% fee & Direct Transaction!"
            permissionBtn.visibility = View.GONE
            sendBtn.visibility = View.VISIBLE
        }

//        else if(toSelfcheck.isChecked && selectedWallets[0].equals(selectedWallets[1])){
//            note.text = "**NOTE: 0% fee & Direct Transaction!"
//            permissionBtn.visibility = View.GONE
//            sendBtn.visibility = View.VISIBLE
//            case = 5
//        }

    }


    override fun onClick(p0: View?) {
        Log.e(TAG, "onClick: fsafdsafasf")

        when (p0?.getId()) {
            R.id.Rs50 -> {
                var amount = amnt.text.toString()
                if (amount == "") amount = "0"
                amnt.setText((amount.toInt() + 50).toString())
            }
            R.id.Rs100 -> {
                var amount = amnt.text.toString()
                if (amount == "") amount = "0"
                amnt.setText((amount.toInt() + 100).toString())
            }
            R.id.Rs150 -> {
                var amount = amnt.text.toString()
                if (amount == "") amount = "0"
                amnt.setText((amount.toInt() + 150).toString())
            }
            R.id.Rs200 -> {
                var amount = amnt.text.toString()
                if (amount == "") amount = "0"
                amnt.setText((amount.toInt() + 200).toString())
            }

        }
    }

    private fun send_money(
        amount_s: String, account_s: String, remark_s: String,
        formatedDate: String, fromWallet: String, toWallet: String
    ) {

        val queue = Volley.newRequestQueue(context)
        val request =
            object : StringRequest(Request.Method.POST, Constants.url_sendMoney, { response ->
                val arr = JSONArray(response)
                val obj1 = arr.getJSONObject(arr.length()-1)
                val msg = arr.getJSONObject(0).getString("message")
                Log.e(TAG, "send_money: $arr" )
                confirmation(msg)
                try {
                    var counter = obj1.getInt("number of transactions")
                    var c=0
                    if(msg.equals("Money Sent Successfully ", ignoreCase=true)){
                        val token = arr.getJSONObject(0).getString("token");
                        val account = "XX" + account_s.substring(6)
                        Log.e(TAG, "send_money: notification sent" )
                        FcmNotificationsSender(SharedPrefmanager.getInstance(context).keyToken,"Transaction Alert",
                        "ALERT: BSR $amount_s has been debited from your account " +
                                "${"XX" + SharedPrefmanager.getInstance(context).keyAccount.substring(6)} " +
                                "from the ${selectedWallets[0]}. Tap to view your account balance",
                        requireContext()).SendNotifications()
                        FcmNotificationsSender(token,"Transaction Alert", "ALERT: BSR $amount_s has been credited " +
                                "to your account $account in the ${selectedWallets[1]}. Tap to view your account balance",requireContext()).SendNotifications()

                    while(c < counter){
                        c++
                        val token = arr.getJSONObject(c).getString("token")
                        Log.e(TAG, "send_money: $token")
                    }
                    }
                }
                catch (e:Exception){
                    Log.e(TAG, "send_money: counter is not present in the response from server", )
                }



//                Log.e(TAG, "send_money: $obj" )

//                Log.e(TAG, "send_money: $msg was the msg sent by the server" )
//                Log.e(TAG, "send_money: ${msg.compareTo("Money Sent Successfully ")}")
//                if(msg=="Money Sent Successfully "){
//                    Log.e(TAG, "send_money: the msg has matched ")
//                    FcmNotificationsSender("cmsib4NqRsmNxiz7uAtEZ1:APA91bHzf48mNATMeCxx3wY-2Ud2Fh7ZxqaifGsoiQndc9NCoFooeEzqZoS8w5Do4vTukxHexm3Dg7AH_wOqyoUyKAmTwoxXBnnyP_Z_dsmbwW3Og0phQlRhDh4IlwYNqBVtvcla2K_0",
//                        "Transaction alert","$amount_s has been debited from your account from the $fromWallet",requireContext()).SendNotifications()
//                }


            }, { error -> Log.e(TAG, "case1: ${error.message}")
            setBlank()}) {
                override fun getParams(): MutableMap<String, String> {
                    val hash = HashMap<String, String>()
                    hash["from_userId"] = userId
                    hash["from_account"] = userAccount
                    hash["to_account"] = account_s
                    hash["amount"] = amount_s
                    hash["from_wallet"] = fromWallet
                    hash["to_wallet"] = toWallet
                    hash["currency"] = "BSR"
                    Log.e(TAG, "getParams: $hash", )

                    return hash
                }
            }
        queue.add(request)

    }

//    private fun closeKeyBoard(v:View) {
//        val view: View? = v.findViewById(R.id.send_money_btn)
//        if (view != null) {
//
//            val input: InputMethodManager =
//                getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
//        }
//
//}

}


