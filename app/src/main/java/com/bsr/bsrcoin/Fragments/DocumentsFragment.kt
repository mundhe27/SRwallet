package com.bsr.bsrcoin.Fragments

import android.app.ProgressDialog
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Bundle
import android.provider.OpenableColumns
import android.provider.Settings
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import com.android.volley.Request
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.androidnetworking.AndroidNetworking
import com.androidnetworking.common.Priority
import com.androidnetworking.error.ANError
import com.androidnetworking.interfaces.JSONObjectRequestListener
import com.bsr.bsrcoin.MysqlConst.Constants
import com.bsr.bsrcoin.R
import com.bsr.bsrcoin.SharedPrefmanager
import com.bsr.bsrcoin.databinding.FragmentDocumentsBinding
import com.bumptech.glide.Glide
import com.canhub.cropper.CropImageContract
import com.canhub.cropper.CropImageView
import com.canhub.cropper.options
import com.karumi.dexter.Dexter
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionDeniedResponse
import com.karumi.dexter.listener.PermissionGrantedResponse
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.single.PermissionListener
import org.json.JSONObject
import java.io.File
import java.net.URI
import java.net.URL
import java.util.*
import kotlin.collections.ArrayList

private const val TAG = "DocumentsFragment"
class DocumentsFragment : Fragment(),View.OnClickListener {
    //we planned to take the display names of the images from the shared preferences but to handle cases where the
    //data of the app is lost the file name from the url received from the api will be used


    //callflags for the add buttons are as follows:
//    0->called by adhaar card section
//    1->called by driving license section
//    2->called by pan card section

    //callflags for viewing and removing the images are as follows:
//    0-> called by first img of adhaar
//    1-> called by second img of adhaar
//    2-> called by first img of dl
//    3-> called by second img of dl
//    4-> called by first img of pan card

    private var callflag = -1
    private lateinit var binding : FragmentDocumentsBinding
    private val picuris = ArrayList<ArrayList<Uri?>>()
    private lateinit var rootview : View
//    private var imguri : Uri? = null
    private var imgdisplayname = ArrayList<ArrayList<String?>>()
    private var filepaths = ArrayList<ArrayList<String?>>()
    private var picurl = ArrayList<ArrayList<URL>>()


    private val cropImage = registerForActivityResult(CropImageContract()) { result ->
        if(result.isSuccessful) {

            picuris[callflag].add(result.uriContent)
            filepaths[callflag].add(result.getUriFilePath(requireContext()))

            if(picuris[0].size==2 && picuris[1].size==2 && picuris[2].size==1) binding.uploadDocs.isEnabled = true
            requireContext().contentResolver.query(result.originalUri!!,null,null,null,null)
                .use { cursor ->
                   val name = cursor?.getColumnIndex(OpenableColumns.DISPLAY_NAME)
                    cursor?.moveToFirst()
                    imgdisplayname[callflag].add(cursor?.getString(name!!))
                    
            }
            updateUi()
            Log.e(TAG, "cropImgeresult: picuris $picuris" )
            Log.e(TAG, "cropImgeresult: imgdisplayname $imgdisplayname" )
            //Toast.makeText(requireContext(),"uri, ${uriContent?.path.toString()} \n filepath $filePath", Toast.LENGTH_LONG).show()
//            Log.e(TAG, "checking the uri: $uriFilePath", )
        } else {
            val exception = result.error
        }
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        for(i in 0..2) {
            picuris.add(ArrayList())
            imgdisplayname.add(ArrayList())
            filepaths.add(ArrayList())
            picurl.add(ArrayList())}
            binding = FragmentDocumentsBinding.inflate(inflater,container,false)
            getDocUrl()
            rootview = binding.root
            setClickListeners()


        return binding.root
    }



    override fun onClick(p0: View?) {
        when(p0?.id){
            R.id.adadd -> callflag=0
            R.id.dladd -> callflag=1
            R.id.panadd -> callflag=2

        }

        Log.e(TAG, "onClick: callflag: $callflag " )
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
        Log.e(TAG, "onClick: abhi hua ye ghatna", )
    }






fun startCrop(){
    cropImage.launch(
        options {
            setGuidelines(CropImageView.Guidelines.ON)
        }
    )
}
    private fun updateUiFromUrl() {
        binding.adimgname1.text = imgdisplayname[0][0]
        binding.adimg1.visibility = View.VISIBLE
        binding.adadd.isEnabled = false
        binding.adimgname2.text = imgdisplayname[0][1]
        binding.adimg2.visibility = View.VISIBLE
        binding.adsucc.visibility = View.VISIBLE
        binding.adpending.visibility = View.GONE
        binding.scroll.scrollTo(0,0)

        binding.dlimgname1.text = imgdisplayname[1][0]
        binding.dlimg1.visibility = View.VISIBLE
        binding.dladd.isEnabled = false
        binding.dlimgname2.text = imgdisplayname[1][1]
        binding.dlimg2.visibility = View.VISIBLE
        binding.dlpending.visibility = View.GONE
        binding.dlsucc.visibility = View.VISIBLE

        binding.panimagename1.text = imgdisplayname[2][0]
        binding.panimg.visibility = View.VISIBLE
        binding.panadd.isEnabled = false
        binding.panpending.visibility = View.GONE
        binding.pansucc.visibility = View.VISIBLE
        binding.scroll.scrollTo(0,0)

    }
    fun updateUi(){

        when(callflag){
            0->{
                val numofpics = picuris[0].size

                if(numofpics==1){
                    binding.adimgname1.text = imgdisplayname[0][0]
                    binding.adimg1.visibility = View.VISIBLE
                }
                else if(numofpics==2){
                    binding.adimgname1.text = imgdisplayname[0][0]
                    binding.adimg1.visibility = View.VISIBLE
                    binding.adadd.isEnabled = false
                    binding.adimgname2.text = imgdisplayname[0][1]
                    binding.adimg2.visibility = View.VISIBLE
                    binding.adsucc.visibility = View.VISIBLE
                    binding.adpending.visibility = View.GONE
                    binding.scroll.scrollTo(0,0)
                }
            }
            1->{
                val numofpics = picuris[1].size

                if(numofpics==1){
                    binding.dlimgname1.text = imgdisplayname[1][0]
                    binding.dlimg1.visibility = View.VISIBLE
                }
                else if(numofpics==2){
                    binding.dlimgname1.text = imgdisplayname[1][0]
                    binding.dlimg1.visibility = View.VISIBLE
                    binding.dladd.isEnabled = false
                    binding.dlimgname2.text = imgdisplayname[1][1]
                    binding.dlimg2.visibility = View.VISIBLE
                    binding.dlpending.visibility = View.GONE
                    binding.dlsucc.visibility = View.VISIBLE
                }
            }
            2->{
                val numofpics = picuris[2].size

                if(numofpics==1){
                    binding.panimagename1.text = imgdisplayname[2][0]
                    binding.panimg.visibility = View.VISIBLE
                    binding.panadd.isEnabled = false
                    binding.panpending.visibility = View.GONE
                    binding.pansucc.visibility = View.VISIBLE
                    binding.scroll.scrollTo(0,0)
                }

            }
        }

    }

    private fun setClickListeners() {
        binding.adadd.setOnClickListener(this)
        binding.dladd.setOnClickListener(this)
        binding.panadd.setOnClickListener(this)

        binding.uploadDocs.setOnClickListener {
            uploadDocs()
        }

        binding.previewadhaarimg1.setOnClickListener {
            if(picuris[0].size!=0){
            previewImage(picuris[0][0].toString(),0)}
            else previewImage(picurl[0][0].toString(),0)
        }
        binding.previewadhaarimg2.setOnClickListener {
            if(picuris[0].size!=0){
            previewImage(picuris[0][1]!!.toString(),0)}
            else previewImage(picurl[0][1].toString(),0)
        }
        binding.previewdlimg1.setOnClickListener {
            if(picuris[1].size!=0){
            previewImage(picuris[1][0]!!.toString(),1)}
            else previewImage(picurl[1][0].toString(),1)
        }
        binding.previewdlimg2.setOnClickListener {
            if(picuris[1].size!=0){
            previewImage(picuris[1][1]!!.toString(),1)}
            else previewImage(picurl[1][1].toString(),1)
        }
        binding.previewpanimg.setOnClickListener {
            if(picuris[2].size!=0){
            previewImage(picuris[2][0]!!.toString(),2)}
            else previewImage(picurl[2][0].toString(),2)
        }

        binding.removeadhaarimg1.setOnClickListener {
            val removeflag = 0
            removeImg(removeflag)
        }
        binding.removeadhaarimg2.setOnClickListener {
            val removeflag = 1
            removeImg(removeflag)
        }
        binding.removedlimg1.setOnClickListener {
            val removeflag = 2
            removeImg(removeflag)
        }
        binding.removedlimg2.setOnClickListener {
            val removeflag = 3
            removeImg(removeflag)
        }
        binding.removepanimg.setOnClickListener {
            val removeflag = 4
            removeImg(removeflag)
        }
        binding.adSeeLess.setOnClickListener {
            binding.imgpreviewad.visibility = View.GONE
        }
        binding.dlSeeLess.setOnClickListener {
            binding.imgpreviewdl.visibility = View.GONE
        }
        binding.panSeeLess.setOnClickListener {
            binding.imgpreviewpan.visibility = View.GONE
        }

    }

    private fun previewImage(imgres: String, flag: Int) {
    lateinit var img : ImageView

        if(flag==0) {img = binding.adpreviewImg
            binding.imgpreviewad.visibility = View.VISIBLE}

        else if(flag==1) {img = binding.dlpreviewImg
        binding.imgpreviewdl.visibility = View.VISIBLE}

        else {img = binding.panpreviewImg
            binding.imgpreviewpan.visibility = View.VISIBLE}

        Glide.with(requireContext()).load(imgres).into(img)

    }

    private fun removeImg(flag:Int){


        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle("Alert!!")
        builder.setMessage("Removing any one document will remove all the documents")
        builder.setMessage("Are you sure you want to continue?")
        builder.setPositiveButton("Yes",DialogInterface.OnClickListener { dialogInterface, i ->
            picuris.clear()
            imgdisplayname.clear()
            filepaths.clear()
            picurl.clear()
            for(i in 0..2) {
                picuris.add(ArrayList())
                imgdisplayname.add(ArrayList())
                filepaths.add(ArrayList())}
            binding.adimg1.visibility = View.GONE
            binding.adimg2.visibility = View.GONE
            binding.dlimg1.visibility = View.GONE
            binding.dlimg2.visibility = View.GONE
            binding.panimg.visibility = View.GONE
            binding.adsucc.visibility = View.GONE
            binding.adpending.visibility = View.VISIBLE
            binding.dlsucc.visibility = View.GONE
            binding.dlpending.visibility = View.VISIBLE
            binding.pansucc.visibility = View.GONE
            binding.panpending.visibility = View.VISIBLE
            binding.adadd.isEnabled = true
            binding.dladd.isEnabled = true
            binding.panadd.isEnabled = true


            Log.e(TAG, "picuris: $picuris" )
        })
        builder.setNegativeButton("No",DialogInterface.OnClickListener { dialogInterface, i ->

        })
        builder.show()

    }

    private fun uploadDocs(){
        val progressDialog = ProgressDialog(requireContext())
        progressDialog.setMessage("Please Wait!!")
        progressDialog.setTitle("Uploading...")
        progressDialog.setCancelable(false)
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER)
        progressDialog.show()

        Log.e(TAG, "uploadDocs: ${File((picuris[0][0]).toString())}" )
            AndroidNetworking.upload(Constants.url_upload_docs)
                .addMultipartFile("afront", File((filepaths[0][0]).toString()))
                .addMultipartParameter(
                    "userId",
                    SharedPrefmanager.getInstance(requireContext().applicationContext).keyId
                )
                .addMultipartFile("aback", File((filepaths[0][1]).toString()))
                .addMultipartFile("lfront", File((filepaths[1][0]).toString()))
                .addMultipartFile("lback", File((filepaths[1][1]).toString()))
                .addMultipartFile("pan", File((filepaths[2][0]).toString()))
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONObject(object : JSONObjectRequestListener {
                    override fun onResponse(response: JSONObject?) {
                        progressDialog.dismiss()
                        if (response != null) {
                            Toast.makeText(
                                requireContext(),
                                response.getString("message"),
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }

                    override fun onError(anError: ANError?) {
                        progressDialog.dismiss()
                        anError!!.stackTrace
                        Toast.makeText(requireContext(),"Error Occured in our server please try again${anError.errorBody}",Toast.LENGTH_SHORT).show()

                    }

                })

    }
    private fun getDocUrl(){
        val queue = Volley.newRequestQueue(context)
        val userId = SharedPrefmanager.getInstance(requireContext()).keyId
        val url = Constants.url_get_docs + "?userId=" + userId
        val request = StringRequest(Request.Method.GET,url,{response ->
            val resobject = JSONObject(response)
            val status = resobject.getString("status")
            binding.ui.visibility = View.VISIBLE
            binding.progressBar.visibility = View.GONE

            if(status == "200"){
                picurl[0].add(URL(resobject.getString("aadharf")))
                picurl[0].add(URL(resobject.getString("aadharb")))
                picurl[1].add(URL(resobject.getString("licensef")))
                picurl[1].add(URL(resobject.getString("licenseb")))
                picurl[2].add(URL(resobject.getString("pan")))
                getDisplayNameFromUrl()
                updateUiFromUrl()
            }

        }, {error->
            Log.e(TAG, "getDocUrl: There were some issues in the server${error.message}" )
            binding.progressBar.visibility = View.GONE
            Toast.makeText(context,"Error Occured, Please Try again later",Toast.LENGTH_SHORT).show()
        })
        queue.add(request)

    }


    private fun getDisplayNameFromUrl(){

        imgdisplayname[0].add(picurl[0][0].toString().substring(picurl[0][0].toString().lastIndexOf('/')+1))
        imgdisplayname[0].add(picurl[0][1].toString().substring(picurl[0][1].toString().lastIndexOf('/')+1))
        imgdisplayname[1].add(picurl[1][0].toString().substring(picurl[1][0].toString().lastIndexOf('/')+1))
        imgdisplayname[1].add(picurl[1][1].toString().substring(picurl[1][1].toString().lastIndexOf('/')+1))
        imgdisplayname[2].add(picurl[2][0].toString().substring(picurl[2][0].toString().lastIndexOf('/')+1))
    }
}




