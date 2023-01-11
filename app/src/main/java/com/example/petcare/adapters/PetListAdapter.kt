package com.example.petcare.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.petcare.R
import com.example.petcare.database.pet.Pet
import com.example.petcare.databinding.PetListPetBinding
import java.security.AccessControlContext
import java.security.AccessController.getContext

class PetListAdapter(/*private val context:Context, */private val onPetClicked: (Pet) -> Unit) :
    ListAdapter<Pet, PetListAdapter.PetViewHolder>(DiffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PetViewHolder {
        return PetViewHolder(
            PetListPetBinding.inflate(LayoutInflater.from(parent.context))
        )
    }

    override fun onBindViewHolder(holder: PetViewHolder, position: Int) {
        val current = getItem(position)
        holder.itemView.setOnClickListener { onPetClicked(current) }
        holder.bind(current)
    }

    class PetViewHolder(private var binding: PetListPetBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(pet: Pet) {
            binding.apply {
                Glide.with(petImg).load(pet.petImage).into(petImg)
                petName.text = pet.petName
                petSex.text = pet.petSex
                petSpecies.text = pet.petSpecies
                petChip.text = pet.petChip
            }
        }
    }

    companion object {
        private val DiffCallback = object : DiffUtil.ItemCallback<Pet>() {
            override fun areItemsTheSame(oldPet: Pet, newPet: Pet): Boolean {
                return oldPet === newPet
            }

            override fun areContentsTheSame(oldPet: Pet, newPet: Pet): Boolean {
                return oldPet.petName == newPet.petName
            }
        }
    }
}