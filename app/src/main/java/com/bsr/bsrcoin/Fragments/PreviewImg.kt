package com.bsr.bsrcoin.Fragments

import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.bsr.bsrcoin.R


class PreviewImg(val imguri:String) : Fragment() {


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_preview_img, container, false)
//        val uri = this.getArguments()?.getString("uri")
//        Log.e("TAG", "innew class: $uri" )
//        // Inflate the layout for this fragment

        view.findViewById<ImageView>(R.id.preview).setImageURI(Uri.parse(imguri))
        val activity = activity as AppCompatActivity
        activity.supportActionBar?.hide()
        return view
    }


}