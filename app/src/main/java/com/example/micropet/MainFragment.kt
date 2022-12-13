package com.example.micropet

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView.OnItemLongClickListener
import android.widget.Toast
import androidx.lifecycle.asLiveData
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.micropet.databinding.FragmentMainBinding

class MainFragment : Fragment(), PetAdapter.Listener {
    lateinit var binding: FragmentMainBinding
    lateinit var adapter: PetAdapter
    private lateinit var controller : NavController
    lateinit var database: MainDatabase
    lateinit var petsArrayList: ArrayList<PetModel>
    private var editMode: Boolean = false
    var lastSelectPosition: Int? = null


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

    private fun editModeActivator(name: String){
        if (editMode){
            binding.cardViewEdit.visibility = View.VISIBLE
            val newName = getText(R.string.name).toString() + name
            binding.textEditName.text = newName
            if (lastSelectPosition != null){
                //здесь доделать
            }
        } else {
            binding.cardViewEdit.visibility = View.GONE
            lastSelectPosition = null
        }
    }

    companion object {
        @JvmStatic
        fun newInstance() = MainFragment()
    }

    override fun onClick(position: Int) {
        Toast.makeText(requireContext(), "$position", Toast.LENGTH_SHORT).show()
    }

    override fun onLongClick(position: Int) {
        Toast.makeText(requireContext(), "hello $position", Toast.LENGTH_SHORT).show()
        editMode = !editMode
        lastSelectPosition = position
        editModeActivator(petsArrayList[position].name)
    }
}