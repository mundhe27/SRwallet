package com.bsr.bsrcoin.Fragments

import android.app.ProgressDialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.renderscript.RenderScript
import android.text.TextUtils
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.content.ContextCompat
import com.android.volley.Request
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.androidnetworking.AndroidNetworking
import com.androidnetworking.common.Priority
import com.androidnetworking.error.ANError
import com.androidnetworking.interfaces.JSONObjectRequestListener
import com.androidnetworking.interfaces.StringRequestListener
import com.google.android.material.snackbar.Snackbar
import com.bsr.bsrcoin.MysqlConst.Constants
import com.bsr.bsrcoin.R
import com.bsr.bsrcoin.SharedPrefmanager
import com.canhub.cropper.CropImage
import com.canhub.cropper.CropImageContract
import com.canhub.cropper.CropImageView
import com.canhub.cropper.options
import com.karumi.dexter.Dexter
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionDeniedResponse
import com.karumi.dexter.listener.PermissionGrantedResponse
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.single.PermissionListener
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.io.File


class LoansFragment : Fragment() {
    lateinit var etAmount: EditText
    lateinit var dropdown_menu_loantype: Spinner
    lateinit var dropdown_menu_agent: Spinner
    lateinit var btnApply: Button
    lateinit var dropdown_menu_duration: Spinner
    lateinit var tvInterest:TextView
    lateinit var loan_image : ImageView
    private var filePath : String? = null


    private val cropImage = registerForActivityResult(CropImageContract()) { result ->
        if(result.isSuccessful) {
            val uriContent = result.uriContent
            val uriFilePath = result.getUriFilePath(requireContext())
            loan_image.setImageDrawable(Drawable.createFromStream(uriContent?.let {
                requireContext().contentResolver.openInputStream(
                    it
                )
            }, null))
            filePath = uriFilePath
            //Toast.makeText(requireContext(),"uri, ${uriContent?.path.toString()} \n filepath $filePath", Toast.LENGTH_LONG).show()

        } else {
            val exception = result.error
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        AndroidNetworking.initialize(requireContext().applicationContext)

        val view = inflater.inflate(R.layout.fragment_loans, container, false)
        etAmount = view.findViewById(R.id.etAmount)
        dropdown_menu_loantype = view.findViewById(R.id.dropdown_menu_loantype)
        dropdown_menu_agent = view.findViewById(R.id.dropdown_menu_agent)
        dropdown_menu_duration = view.findViewById(R.id.dropdown_menu_duration)
        btnApply = view.findViewById(R.id.btnApply)
        tvInterest=view.findViewById(R.id.tvInterest)
        loan_image = view.findViewById(R.id.loan_image)


        var duration = arrayOf("1 year", "1.5 years", "2 years", "3 years", "4 or more Years")
        val type = arrayOf("Select Loan","Jewel loan",
            "Vehicle loan",
            "Device loan",
            "House loan",
            "Business loan",
            "Credit loan",
            "2days loan")

        var selectedLoan: String = type[0]
        dropdown_menu_loantype.adapter = ArrayAdapter<String>(
            activity as Context,
            R.layout.support_simple_spinner_dropdown_item,
            type
        )

        dropdown_menu_loantype.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                    selectedLoan = type[p2]
                    duration = when (selectedLoan) {
                        type[0] -> arrayOf("")
                        type[1] -> {
                            arrayOf("1 year", "1.5 years", "2 years", "3 years", "4 or more Years")
                        }
                        type[7] -> arrayOf("2 days")
                        else -> {
                            arrayOf("Upto 2 years", "3-5 years", "More than 5 years")
                        }
                    }

                    if(p2 == 0 || p2 == 7) {
                        loan_image.visibility = View.GONE
                    } else {
                        loan_image.visibility = View.VISIBLE
                    }

                    dropdown_menu_duration.adapter = ArrayAdapter(
                        activity as Context,
                        R.layout.support_simple_spinner_dropdown_item,
                        duration
                    )
                }

                override fun onNothingSelected(p0: AdapterView<*>?) {

                }

            }
        var selectedDuration = duration[0]
        val rateOfInterest: Float = 8.8f
        dropdown_menu_duration.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                    selectedDuration = duration[p2]

                    val tvint=getString(R.string.interest_rate)+rateOfInterest
                    tvInterest.text=tvint
                }

                override fun onNothingSelected(p0: AdapterView<*>?) {

                }

            }

        //TODO:Add agents in below list
        val options = ArrayList<String>()
        options.add("Select Agent")
        var selectedAgent: String = options[0]

        val progressDialog = ProgressDialog(requireContext())
        progressDialog.setCancelable(false)
        progressDialog.setMessage("Loading Agents....")
        progressDialog.show()

        Volley.newRequestQueue(requireContext()).add(
            object : StringRequest(
                Method.POST,
                Constants.url_agent,
                { s ->
                    progressDialog.dismiss()
                    try {
                        val array = JSONArray(s)
                        for (i in 0 until array.length()) {
                            val agent = array.getJSONObject(i)
                            options.add(agent.getString("name"))
                        }
                        dropdown_menu_agent.adapter = ArrayAdapter(
                            activity as Context,
                            R.layout.support_simple_spinner_dropdown_item,
                            options
                        )
                    } catch ( e: JSONException) { e.stackTrace }
                },
                { error -> error.stackTrace
                    progressDialog.dismiss()
                }
            ){
                override fun getParams(): MutableMap<String, String> {
                    val params : MutableMap<String, String> = HashMap()
                    return params
                }
            }
        )


        dropdown_menu_agent.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                selectedAgent = options[p2]
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {

            }

        }
        btnApply.setOnClickListener {
            val amt = etAmount.text.toString().trim { it <= ' ' }
            when {
                TextUtils.isEmpty(amt) -> {
                    displayErrorSnackbar("Please Enter Amount")
                }
                selectedAgent == options[0] -> {
                    displayErrorSnackbar("Please Select an Agent")
                }
                dropdown_menu_loantype.selectedItemPosition == 0 ->{
                    displayErrorSnackbar("Please Select Loan Type")
                }
                dropdown_menu_loantype.selectedItemPosition != 7 && filePath == null -> {
                    displayErrorSnackbar("Please Upload The Image of Entity You are applying Loan For")
                }
                else -> {
                    val progressDialog = ProgressDialog(requireContext())
                    progressDialog.setMessage("Please Wait... \n Applying for Loan")
                    progressDialog.setCancelable(false)
                    progressDialog.max = 100
                    progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL)
                    progressDialog.show()

                    if(filePath != null && filePath != "") {
                        AndroidNetworking.upload(Constants.url_loan_create)
                            .addMultipartFile("image", File(filePath))
                            .addMultipartParameter(
                                "userId",
                                SharedPrefmanager.getInstance(requireContext().applicationContext).keyId
                            )
                            .addMultipartParameter("loan_amount", amt)
                            .addMultipartParameter("loan_type", selectedLoan)
                            .addMultipartParameter("agent_name", selectedAgent)
                            .addMultipartParameter("rate_of_Interest", rateOfInterest.toString())
                            .addMultipartParameter("duration", selectedDuration)
                            .setPriority(Priority.HIGH)
                            .build()
                            .setUploadProgressListener { bytesUploaded, totalBytes ->
                                val progress: Float = (bytesUploaded / totalBytes * 100).toFloat()
                                progressDialog.progress = progress.toInt()
                            }
                            .getAsJSONObject(object : JSONObjectRequestListener {
                                override fun onResponse(response: JSONObject?) {
                                    progressDialog.dismiss()
                                    if (response != null) {
                                        Toast.makeText(
                                            requireContext(),
                                            response.getString("message"),
                                            Toast.LENGTH_SHORT
                                        ).show()
                                        etAmount.setText("")
                                        filePath = ""
                                        loan_image.setImageResource(R.drawable.upl_img)
                                    }
                                }

                                override fun onError(anError: ANError?) {
                                    progressDialog.dismiss()
                                    anError!!.stackTrace
                                    filePath = ""
                                    loan_image.setImageResource(R.drawable.upl_img)
                                }

                            })
                    } else {
                        AndroidNetworking.upload(Constants.url_loan_create)
                            .addMultipartParameter(
                                "userId",
                                SharedPrefmanager.getInstance(requireContext().applicationContext).keyId
                            )
                            .addMultipartParameter("loan_amount", amt)
                            .addMultipartParameter("loan_type", selectedLoan)
                            .addMultipartParameter("agent_name", selectedAgent)
                            .addMultipartParameter("rate_of_Interest", rateOfInterest.toString())
                            .addMultipartParameter("duration", selectedDuration)
                            .setPriority(Priority.HIGH)
                            .build()
                            .setUploadProgressListener { bytesUploaded, totalBytes ->
                                val progress: Float = (bytesUploaded / totalBytes * 100).toFloat()
                                progressDialog.progress = progress.toInt()
                            }
                            .getAsJSONObject(object : JSONObjectRequestListener {
                                override fun onResponse(response: JSONObject?) {
                                    progressDialog.dismiss()
                                    if (response != null) {
                                        Toast.makeText(
                                            requireContext(),
                                            response.getString("message"),
                                            Toast.LENGTH_SHORT
                                        ).show()
                                        etAmount.setText("")
                                        filePath = ""
                                    }
                                }

                                override fun onError(anError: ANError?) {
                                    progressDialog.dismiss()
                                    anError!!.stackTrace
                                    filePath = ""
                                }

                            })
                    }
                }
            }
        }




        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        loan_image.setOnClickListener {
            if(ContextCompat.checkSelfPermission(requireActivity(), android.Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)
            {
                Dexter.withContext(requireContext())
                    .withPermission(android.Manifest.permission.READ_EXTERNAL_STORAGE)
                    .withListener(object : PermissionListener {
                        override fun onPermissionGranted(p0: PermissionGrantedResponse?) {
                            startCrop()
                        }

                        override fun onPermissionDenied(p0: PermissionDeniedResponse?) {
                            if (p0 != null) {
                                if(p0.isPermanentlyDenied) {
                                    val alertbuilder = android.app.AlertDialog.Builder(requireContext())
                                    alertbuilder.setTitle("Permission Required")
                                        .setMessage("Permission required to access your device storage to pick image. \n Please Enable in Settings")
                                        .setPositiveButton("OK") { _: DialogInterface, _: Int ->
                                            val intent = Intent()
                                            intent.action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
                                            intent.data = Uri.fromParts("package", requireActivity().packageName, null)
                                            startActivityForResult(intent, 51)
                                        }
                                        .setNegativeButton("Cancel", null)
                                        .show()
                                }
                            }
                        }

                        override fun onPermissionRationaleShouldBeShown(
                            p0: PermissionRequest?,
                            p1: PermissionToken?
                        ) {
                            p1?.continuePermissionRequest()
                        }

                    }).check()
            } else {
                startCrop()
            }
        }
    }

    fun startCrop(){
        cropImage.launch(
            options {
                setGuidelines(CropImageView.Guidelines.ON)
            }
        )
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