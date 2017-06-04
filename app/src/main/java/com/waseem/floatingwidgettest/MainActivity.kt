package com.waseem.floatingwidgettest

import android.app.Activity
import android.app.Service
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.provider.Settings
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.view.View
import android.view.WindowManager
import android.widget.Toast

import kotlinx.android.synthetic.main.activity_main.*
import java.io.Serializable

class MainActivity : AppCompatActivity() {

    val PERMISSION_REQUEST:Int = 9876

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        var Win= getSystemService(Context.WINDOW_SERVICE) as WindowManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && !Settings.canDrawOverlays(this)) {
        var intent = Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION, Uri.parse("package:"+packageName))
            startActivityForResult(intent,PERMISSION_REQUEST)
        }else{
            initialize(Win)
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if(requestCode == PERMISSION_REQUEST ){
            if (resultCode == Activity.RESULT_OK){
                initialize(getSystemService(Context.WINDOW_SERVICE) as WindowManager)
            }else{
                toast("Please allow permission")
                finish()
            }
        }
        super.onActivityResult(requestCode, resultCode, data)
    }

    fun initialize(Win:WindowManager){
        notify_me.setOnClickListener {
            if(floatingWidgetService.serviceRunning){
                toast("Floating widget Already exists ")
            }else{
                val flwService = Intent(applicationContext,floatingWidgetService::class.java)
                startService(flwService)
            }


        }


    }

    fun Context.toast(message: CharSequence) =
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
}
