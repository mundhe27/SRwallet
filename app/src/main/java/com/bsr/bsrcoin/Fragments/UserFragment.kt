package com.bsr.bsrcoin.Fragments

import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.content.ContextCompat
import com.bsr.bsrcoin.Adapter.ViewPagerAdapter
import com.bsr.bsrcoin.R

import com.bsr.bsrcoin.SharedPrefmanager
import com.bsr.bsrcoin.databinding.FragmentUserBinding
import com.canhub.cropper.CropImageContract
import com.canhub.cropper.CropImageView
import com.canhub.cropper.options
import com.google.android.material.tabs.TabLayoutMediator
import com.karumi.dexter.Dexter
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionDeniedResponse
import com.karumi.dexter.listener.PermissionGrantedResponse
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.single.PermissionListener
import java.net.URI

private const val TAG = "UserFragment"
class UserFragment : Fragment() {

private lateinit var binding : FragmentUserBinding
var filePath:String? = ""
    private val cropImage = registerForActivityResult(CropImageContract()) { result ->
        if(result.isSuccessful) {
            val uriContent = result.uriContent
            val uriFilePath = result.getUriFilePath(requireContext())
            SharedPrefmanager.getInstance(context).userDP(uriContent.toString(),
                SharedPrefmanager.getInstance(context).keyId)
            binding.dpimg.setImageURI(uriContent)
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
        binding = FragmentUserBinding.inflate(inflater,container,false)
        // Inflate the layout for this fragment

        val adapter = ViewPagerAdapter(childFragmentManager,lifecycle)

        binding.viewPager.adapter = adapter

        TabLayoutMediator(binding.tablayout,binding.viewPager){tab,position->
            when(position){
                1-> tab.text="Documents"
                else -> tab.text="Profile"
            }
        }.attach()

         return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val userDpUri = SharedPrefmanager.getInstance(context).dpUri(SharedPrefmanager.getInstance(context).keyId)
        if(!(userDpUri.equals(""))){
            val uri = Uri.parse(userDpUri)
            binding.dpimg.setImageURI(uri)
        }


        binding.editdp.setOnClickListener {
            isChangeDp()
        }
    }

    private fun isChangeDp() {
        var optionChosen = -1
        val actionsArray = arrayOf("Remove image","Choose image")
        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle("Edit")
        if(SharedPrefmanager.getInstance(requireContext()).dpUri(SharedPrefmanager.getInstance(context).keyId)!=""){
        builder.setItems(actionsArray,DialogInterface.OnClickListener { dialogInterface, i ->
//            Toast.makeText(context,"$i",Toast.LENGTH_SHORT).show()
                if(i==0) {binding.dpimg.setImageDrawable(AppCompatResources
                    .getDrawable(requireContext(),R.drawable.ic_baseline_account_circle_24))}
                else chooseImg()
        })
            builder.show()}
        else{
            chooseImg()
        }



    }

    private fun startCrop(){
        cropImage.launch(options { setGuidelines(CropImageView.Guidelines.ON) })
    }

    private fun chooseImg(){
        if (ContextCompat.checkSelfPermission(
                requireActivity(),
                android.Manifest.permission.READ_EXTERNAL_STORAGE
            ) != PackageManager.PERMISSION_GRANTED
        ) {


            Dexter.withContext(requireContext())
                .withPermission(android.Manifest.permission.READ_EXTERNAL_STORAGE)
                .withListener(object : PermissionListener {
                    override fun onPermissionGranted(p0: PermissionGrantedResponse?) {
                        startCrop()
                    }

                    override fun onPermissionDenied(p0: PermissionDeniedResponse?) {
                        if (p0 != null) {
                            if (p0.isPermanentlyDenied) {
                                val alertbuilder =
                                    android.app.AlertDialog.Builder(requireContext())
                                alertbuilder.setTitle("Permission Required")
                                    .setMessage("Permission required to access your device storage to pick image. \n Please Enable in Settings")
                                    .setPositiveButton("OK") { _: DialogInterface, _: Int ->
                                        val intent = Intent()
                                        intent.action =
                                            Settings.ACTION_APPLICATION_DETAILS_SETTINGS
                                        intent.data = Uri.fromParts(
                                            "package",
                                            requireActivity().packageName,
                                            null
                                        )
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
