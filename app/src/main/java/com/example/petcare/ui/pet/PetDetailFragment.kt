package com.example.petcare.ui.pet

import android.os.Bundle
import android.security.identity.AccessControlProfileId
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.petcare.PetCareApplication
import com.example.petcare.PetCareViewModel
import com.example.petcare.PetCareViewModelFactory
import com.example.petcare.R
import com.example.petcare.database.pet.Pet
import com.example.petcare.databinding.FragmentPetDetailBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class PetDetailFragment : Fragment() {
    private var _binding: FragmentPetDetailBinding? = null
    private val binding get() = _binding!!

    lateinit var pet: Pet

    private val navigationArgs: PetDetailFragmentArgs by navArgs()
    // ------------------------------------------------------------------------------------------

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentPetDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val id = navigationArgs.petId

        viewModel.retrievePet(id).observe(this.viewLifecycleOwner) { selectedPet ->
            pet = selectedPet
            bind(pet)
        }
    }

    private val viewModel: PetCareViewModel by activityViewModels {
        PetCareViewModelFactory(
            (activity?.application as PetCareApplication).database.petDao(),
            (activity?.application as PetCareApplication).database.vaccineDao()
        )
    }

    private fun bind(pet: Pet) {
        binding.apply {
            petName.text = pet.petName
            petSpecies.text = pet.petSpecies
            petBreed.text = pet.petBreed
            petSex.text = pet.petSex
            petDateOfBirth.text = pet.petDateOfBirth
            petColour.text = pet.petColour
            petChip.text = pet.petChip
            deleteButton.setOnClickListener { confirmDialog() }
            editButton.setOnClickListener { editPet() }
            petVaccine.setOnClickListener {
                val action =
                    PetDetailFragmentDirections.actionPetDetailFragmentToVaccineListFragment(navigationArgs.petId)
                findNavController().navigate(action)
            }
        }
    }

    private fun deletePet() {
        viewModel.deletePet(pet)
        findNavController().navigateUp()
    }

    private fun confirmDialog() {
        MaterialAlertDialogBuilder(requireContext())
            .setTitle(getString(android.R.string.dialog_alert_title))
            .setMessage(getString(R.string.delete_question))
            .setCancelable(false)
            .setNegativeButton(getString(R.string.no)) { _, _ -> }
            .setPositiveButton(getString(R.string.yes)) { _, _ ->
                deletePet()
            }
            .show()
    }

    private fun editPet() {
        val action = PetDetailFragmentDirections.actionPetDetailFragmentToAddPetFragment(
            getString(R.string.edit_pet_title),
            pet.petId
        )
        this.findNavController().navigate(action)
    }
}

