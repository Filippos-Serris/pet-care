package com.example.petcare.ui.pet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.example.petcare.PetCareApplication
import com.example.petcare.R
import com.example.petcare.database.pet.Pet
import com.example.petcare.databinding.FragmentPetDetailBinding
import com.example.petcare.viewmodels.PetViewModel
import com.example.petcare.viewmodels.PetViewModelFactory
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class PetDetailFragment : Fragment() {
    private var _binding: FragmentPetDetailBinding? = null
    private val binding get() = _binding!!

    private val petViewModel: PetViewModel by activityViewModels {
        PetViewModelFactory((activity?.application as PetCareApplication).database.petDao())
    }

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

        this.petViewModel.retrievePet(id).observe(this.viewLifecycleOwner) { selectedPet ->
            pet = selectedPet
            bind(pet)
        }
    }

    private fun bind(pet: Pet) {
        binding.apply {
            petName.text = pet.petName
            petSpecies.text = pet.petSpecies
            petBreed.text = pet.petBreed
            petSex.text = pet.petSex
            petDateOfBirth.text = petViewModel.calculatePetAge(pet.petDateOfBirth)
            petColour.text = pet.petColour
            petChip.text = pet.petChip
            deleteButton.setOnClickListener { confirmDialog() }
            editButton.setOnClickListener { editPet() }
            petVaccine.setOnClickListener {
                val action =
                    PetDetailFragmentDirections.actionPetDetailFragmentToVaccineListFragment(
                        navigationArgs.petId
                    )
                findNavController().navigate(action)
            }
            petMedication.setOnClickListener {
                val action =
                    PetDetailFragmentDirections.actionPetDetailFragmentToMedicationListFragment(
                        navigationArgs.petId
                    )
                findNavController().navigate(action)
            }
            petExams.setOnClickListener {
                Toast.makeText(
                    context,
                    "Not yet implemented",
                    Toast.LENGTH_LONG
                ).show()
            }
            petBath.setOnClickListener {
                val action = PetDetailFragmentDirections.actionPetDetailFragmentToBathListFragment(
                    navigationArgs.petId
                )
                findNavController().navigate(action)
            }
            petGrooming.setOnClickListener {
                val action =
                    PetDetailFragmentDirections.actionPetDetailFragmentToGroomingListFragment(
                        navigationArgs.petId
                    )
                findNavController().navigate(action)
            }
        }
    }

    private fun deletePet() {
        this.petViewModel.deletePet(pet)
        findNavController().navigateUp()
    }

    private fun confirmDialog() {
        MaterialAlertDialogBuilder(requireContext())
            .setTitle(getString(android.R.string.dialog_alert_title))
            .setMessage(getString(R.string.delete_question) + " " + pet.petName)
            .setCancelable(false)
            .setNegativeButton(getString(R.string.no)) { _, _ -> }
            .setPositiveButton(getString(R.string.yes)) { _, _ ->
                deletePet()
            }
            .show()
    }

    private fun editPet() {
        val title = "edit" + pet.petName
        val action = PetDetailFragmentDirections.actionPetDetailFragmentToAddPetFragment(
            title,
            pet.petId
        )
        this.findNavController().navigate(action)
    }
}

