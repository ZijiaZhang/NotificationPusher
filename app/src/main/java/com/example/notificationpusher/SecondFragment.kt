package com.example.notificationpusher

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.appcompat.widget.AppCompatEditText
import androidx.fragment.app.Fragment


/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class SecondFragment : Fragment() {
    lateinit var root: View;
    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        root = inflater.inflate(R.layout.fragment_second, container, false)

        val uuidText = root.findViewById<AppCompatEditText>(R.id.text_input_uuid)
        val settings = activity?.applicationContext?.getSharedPreferences("saveData", 0)
        if (settings != null) {
            uuidText.setText(settings.getString("uuid", ""))
        }

        root.findViewById<Button>(R.id.button_second).setOnClickListener { view ->
            Log.i("test", "saved")
            (activity as MainActivity).myIntent!!.putExtra("uuid", uuidText.text)
            (activity as MainActivity).myIntent!!.putExtra("PushOver", false)

            val settings = (activity as MainActivity).applicationContext.getSharedPreferences("saveData", 0)
            val editor = settings.edit()
            editor.putString("uuid", uuidText.text.toString())
            editor.apply();
            (activity as MainActivity).restartService((activity as MainActivity).myIntent!!)
        }

        root.findViewById<Button>(R.id.button_scan).setOnClickListener { view ->
            val intent = Intent(activity, ScannerActivity::class.java)
            startActivityForResult(intent, 0)

        }



        return root
    }

    override fun onActivityResult(
        requestCode: Int,
        resultCode: Int,
        data: Intent?
    ) {
        when (requestCode) {
            0 -> {
                if (data != null) {
                    root.findViewById<AppCompatEditText>(R.id.text_input_uuid).setText(data.extras!!.get("code") as String)
                }
            }
        }
    }
}