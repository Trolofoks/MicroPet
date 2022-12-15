package com.example.micropet

import android.annotation.SuppressLint
import android.app.Service
import android.content.Context
import android.content.Intent
import android.graphics.PixelFormat
import android.os.Build
import android.os.IBinder
import android.view.Gravity
import android.view.MotionEvent
import android.view.View
import android.view.WindowAnimationFrameStats
import android.view.WindowManager
import android.view.WindowManager.LayoutParams
import android.widget.ImageButton
import android.widget.ImageSwitcher
import android.widget.Toast
import androidx.core.content.getSystemService

class OverlayPet : Service(), View.OnTouchListener, View.OnClickListener {


    private lateinit var windowManager: WindowManager
    private lateinit var overlayButton: ImageButton
    private lateinit var params: LayoutParams

    override fun onCreate() {
        super.onCreate()
        Toast.makeText(this, "Hello", Toast.LENGTH_SHORT).show()

        windowManager = getSystemService(Context.WINDOW_SERVICE) as WindowManager
        overlayButton = ImageButton(this)
        overlayButton.setImageResource(R.mipmap.ic_launcher)
        overlayButton.setOnTouchListener(this)
        overlayButton.setOnClickListener(this)
        val layoutFlag = if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY
        } else {
            WindowManager.LayoutParams.TYPE_PHONE
        }

        params = WindowManager.LayoutParams(
            WindowManager.LayoutParams.WRAP_CONTENT,
            WindowManager.LayoutParams.WRAP_CONTENT,
            layoutFlag,
            WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
            PixelFormat.TRANSLUCENT
        )

        params.gravity = Gravity.TOP or Gravity.START

        params.x = 0
        params.y = 100

        windowManager.addView(overlayButton, params)

    }

    override fun onDestroy() {
        super.onDestroy()
        windowManager.removeView(overlayButton)
    }

    override fun onBind(p0: Intent?): IBinder? {
        return null
    }

    private var moving = false
    private var initialTouchY = 0.0f
    private var initialTouchX = 0.0f
    private var initialY = 0
    private var initialX = 0

    override fun onTouch(view: View?, event: MotionEvent?): Boolean {
        view!!.performClick()

        when(event!!.action){
            MotionEvent.ACTION_DOWN -> {
                initialX = params.x
                initialY = params.y
                initialTouchX = event.rawX
                initialTouchY = event.rawY
                moving = true
            }
            MotionEvent.ACTION_UP ->{
                moving = false
            }
            MotionEvent.ACTION_MOVE ->{
                params.x = initialX + (event.rawX - initialTouchX).toInt()
                params.y = initialY + (event.rawY - initialTouchY).toInt()
                windowManager.updateViewLayout(overlayButton, params)
            }
        }
        return true
    }

    override fun onClick(p0: View?) {
        if (!moving) Toast.makeText(this, "Ass Touched", Toast.LENGTH_SHORT).show()
    }
}