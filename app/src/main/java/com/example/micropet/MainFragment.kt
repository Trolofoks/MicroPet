package com.example.micropet

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.fragment.app.Fragment
import androidx.lifecycle.asLiveData
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.example.micropet.databinding.FragmentMainBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class MainFragment : Fragment(), PetAdapter.Listener {
    lateinit var binding: FragmentMainBinding
    lateinit var adapter: PetAdapter
    private lateinit var controller : NavController
    lateinit var database: MainDatabase
    lateinit var petsArrayList: ArrayList<PetModel>
    private var editMode: Boolean = false
    var lastSelectPet: PetModel? = null
    lateinit var bundle: Bundle
    private lateinit var permissionLauncher: ActivityResultLauncher<String>


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMainBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
        bind()
        getPetsFromDatabase()
    }




    private fun init(){
        controller = findNavController()
        adapter = PetAdapter(this)
        database = MainDatabase.getDatabase(requireContext())
        petsArrayList = arrayListOf()
        bundle = Bundle()

    }

    private fun checkPermission(){
        if (!Settings.canDrawOverlays(requireContext())) {
            val myIntent = Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION)
            startActivity(myIntent)
        }
    }



    private fun bind(){
        binding.cardViewButton.setOnClickListener {
            controller.navigate(R.id.selectPetFragment)
        }

        binding.recyclerView.adapter = adapter
        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.textView2.setOnClickListener {
        }
        binding.cardViewButton.setOnLongClickListener{
            Toast.makeText(requireContext(), "123", Toast.LENGTH_SHORT).show()
            true
        }
        binding.buttonDelete.setOnClickListener{
            if (lastSelectPet != null){
                //этот тред происходит позже чем то что ниже поэтому сюда клонируем элемент
                val pet = lastSelectPet
                Thread{
                    database.getDao().deleteItem(pet!!)
                }.start()
                editModeActivator(false, null)
            }
        }
        binding.buttonCancel.setOnClickListener{
            editModeActivator(false,null)
        }
        binding.buttonUsePet.setOnClickListener{
            checkPermission()
            summonPet()
        }
    }

    private fun summonPet(){
        val service = Intent(requireContext(), OverlayPet::class.java)
        requireActivity().startService(service)
    }

    private fun getPetsFromDatabase(){
        database.getDao().getAllPets().asLiveData().observe(viewLifecycleOwner){ list ->
            adapter.addPets(list as ArrayList<PetModel>)
            petsArrayList = list
            if (list.isNotEmpty()) {
                petListNotNull()
            }
        }
    }

    private fun petListNotNull(){
        binding.cardViewButton.visibility = View.GONE
    }

    private fun editModeActivator(activate: Boolean, pet: PetModel?){
        editMode = activate
        lastSelectPet = pet
        if (editMode){
            binding.cardViewEdit.visibility = View.VISIBLE
            val newName = getText(R.string.name).toString() + pet!!.name
            binding.textEditName.text = newName

        } else {
            binding.cardViewEdit.visibility = View.GONE
            lastSelectPet = null
        }
    }

    companion object {
        @JvmStatic
        fun newInstance() = MainFragment()
    }

    override fun onClick(position: Int) {
        Toast.makeText(requireContext(), "$position", Toast.LENGTH_SHORT).show()
        val pet = petsArrayList[position]
        bundle.putSerializable("Key", pet)
        if (editMode){
            editModeActivator(true, petsArrayList[position])
        } else {
            controller.navigate(R.id.petRoomFragment)
        }

    }

    override fun onLongClick(position: Int) {
        Toast.makeText(requireContext(), "hello $position", Toast.LENGTH_SHORT).show()
        editModeActivator(true, petsArrayList[position])
    }
}