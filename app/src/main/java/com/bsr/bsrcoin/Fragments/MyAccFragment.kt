package com.bsr.bsrcoin.Fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bsr.bsrcoin.Database.WalletModel
import com.bsr.bsrcoin.R
import com.bsr.bsrcoin.SharedPrefmanager
import com.bsr.bsrcoin.databinding.FragmentMyAccBinding
import com.bsr.bsrcoin.network.retroService
import com.bsr.bsrcoin.network.retrofitInstance
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import retrofit2.awaitResponse


class MyAccFragment : Fragment() {
    private val TAG = "MyAccFragment"
    private lateinit var binding: FragmentMyAccBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMyAccBinding.inflate(inflater, container, false)
        // Inflate the layout for this fragment


        Log.e(TAG, "onCreateView: ${SharedPrefmanager.getInstance(context).keyId}" )

        val api = retrofitInstance.getretrofitConnection().create(retroService::class.java)
        val activeUser = SharedPrefmanager.getInstance(context)

        //dispatching the request on the Main dispatcher and updating the UI with the results
        GlobalScope.launch(Dispatchers.Main) {
            val response = api.getUserWallets(activeUser.keyId).awaitResponse()
            if(response.isSuccessful && response.body()!=null){
                val responseWalletModel = response.body()
                val responseArray = responseWalletModel!!.Wallet
                updateUI(responseArray)
            }
            else{
                Log.e(TAG, response.toString())
            }
        }

        binding.addMoney.setOnClickListener {
            parentFragmentManager.beginTransaction().replace(R.id.Frame, AddMoneytoWalletFragment()).addToBackStack(null).commit()
        }

        return binding.root
    }

    //method to update the Ui according to the response returned by the server
    private fun updateUI(responseArray: Array<WalletModel>){
        binding.balance1.text = String.format("%.2f",responseArray[0].coin.toFloat())
        binding.balance2.text = String.format("%.2f",responseArray[1].coin.toFloat())
        binding.balance3.text = String.format("%.2f",responseArray[2].coin.toFloat())
        binding.balance4.text = String.format("%.2f",responseArray[3].coin.toFloat())
        binding.balance5.text = String.format("%.2f",responseArray[4].coin.toFloat())
    }



}


