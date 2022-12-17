package com.example.micropet

import android.annotation.SuppressLint
import android.app.Service
import android.content.Context
import android.content.Intent
import android.graphics.PixelFormat
import android.os.*
import android.view.*
import android.view.WindowManager.LayoutParams
import android.widget.ImageButton
import android.widget.Toast

class OverlayPet : Service() {

    private var initialTouchY = 0.0f
    private var initialTouchX = 0.0f
    private var initialY = 0
    private var initialX = 0
    var isMoving = false

    private lateinit var windowManager: WindowManager
    private lateinit var overlayButton: ImageButton
    private lateinit var layoutParams: LayoutParams
    private lateinit var view: View

    @SuppressLint("InflateParams", "ClickableViewAccessibility")
    override fun onCreate() {
        super.onCreate()
        init()
        bind()
        setOnTouchListener()

    }

    private fun init(){
        //флаг чтобы обьяснить андройду что это такое
        val layoutFlag = if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY
        } else {
            WindowManager.LayoutParams.TYPE_PHONE
        }

        //параметры размеров, флага, и формата измерения X и Y
        layoutParams = LayoutParams(
            LayoutParams.WRAP_CONTENT,
            LayoutParams.WRAP_CONTENT,
            layoutFlag,
            LayoutParams.FLAG_NOT_FOCUSABLE,
            PixelFormat.TRANSLUCENT
        )

        // inflater для наполнения контентом
        val inflater = getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        view = inflater.inflate(R.layout.overlay_view, null)

        // обьединяем все и отображаем элемент
        windowManager = getSystemService(Context.WINDOW_SERVICE) as WindowManager
        windowManager.addView(view, layoutParams)
    }

    private fun bind(){
        //это КАЛИЩЕ не работает
        view.setOnClickListener {
            if (!isMoving) {
                Toast.makeText(this, "it's work", Toast.LENGTH_SHORT).show()
            }
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun setOnTouchListener(){
        //on Touch для перемещения
        view.setOnTouchListener { v, event ->

            when (event.action) {
                //начало касания
                MotionEvent.ACTION_DOWN -> {
                    //если двигаем получаем данные о том где палец и где image
                    initialX = layoutParams.x
                    initialY = layoutParams.y
                    initialTouchX = event.rawX
                    initialTouchY = event.rawY

                    //даем понять что мы двигаем чтобы on click не работал
                    isMoving = true
                }
                //перемещение во время касания
                MotionEvent.ACTION_MOVE -> {

                    // Update the x and y coordinates of the layout params
                    layoutParams.x = initialX + (event.rawX - initialTouchX).toInt()
                    layoutParams.y = initialY + (event.rawY - initialTouchY).toInt()

                    // Update the view layout with the new coordinates
                    windowManager.updateViewLayout(view, layoutParams)
                }
                //прекращение касания
                MotionEvent.ACTION_UP -> {
                    fallingDown()
                    //даем понять что мы не двигаем чтобы работал on click
                    isMoving = false
                }

            }
            true
        }
    }

    //делает падение(вот это да)
    private fun fallingDown(){
        //на создание этого ушла вся моя жизнь, боже это просто ужасно но оно работает
        class Timer() : CountDownTimer(30000, 20) {
            override fun onTick(millisUntilFinished: Long) {
                layoutParams.y = layoutParams.y + 20
                windowManager.updateViewLayout(view, layoutParams)
                if (layoutParams.y > resources.displayMetrics.heightPixels / 2 - view.height / 2){
                    cancel()
                }
            }
            override fun onFinish() {
            }
        }
        val timer = Timer()
        timer.start()

        //layoutParams.y = resources.displayMetrics.heightPixels / 2 - view.height / 2
        //windowManager.updateViewLayout(view, layoutParams)
    }

    override fun onDestroy() {
        super.onDestroy()
        windowManager.removeView(overlayButton)
    }

    override fun onBind(p0: Intent?): IBinder? {
        return null
    }

}

