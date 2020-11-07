package com.example.notificationpusher

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatEditText
import androidx.constraintlayout.widget.ConstraintLayout
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar


class MainActivity : AppCompatActivity() {
    var myIntent: Intent? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(findViewById(R.id.toolbar))

        findViewById<FloatingActionButton>(R.id.fab).setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show()
        }


        val firstFragment = findViewById<ConstraintLayout>(R.id.FirstFragment)
        val tokenText = firstFragment.getChildAt(2)  as AppCompatEditText
        val userIdText = firstFragment.getChildAt(4) as AppCompatEditText
        val settings = applicationContext.getSharedPreferences("saveData", 0)
        tokenText.setText(settings.getString("token", ""))
        userIdText.setText(settings.getString("user_id", ""))

        myIntent = Intent(this, NotificationListener::class.java).also { intent ->
            intent.putExtra("token", tokenText.text)
            intent.putExtra("user_id", userIdText.text)
            startService(intent)
            Log.i("test", "Service Created")
        }


        (firstFragment.getChildAt(5)).setOnClickListener { view ->
            Log.i("test", "saved")
            myIntent!!.putExtra("token", tokenText.text)
            myIntent!!.putExtra("user_id", userIdText.text)

            val settings = applicationContext.getSharedPreferences("saveData", 0)
            val editor = settings.edit()
            editor.putString("token", tokenText.text.toString())
            editor.putString("user_id", userIdText.text.toString())
            editor.apply();
            restartService(myIntent!!)
        }

    }


    fun restartService(intent: Intent){
        stopService(intent)
        startService(intent)
    }


    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_settings -> true
            else -> super.onOptionsItemSelected(item)
        }
    }

}