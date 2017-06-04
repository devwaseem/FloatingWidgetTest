package com.waseem.floatingwidgettest

import android.app.Activity
import android.app.ActivityManager
import android.app.Service
import android.content.Context
import android.content.Intent
import android.graphics.PixelFormat
import android.opengl.Visibility
import android.os.Bundle
import android.os.IBinder
import android.util.DisplayMetrics
import android.view.*
import android.widget.Button
import android.widget.RelativeLayout
import com.pawegio.kandroid.v
import com.pawegio.kandroid.windowManager
import com.waseem.floatingwidgettest.R.id.*

import kotlinx.android.synthetic.main.layout_floating_widget.*
import kotlinx.android.synthetic.main.layout_floating_widget.view.*

class floatingWidgetService : Service() {


    var isCollapsed:Boolean=false



    override fun onBind(intent: Intent): IBinder? {
        // TODO: Return the communication channel to the service.
        throw UnsupportedOperationException("Not yet implemented")
    }

    override fun onCreate() {
        super.onCreate()



    }



    companion object{
        var serviceRunning=false
    }
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        serviceRunning=true


        var flw = LayoutInflater.from(this).inflate(R.layout.layout_floating_widget,null)
        var WManager:WindowManager = windowManager as WindowManager
        val params:WindowManager.LayoutParams = WindowManager.LayoutParams( WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.TYPE_PHONE,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                PixelFormat.TRANSLUCENT)
        params.gravity = Gravity.TOP + Gravity.LEFT
        params.x=0
        params.y=150
        flw.expanded.visibility = View.GONE

        WManager.addView(flw,params)


         flw.exitall.setOnClickListener {
             WManager.removeView(flw)
             stopSelf()
         }


        var initialX:Int=0
        var initialY:Int=0
        var initialTouchX:Float=0.0.toFloat()
        var initialTouchY:Float=0.0.toFloat()

        var moveableView:View = flw.floatingButton


        flw.boxClose.setOnClickListener {
            flw.floatingButton.visibility = View.VISIBLE
            flw.expanded.visibility = View.GONE
        }

       flw.floatingButton.setOnTouchListener(View.OnTouchListener { view, motionEvent ->
           v("touch","touched")

            when(motionEvent.action){

                MotionEvent.ACTION_DOWN->{
                   v("ACTION DOWN")
                    initialX = params.x
                    initialY = params.y
                    initialTouchX = motionEvent.rawX
                    initialTouchY = motionEvent.rawY
                    true
                }

                MotionEvent.ACTION_UP->{
                   v("ACTION UP")
                    var xdiff = (motionEvent.rawX - initialTouchX).toInt()
                    var ydiff = (motionEvent.rawY - initialTouchY).toInt()
                    if (xdiff<30 && ydiff<30){


                        if ( flw.floatingButton.visibility == View.VISIBLE ) {
                            moveableView = flw.expanded
                            flw.floatingButton.visibility = View.GONE
                            flw.expanded.visibility = View.VISIBLE


                        }
                    }


                    //move to left or right
                    var metrics = DisplayMetrics()

                    WManager.defaultDisplay.getMetrics(metrics)
                    v("width",""+metrics.widthPixels/2)
                    if(params.x >= metrics.widthPixels/2){
                        params.x=metrics.widthPixels- flw.floatingButton.width

                    }else{

                       params.x=0
                    }
                    WManager.updateViewLayout(flw,params)
                    true
                }
                MotionEvent.ACTION_MOVE->{
                    v("ACTION MOVE", "x: " + motionEvent.rawX +"  y: "+motionEvent.rawY +
                            " params.x: " + params.x + "params.y: " + params.y )
                    v("params width"+ flw.floatingButton.width + "params height" + flw.floatingButton.width)
                    params.x = (motionEvent.rawX - flw.floatingButton.width/2).toInt()
                    params.y = (motionEvent.rawY - flw.floatingButton.height/2).toInt()
                    WManager.updateViewLayout(flw,params)
                    true
                }
            }
            false

        })

        return super.onStartCommand(intent, flags, startId)

    }

    fun isInCollapsedMode(flw:View){

    }



    override fun onDestroy() {

        super.onDestroy()
    }
}
