package com.example.lab_3

import android.annotation.SuppressLint
import android.content.BroadcastReceiver
import android.content.ContentValues.TAG
import android.content.Context
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.net.ConnectivityManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.ContactsContract
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import java.net.HttpURLConnection
import java.net.URL
import kotlinx.coroutines.*
import java.lang.Error
import java.util.jar.Manifest

class MainActivity : AppCompatActivity() {

    private val READ_CONTACTS_PERMISSION_REQUEST_CODE = 1

    private val networkReceiver: BroadcastReceiver = NetworkReceiver()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        registerReceiver(networkReceiver, IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION))
    }

    override fun onDestroy() {
        unregisterReceiver(networkReceiver)
        super.onDestroy()
    }


    fun onGetClick(view: View) {
        val scope = CoroutineScope(Dispatchers.IO)
        val url = URL("https://jsonplaceholder.typicode.com/posts")
        scope.launch {
            with(url.openConnection() as HttpURLConnection) {
                requestMethod = "GET"
                println("Getting from $url")
                try {

                    inputStream.bufferedReader().use {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                            it.lines().forEach { line -> println(line) }
                        }
                    }
                }
                catch(e: Exception)
                {
                    println("Error: $e")
                }
            }
        }
    }

    fun onContactClick(view: View) {
        ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.READ_CONTACTS), READ_CONTACTS_PERMISSION_REQUEST_CODE)

    }

    @SuppressLint("Range")
    private fun readContacts(){
        try {
            val cursor = contentResolver.query(
                ContactsContract.Contacts.CONTENT_URI,
                null,
                null,
                null,
                ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME + " ASC"
            )
            while (cursor!!.moveToNext()) {
                val contactId =
                    cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID))
                val displayName =
                    cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME))
                Log.d(TAG, "Contacts $contactId $displayName")

            }
        }
        catch (e: Exception){
            println("Error: $e")
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray){
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if(requestCode == READ_CONTACTS_PERMISSION_REQUEST_CODE) {
            if(grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                readContacts()
            }
            else
            {
                val text = "Permission needed"
                val duration = Toast.LENGTH_SHORT

                val toast = Toast.makeText(applicationContext, text, duration)
                toast.show()
            }
        }
    }
}