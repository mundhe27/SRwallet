package com.bsr.bsrcoin.Fragments

import android.annotation.SuppressLint
import android.app.ProgressDialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.text.TextUtils
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.content.ContextCompat
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.androidnetworking.AndroidNetworking
import com.androidnetworking.common.Priority
import com.androidnetworking.error.ANError
import com.androidnetworking.interfaces.JSONObjectRequestListener
import com.google.android.material.snackbar.Snackbar
import com.bsr.bsrcoin.MysqlConst.Constants
import com.bsr.bsrcoin.R
import com.bsr.bsrcoin.SharedPrefmanager
import com.bsr.bsrcoin.ViewModels.InsuranceViewModel
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


class InsuranceFragment : Fragment() {
    lateinit var etAmount: EditText
    lateinit var dropdown_menu_insurance: Spinner
    lateinit var dropdown_menu_agent: Spinner
    lateinit var btnApply: Button
    lateinit var tvDuration: TextView
    lateinit var dropdown_menu_duration: Spinner
    lateinit var insurance_image : ImageView
    private var filePath : String? = null


    private val cropImage = registerForActivityResult(CropImageContract()) { result ->
        if(result.isSuccessful) {
            val uriContent = result.uriContent
            val uriFilePath = result.getUriFilePath(requireContext())
            insurance_image.setImageDrawable(Drawable.createFromStream(uriContent?.let {
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


    @SuppressLint("CutPasteId")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        AndroidNetworking.initialize(requireContext().applicationContext)

        val view = inflater.inflate(R.layout.fragment_insurance, container, false)
        etAmount = view.findViewById(R.id.etAmount)
        dropdown_menu_insurance = view.findViewById(R.id.dropdown_menu_insurance)
        dropdown_menu_agent = view.findViewById(R.id.dropdown_menu_agent)
        btnApply = view.findViewById(R.id.btnApply)
        tvDuration = view.findViewById(R.id.tvDuration)
        dropdown_menu_duration = view.findViewById(R.id.dropdown_menu_duration)
        insurance_image = view.findViewById(R.id.insurance_image)
        dropdown_menu_duration.visibility = View.GONE
        val w_spin = view.findViewById<Spinner>(R.id.wallet_spinner)
        val c_spin = view.findViewById<Spinner>(R.id.currency_spinner)

        val insurance = arrayOf("Life insurance",
            "Health insurance",
            "Device insurance",
            "Jewel insurance",
            "House insurance",
            "Team insurance",
            "Death insurance",
            "Business insurance")

        val wallet = arrayOf("Nwallet", "Mwallet")
        val w = arrayOf("wallet1","wallet5")
        val currency = arrayOf("INR","BSR")
        c_spin.adapter = ArrayAdapter(
            activity as Context,
            R.layout.support_simple_spinner_dropdown_item,
            currency
        )
        w_spin.adapter = ArrayAdapter(
            activity as Context,
            R.layout.support_simple_spinner_dropdown_item,
            wallet
        )

        var selectedInsurance: String = insurance[0]
        dropdown_menu_insurance.adapter = ArrayAdapter(
            activity as Context,
            R.layout.support_simple_spinner_dropdown_item,
            insurance
        )
        dropdown_menu_insurance.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                    selectedInsurance = insurance[p2]
                    val text = "Duration:"
                    dropdown_menu_duration.visibility = View.VISIBLE

                    tvDuration.text = text
                }

                override fun onNothingSelected(p0: AdapterView<*>?) {

                }

            }
        //Agent part
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
        val TermInsuranceDuration= arrayOf("1","2","3","4","5","6","7","8","9","10","11","12","13","14","15")
        dropdown_menu_duration.adapter=ArrayAdapter(activity as Context,R.layout.support_simple_spinner_dropdown_item,TermInsuranceDuration)
        var exactDuration=TermInsuranceDuration[0]
        dropdown_menu_duration.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                    exactDuration=TermInsuranceDuration[p2]
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
                    displayErrorSnackbar("Please Select An Agent")
                }
                filePath == null || filePath == "" -> {
                    displayErrorSnackbar("Please upload Document ex your Aadhar")
                }
                else -> {
                    val Duration = "$exactDuration Years"

                    val progressDialog = ProgressDialog(requireContext())
                    progressDialog.setMessage("Please Wait... \n Applying for Insurance")
                    progressDialog.setCancelable(false)
                    progressDialog.max = 100
                    progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL)
                    progressDialog.show()

                    AndroidNetworking.upload(Constants.url_insurance_create)
                        .addMultipartFile("image", File(filePath))
                        .addMultipartParameter(
                            "userId",
                            SharedPrefmanager.getInstance(requireContext().applicationContext).keyId
                        )
                        .addMultipartParameter("insurance_amount", amt)
                        .addMultipartParameter("insurance_type", selectedInsurance)
                        .addMultipartParameter("agent_name", selectedAgent)
                        .addMultipartParameter("duration", Duration)
                        .addMultipartParameter("wallet", w[w_spin.selectedItemPosition])
                        .addMultipartParameter("currency", currency[c_spin.selectedItemPosition])
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
                                    insurance_image.setImageResource(R.drawable.upl_img)
                                }
                            }

                            override fun onError(anError: ANError?) {
                                progressDialog.dismiss()
                                anError!!.stackTrace
                                filePath = ""
                                insurance_image.setImageResource(R.drawable.upl_img)
                            }

                        })


                }
            }
        }
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        insurance_image.setOnClickListener {
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

    fun displayErrorSnackbar(text: String) {
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