package com.example.micropet

import android.animation.ObjectAnimator
import android.content.res.Resources
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.micropet.databinding.FragmentPetRoomBinding


class PetRoomFragment : Fragment() {
    lateinit var binding: FragmentPetRoomBinding

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
    }

    private fun init(){

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
            TODO("Запиши это все в заметки и придумай как реализовать")
            binding.imageView.animate()
                .x(screenWidth - imageView.width.toFloat())
                .setDuration(1000)
                .start()
        }
    }

    companion object {

        @JvmStatic
        fun newInstance() = PetRoomFragment()

    }
}