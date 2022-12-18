package com.example.micropet.service

import android.annotation.SuppressLint
import android.app.Service
import android.content.Context
import android.content.Intent
import android.graphics.PixelFormat
import android.graphics.drawable.AnimationDrawable
import android.os.*
import android.util.Log
import android.view.*
import android.view.WindowManager.LayoutParams
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.Toast
import com.example.micropet.Constance
import com.example.micropet.R
import com.example.micropet.data.TodoPetModel
import java.util.*
import kotlin.random.Random

class OverlayPet : Service() {

    private var initialTouchY = 0.0f
    private var initialTouchX = 0.0f
    private var initialY = 0
    private var initialX = 0
    var isMoving = false

    var isFalling = false

    private lateinit var windowManager: WindowManager
    private lateinit var overlayButton: ImageButton
    private lateinit var layoutParams: LayoutParams
    private lateinit var view: View

    private var arrayListTodo = arrayListOf<TodoPetModel>()
    private var globalRandomDo = 0

    @SuppressLint("InflateParams", "ClickableViewAccessibility")
    override fun onCreate() {
        super.onCreate()
        randomTodoGenerator()
        init()
        bind()
        setOnTouchListener()
    }

    private fun randomTodoGenerator(){
        for (i in 0..1000){
            val randomDo = Random.nextInt(0, 300)
            var randomTime = Random.nextInt(3, 11)
            randomTime *= 200
            arrayListTodo.add(TodoPetModel(randomDo, randomTime))
            arrayListTodo.add(TodoPetModel(0, 2000))

        }
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
                    animSet(Constance.DRAG)
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
                    animSet(Constance.FALL)
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
        isFalling = true
        //на создание этого ушла вся моя жизнь, боже это просто ужасно но оно работает
        class Timer() : CountDownTimer(30000, 20) {
            override fun onTick(millisUntilFinished: Long) {
                layoutParams.y = layoutParams.y + 20
                windowManager.updateViewLayout(view, layoutParams)
                if (layoutParams.y > resources.displayMetrics.heightPixels / 2 - view.height / 2){
                    cancel()
                    isFalling = false
                    animSet(Constance.END_FALL)

                    class MiniTimer : CountDownTimer(2000, 50) {
                        override fun onTick(p0: Long) {
                            if (isMoving || isFalling){
                                cancel()
                            }
                        }

                        override fun onFinish() {
                            setAI()
                        }
                    }
                    MiniTimer().start()
                }

                if (isMoving){
                    cancel()
                    isFalling = false
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

    private fun setAI(){
        Log.d("MyLog", "${arrayListTodo[globalRandomDo]}")
        if (!isMoving && !isFalling){
            petDo(arrayListTodo[globalRandomDo].whatPetDo,arrayListTodo[globalRandomDo].TimeTodo)
            globalRandomDo += 1
            if (globalRandomDo >= arrayListTodo.size) globalRandomDo = 0
        }
    }

    private fun petDo(whatDo : Int, TimeDo : Int){
        val whatDoString: String = when(whatDo){
            in 0.. 99->  Constance.STAY
            in 100..199-> Constance.LEFT
            in 200..299-> Constance.RIGHT
            else -> Constance.STAY
        }

        animSet(whatDoString)

        class DoTimer(): CountDownTimer(TimeDo.toLong(), 20){
            override fun onTick(p0: Long) {
                when (whatDoString){
                    in Constance.STAY -> {
                    }
                    in Constance.LEFT -> {
                        if (layoutParams.x < -(resources.displayMetrics.widthPixels / 2 - view.width / 2)) {
                            cancel()
                            animSet(Constance.STAY)
                            setAI()

                        }
                        if (isMoving || isFalling){
                            cancel()
                        }
                        walk(-5)
                    }
                    in Constance.RIGHT -> {
                        if (layoutParams.x > resources.displayMetrics.widthPixels / 2 - view.width / 2) {
                            cancel()
                            animSet(Constance.STAY)
                            setAI()

                        }
                        if (isMoving || isFalling){
                            cancel()
                        }
                        walk(5)
                    }
                }
            }

            override fun onFinish() {
                if (!isMoving && !isFalling){
                    animSet(Constance.STAY)
                    setAI()
                }
            }
        }
        val timer = DoTimer()
        timer.start()

    }

    private fun animSet(whatAnimSet: String){
        val image = view.findViewById<ImageView>(R.id.imagePets)
        var notAnim = false
        when(whatAnimSet){
            Constance.STAY -> {
                image.setBackgroundResource(R.drawable.stick)
                notAnim = true
            }
            Constance.LEFT -> {
                image.setBackgroundResource(R.drawable.stick_walk_left)
            }
            Constance.RIGHT -> {
                image.setBackgroundResource(R.drawable.stick_walk_right)
            }
            Constance.DRAG ->{
                image.setBackgroundResource(R.drawable.stick_drag)
            }
            Constance.FALL ->{
                image.setBackgroundResource(R.drawable.stick_fall)
                notAnim = true
            }
            Constance.END_FALL->{
                image.setBackgroundResource(R.drawable.stick_end_fall)
            }
        }
        if (!notAnim){
            val animation = image.background as AnimationDrawable
            animation.start()
        }
    }

    private fun walk(side: Int){

        layoutParams.x = layoutParams.x + side
        windowManager.updateViewLayout(view, layoutParams)
    }



    override fun onDestroy() {
        super.onDestroy()
        windowManager.removeView(overlayButton)
    }

    override fun onBind(p0: Intent?): IBinder? {
        return null
    }

}

