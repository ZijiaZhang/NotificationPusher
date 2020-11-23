package com.example.notificationpusher

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.appcompat.widget.AppCompatEditText
import androidx.navigation.fragment.findNavController

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class FirstFragment : Fragment() {

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val root = inflater.inflate(R.layout.fragment_first, container, false)

        val tokenText = root.findViewById<AppCompatEditText>(R.id.text_input_token)
        val userIdText = root.findViewById<AppCompatEditText>(R.id.text_input_user_id)
        val settings = activity?.applicationContext?.getSharedPreferences("saveData", 0)
        if (settings != null) {
            tokenText.setText(settings.getString("token", ""))
            userIdText.setText(settings.getString("user_id", ""))
        }

        root.findViewById<Button>(R.id.button_first).setOnClickListener { view ->
            Log.i("test", "saved")
            (activity as MainActivity).myIntent!!.putExtra("token", tokenText.text)
            (activity as MainActivity).myIntent!!.putExtra("user_id", userIdText.text)
            (activity as MainActivity).myIntent!!.putExtra("PushOver", true)

            val settings = (activity as MainActivity).applicationContext.getSharedPreferences("saveData", 0)
            val editor = settings.edit()
            editor.putString("token", tokenText.text.toString())
            editor.putString("user_id", userIdText.text.toString())
            editor.apply();
            (activity as MainActivity).restartService((activity as MainActivity).myIntent!!)
        }

        return root
    }
}