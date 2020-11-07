package com.example.notificationpusher

import android.app.NotificationChannel
import android.app.Service
import android.content.Intent
import android.content.Intent.getIntent
import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import android.os.IBinder
import android.os.UserHandle
import android.service.notification.NotificationListenerService
import android.service.notification.StatusBarNotification
import android.util.Log
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import java.lang.Exception


class NotificationListener : NotificationListenerService() {
    var intent: Intent? = null;

    override fun onCreate() {
        super.onCreate()
        Log.i("test", "Created")
    }

    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        this.intent = intent
        return Service.START_STICKY
    }

    override fun onBind(intent: Intent): IBinder? {
        this.intent = intent
        Log.i("test", intent.toString())
        return super.onBind(intent)
    }

    override fun onNotificationPosted(sbn: StatusBarNotification) {
        // super.onNotificationPosted(sbn);
        Log.i("test", sbn.toString())
        // Instantiate the RequestQueue.
        val queue = Volley.newRequestQueue(this)
        val url = "https://api.pushover.net/1/messages.json"
        try {
            val token = intent!!.extras!!.get("token")
            val user_id = intent!!.extras!!.get("user_id")
            // Request a string response from the provided URL.
            val stringRequest = object: StringRequest(Request.Method.POST, url,
                    Response.Listener<String> { response -> Log.i("test", response)
                    },
                    Response.ErrorListener {error ->
                        Log.i("test", error.networkResponse.toString()) })  {
                override fun getHeaders(): MutableMap<String, String> {
                    val headers = HashMap<String, String>()
                    headers["Content-Type"] = "application/x-www-form-urlencoded"
                    return headers
                }

                override fun getBody(): ByteArray {
                    val pm = applicationContext.packageManager
                    var ai: ApplicationInfo? = null
                    try {
                        ai = pm.getApplicationInfo(sbn.packageName, 0)
                    } catch (e: PackageManager.NameNotFoundException) {

                    }
                    val applicationName = (if (ai != null) pm.getApplicationLabel(ai) else "(unknown)") as String
                    val body = "token=" + token + "&user=" + user_id + "&title="+ applicationName + "&message=" + sbn.notification.tickerText
                    Log.i("test", body)
                    return (body).toByteArray()
                }
            };
            // Add the request to the RequestQueue.
            queue.add(stringRequest)
        } catch (e: Exception) {
            Log.e("test", e.toString())
        }

    }

    override fun onNotificationChannelModified(pkg: String, user: UserHandle, channel: NotificationChannel, modificationType: Int) {
        // super.onNotificationChannelModified(pkg, user, channel, modificationType);
        Log.i("test", pkg)
    }
}