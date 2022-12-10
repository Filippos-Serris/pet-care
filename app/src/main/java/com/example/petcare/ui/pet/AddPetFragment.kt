package com.example.petcare.ui.pet

import android.app.DatePickerDialog
import android.icu.util.Calendar
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.DatePicker
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.petcare.PetCareApplication
import com.example.petcare.R
import com.example.petcare.database.pet.Pet
import com.example.petcare.databinding.FragmentAddPetBinding
import com.example.petcare.viewmodels.PetViewModel
import com.example.petcare.viewmodels.PetViewModelFactory


class AddPetFragment : Fragment() {
    private var _binding: FragmentAddPetBinding? = null
    private val binding get() = _binding!!

    /*private val petViewModel: PetCareViewModel by activityViewModels {
        PetCareViewModelFactory(
            (activity?.application as PetCareApplication).database.petDao(),
            (activity?.application as PetCareApplication).database.vaccineDao()
        )
    }*/

    private val petViewModel: PetViewModel by activityViewModels {
        PetViewModelFactory((activity?.application as PetCareApplication).database.petDao())
    }

    lateinit var pet: Pet

    private val navigationArgs: AddPetFragmentArgs by navArgs()

    var cal = Calendar.getInstance()

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
            this.petViewModel.retrievePet(id).observe(this.viewLifecycleOwner) { selectedPet ->
                pet = selectedPet
                bind(pet)
            }
        } else {
            binding.saveInfoButton.setOnClickListener { addNewPet() }
        }

        /*setting date
        binding.petDateOfBirth.setOnClickListener {}*/
    }


    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    private fun isEntryValid(): Boolean {
        return this.petViewModel.isEntryValid(
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
            this.petViewModel.addNewPet(
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
            this.petViewModel.updatePet(
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