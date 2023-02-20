package com.bsr.bsrcoin.Fragments

import android.annotation.SuppressLint
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.TextView
import android.widget.Toast
import androidx.cardview.widget.CardView
import com.android.volley.Request
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.bsr.bsrcoin.Convertor
import com.bsr.bsrcoin.MainActivity
import com.bsr.bsrcoin.MysqlConst.Constants
import com.bsr.bsrcoin.R
import com.bsr.bsrcoin.SharedPrefmanager
import com.bsr.bsrcoin.databinding.FragmentMyAccountBinding
import com.mikhaellopez.circularimageview.CircularImageView
import org.json.JSONObject


class MyAccountFragment : Fragment(),View.OnClickListener {

    private val TAG = "MyAccountFragment"
    private lateinit var v: View
    private lateinit var binding : FragmentMyAccountBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentMyAccountBinding.inflate(inflater,container,false)
        v = inflater.inflate(R.layout.fragment_my_account, container, false)
        v.findViewById<TextView>(R.id.lastlogintime).text = SharedPrefmanager.getInstance(context).
                                                            getLl(
                                                                SharedPrefmanager.getInstance(
                                                                    context
                                                                ).keyId)

        return v
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val userDpUri = SharedPrefmanager.getInstance(context)
            .dpUri(SharedPrefmanager.getInstance(context).keyId)
        if(!(userDpUri.equals(""))){
            val uri = Uri.parse(userDpUri)
            view.findViewById<CircularImageView>(R.id.userdp).setImageURI(uri)
        }


        val userAcc = view.findViewById<TextView>(R.id.accNumber)
        userAcc.text = SharedPrefmanager.getInstance(context).keyAccount

        view.findViewById<CircularImageView>(R.id.userdp).setOnClickListener(this)
        view.findViewById<CardView>(R.id.myac).setOnClickListener(this)
        view.findViewById<CardView>(R.id.pay_bill).setOnClickListener(this)
        view.findViewById<CardView>(R.id.user).setOnClickListener(this)
        view.findViewById<CardView>(R.id.chequeServ).setOnClickListener(this)
        view.findViewById<CardView>(R.id.fund_transfer).setOnClickListener(this)
        view.findViewById<CardView>(R.id.fundMgmt).setOnClickListener(this)
        view.findViewById<CardView>(R.id.converter).setOnClickListener(this)
//        view.findViewById<FloatingActionButton>(R.id.query).setOnClickListener(this)


        if(SharedPrefmanager.getInstance(requireContext().applicationContext).isAdmin){
            setHasOptionsMenu(true)
        }
        sendProfileRequest(SharedPrefmanager.getInstance(context).keyId)


    }

    override fun onClick(p0: View?) {
        when(p0?.id){
            R.id.fund_transfer -> { parentFragmentManager.beginTransaction().
                                    replace(R.id.Frame, SendMoneyFragment()).addToBackStack(null).commit()}
            R.id.myac -> {parentFragmentManager.beginTransaction()
                            .replace(R.id.Frame, MyAccFragment()).addToBackStack(null).commit()}
            R.id.pay_bill -> {
                Toast.makeText(context,"Will be implemented soon...",Toast.LENGTH_SHORT).show()}
            R.id.chequeServ -> {
                parentFragmentManager.beginTransaction().replace(R.id.Frame, ChequeFragment()).addToBackStack(null).commit() }
            R.id.fundMgmt -> {
                parentFragmentManager.beginTransaction().replace(R.id.Frame,FundManagementFragment()).addToBackStack(null).commit()

            }

            R.id.user -> {parentFragmentManager.beginTransaction()
                .replace(R.id.Frame, UserFragment()).addToBackStack(null).commit()}
            R.id.userdp ->{
                parentFragmentManager.beginTransaction()
                    .replace(R.id.Frame, UserFragment()).addToBackStack(null).commit()
            }
            R.id.converter -> {
                parentFragmentManager.beginTransaction().replace(R.id.Frame, Convertor()).commit()
            }
//            R.id.query ->
//            {
//                val intent = Intent(context,ChatActivity::class.java)
//                startActivity(intent)
//            }
            }
        }


    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.request, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {

            R.id.addMoneyRequest -> {
                parentFragmentManager.beginTransaction().replace(R.id.Frame, AddMoneyRequestFragment()).addToBackStack(null).commit()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    @SuppressLint("SetTextI18n")
    private fun sendProfileRequest(userId: String?) {
        val url = Constants.url_get_userInfo + "?id=$userId"
        val queue = Volley.newRequestQueue(context)

        val request = StringRequest(Request.Method.GET, url,{ response ->
            val res = JSONObject(response)
            val array = res.getJSONArray("user")
            val obj = array.getJSONObject(0)

            val balance = obj.getInt("wallet1") + obj.getInt("wallet2") + obj.getInt("wallet3")
                            + obj.getInt("wallet4") + obj.getInt("wallet5")

            Log.e(TAG, "sendProfileRequest: $response" )
            v.findViewById<TextView>(R.id.username).text = "   " + obj.getString("name")
            v.findViewById<TextView>(R.id.Accbalance).text = "₹ "+ balance.toString()
//            if(obj.getString("lastLogin")==""){
//                v.findViewById<LinearLayout>(R.id.lastloginstat).visibility = View.GONE
//
//            }
//            else{
//                v.findViewById<TextView>(R.id.lastlogintime).visibility= View.VISIBLE
//                v.findViewById<TextView>(R.id.lastlogintime).text = obj.getString("lastLogin")
//            }



        },{error -> Log.e(TAG, "sendProfileRequest: $error" )})

        queue.add(request)

    }

}
