package com.example.micropet.fragment

import android.content.res.Resources
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import com.example.micropet.R
import com.example.micropet.databinding.FragmentPetRoomBinding


class PetRoomFragment : Fragment() {
    lateinit var binding: FragmentPetRoomBinding
    lateinit var imageView: View

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentPetRoomBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
        bind()
        stickMind()
    }

    private fun init(){
        imageView = binding.imageView
    }

    private fun bind(){
        binding.buttonSpawn.setOnClickListener {
            binding.imageView.setBackgroundResource(R.drawable.stick)
        }
        binding.buttonLeft.setOnClickListener {
            val imageView = binding.imageView
            val currentX = binding.imageView.x
            val currentY = binding.imageView.y
            // Get the width of the screen
            val screenWidth = Resources.getSystem().displayMetrics.widthPixels

            binding.imageView.animate()
                .x(screenWidth - imageView.width.toFloat())
                .setDuration(1000)
                .start()
        }
        binding.button3.setOnClickListener {
            gravity()
        }

    }
    private fun stickMind(){
        movable(binding.imageView)
    }

    private fun movable(view: View){
        view.setOnTouchListener(object : View.OnTouchListener {
            var dX = 0f
            var dY = 0f

            override fun onTouch(view: View, event: MotionEvent): Boolean {
                when (event.action) {
                    MotionEvent.ACTION_DOWN -> {
                        dX = view.x - event.rawX
                        dY = view.y - event.rawY
                    }
                    MotionEvent.ACTION_MOVE -> {
                        view.animate()
                            .x(event.rawX + dX)
                            .y(event.rawY + dY)
                            .setDuration(0)
                            .start()
                    }
                    MotionEvent.ACTION_UP -> {
                        view.x = event.rawX + dX
                        view.y = event.rawY + dY
                    }
                    else -> return false
                }
                return true
            }
        })
    }
    private fun autoGravity(){
        gravity()
    }
    private fun gravity() {
    }



    companion object {

        @JvmStatic
        fun newInstance() = PetRoomFragment()

    }
}