package com.bsr.bsrcoin.Noitification

import android.content.Context
import android.util.Log
import com.android.volley.RequestQueue
import com.android.volley.toolbox.Volley
import org.json.JSONObject
import com.android.volley.toolbox.JsonObjectRequest
import kotlin.Throws
import com.android.volley.AuthFailureError
import com.android.volley.Response
import org.json.JSONException
import java.util.HashMap

class FcmNotificationsSender(
    var receiverFCMToken: String,
    var title: String,
    var body: String,
    var mContext: Context,
) {
    private var requestQueue: RequestQueue? = null
    private val postUrl = "https://fcm.googleapis.com/fcm/send"
    private val fcmServerKey =
        "AAAAQQ_32vQ:APA91bHguCpw-V8wmAXChY7N5gqp5oMGS7dahN9fg6Yxib3dgngedGcL8SB5CrPAzS3o_xqCU7lLwY6bHRJykKhUVZOHxRVg4gG72gbuf4iwZIqNMQNXn_kfSoWA0pCZimdNow4iRf01"

    fun SendNotifications() {
        requestQueue = Volley.newRequestQueue(mContext)
        val mainObj = JSONObject()
        Log.e("TAG", "SendNotifications: checking the notifications" )
        try {
            mainObj.put("to", receiverFCMToken)
            val notiObject = JSONObject()
            notiObject.put("title", title)
            notiObject.put("body", body)
            notiObject.put("icon", "bill") // enter icon that exists in drawable only
            mainObj.put("notification", notiObject)
            val request: JsonObjectRequest =
                object : JsonObjectRequest(Method.POST, postUrl, mainObj, Response.Listener {
                    // code run is got response
                }, Response.ErrorListener {
                    // code run is got error
                }) {
                    @Throws(AuthFailureError::class)
                    override fun getHeaders(): Map<String, String> {
                        val header: MutableMap<String, String> = HashMap()
                        header["content-type"] = "application/json"
                        header["authorization"] = "key=$fcmServerKey"
                        return header
                    }
                }
            requestQueue!!.add(request)
        } catch (e: JSONException) {
            e.printStackTrace()
        }
    }
}