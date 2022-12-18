package com.example.micropet.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.example.micropet.R
import com.example.micropet.data.MainDatabase
import com.example.micropet.data.PetModel
import com.example.micropet.databinding.FragmentSelectPetBinding

class SelectPetFragment : Fragment() {

    lateinit var binding: FragmentSelectPetBinding
    lateinit var controller: NavController
    lateinit var bundle: Bundle
    private lateinit var database: MainDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSelectPetBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
        bind()


    }
    private fun init(){
        controller = findNavController()
        bundle = Bundle()
        database = MainDatabase.getDatabase(requireContext())
    }

    private fun bind(){
        binding.buttonSave.setOnClickListener{
            saveThePet()
            controller.navigateUp()
        }
    }

    companion object {
        @JvmStatic
        fun newInstance() = SelectPetFragment()
    }

    private fun saveThePet(){
        val pet = PetModel(
            null,
            binding.editTextPetName.text.toString(),
            R.drawable.img,
            1,1,1)
        Thread{
            database.getDao().insertPet(pet)
        }.start()
    }
}

/**
binding.buttonSpawn.setOnClickListener {
binding.imageView5.setBackgroundResource(R.drawable.stick_walk_right)
val backAnim = binding.imageView5.background as AnimationDrawable
backAnim.start()
ObjectAnimator.ofFloat(binding.imageView5,"translationY", -500f).apply {
duration = 2000
start()
}
}
binding.button2.setOnClickListener {
binding.imageView5.setBackgroundResource(R.drawable.stick)
binding.imageView5.apply {
ObjectAnimator.ofFloat(binding.imageView5,"translationX", 500f).apply {
duration = 2000
start()
}
}


}
 */