package com.example.petcare.ui.pet

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.petcare.PetCareApplication
import com.example.petcare.PetCareViewModel
import com.example.petcare.PetCareViewModelFactory
import com.example.petcare.R
import com.example.petcare.database.pet.Pet
import com.example.petcare.databinding.FragmentAddPetBinding


class AddPetFragment : Fragment() {
    private var _binding: FragmentAddPetBinding? = null
    private val binding get() = _binding!!

    private val viewModel: PetCareViewModel by activityViewModels {
        PetCareViewModelFactory(
            (activity?.application as PetCareApplication).database.petDao(),
            (activity?.application as PetCareApplication).database.vaccineDao()
        )
    }

    lateinit var pet: Pet

    private val navigationArgs: AddPetFragmentArgs by navArgs()
    // ------------------------------------------------------------------------------------

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentAddPetBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val id = navigationArgs.petId
        if (id > 0) {
            viewModel.retrievePet(id).observe(this.viewLifecycleOwner) { selectedPet ->
                pet = selectedPet
                bind(pet)
            }
        } else {
            binding.saveInfoButton.setOnClickListener { addNewPet() }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    private fun isEntryValid(): Boolean {
        return viewModel.isEntryValid(
            binding.petName.text.toString(),
            binding.petSpecies.text.toString(),
            binding.petBreed.text.toString(),
            binding.petSex.text.toString(),
            binding.petDateOfBirth.text.toString(),
            binding.petColour.text.toString(),
            binding.petChip.text.toString()
        )
    }

    private fun addNewPet() {
        if (isEntryValid()) {
            viewModel.addNewPet(
                binding.petName.text.toString(),
                binding.petSpecies.text.toString(),
                binding.petBreed.text.toString(),
                binding.petSex.text.toString(),
                binding.petDateOfBirth.text.toString(),
                binding.petColour.text.toString(),
                binding.petChip.text.toString()
            )
            findNavController().navigate(R.id.action_addPetFragment_to_petListFragment)
        }
    }

    private fun bind(pet: Pet) {
        binding.apply {
            petName.setText(pet.petName, TextView.BufferType.SPANNABLE)
            petSpecies.setText(pet.petSpecies, TextView.BufferType.SPANNABLE)
            petBreed.setText(pet.petBreed, TextView.BufferType.SPANNABLE)
            petSex.setText(pet.petSex, TextView.BufferType.SPANNABLE)
            petDateOfBirth.setText(pet.petDateOfBirth, TextView.BufferType.SPANNABLE)
            petColour.setText(pet.petColour, TextView.BufferType.SPANNABLE)
            petChip.setText(pet.petChip, TextView.BufferType.SPANNABLE)
            saveInfoButton.setOnClickListener { updatePet() }
        }
    }

    private fun updatePet() {
        if (isEntryValid()) {
            viewModel.updatePet(
                this.navigationArgs.petId,
                this.binding.petName.text.toString(),
                this.binding.petSpecies.text.toString(),
                this.binding.petBreed.text.toString(),
                this.binding.petSex.text.toString(),
                this.binding.petDateOfBirth.text.toString(),
                this.binding.petColour.text.toString(),
                this.binding.petChip.text.toString()
            )
            val action = AddPetFragmentDirections.actionAddPetFragmentToPetListFragment()
            findNavController().navigate(action)
        }
    }
}