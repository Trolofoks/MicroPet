package com.example.micropet

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.micropet.databinding.PetItemBinding

class PetAdapter(private val listener: Listener) : ListAdapter<PetModel, PetAdapter.PetHolder>(Comparator()) {
    private val petList = ArrayList<PetModel>()
    class PetHolder(item: View, private val listener: Listener):RecyclerView.ViewHolder(item){
        private val binding = PetItemBinding.bind(item)

        fun bind(pet: PetModel){
            binding.imagePetList.setImageResource(pet.imageId)
            binding.textPetName.text = if (pet.name.length > 50){
                pet.name.substring(0, 49)
            } else {
                pet.name
            }
            itemView.setOnClickListener{
                listener.onClick(adapterPosition)
            }
            itemView.setOnLongClickListener{
                listener.onLongClick(adapterPosition)
                true
            }
        }
    }

    class Comparator: DiffUtil.ItemCallback<PetModel>(){
        override fun areItemsTheSame(oldItem: PetModel, newItem: PetModel): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: PetModel, newItem: PetModel): Boolean {
            return oldItem == newItem
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PetHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.pet_item, parent, false)
        return PetHolder(view, listener)
    }

    override fun onBindViewHolder(holder: PetHolder, position: Int) {
        holder.bind(petList[position])
    }

    override fun getItemCount(): Int {
        return petList.size
    }

    fun addPets(petArrayList: ArrayList<PetModel>){
        petList.clear()
        petList.addAll(petArrayList)
        notifyDataSetChanged()
    }

    interface Listener{
        fun onClick(position: Int)
        fun onLongClick(position: Int)
    }
}