package com.example.petcare.ui.pet

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.petcare.PetCareApplication
import com.example.petcare.R
import com.example.petcare.database.exams.Exams
import com.example.petcare.database.pet.Pet
import com.example.petcare.databinding.FragmentPetDetailBinding
import com.example.petcare.viewmodels.PetViewModel
import com.example.petcare.viewmodels.PetViewModelFactory
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import java.io.File

class PetDetailFragment : Fragment() {
    private var _binding: FragmentPetDetailBinding? = null
    private val binding get() = _binding!!

    private val petViewModel: PetViewModel by activityViewModels {
        PetViewModelFactory((activity?.application as PetCareApplication).database.petDao())
    }

    lateinit var pet: Pet
    lateinit var examsList: List<Exams>
    private var listOfExamsForDelete: MutableList<String> = mutableListOf()

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

        petViewModel.retrievePet(id).observe(this.viewLifecycleOwner) { selectedPet ->
            pet = selectedPet
            bind(pet)
        }

        petViewModel.retrievePetExamsForDelete(id)
            .observe(this.viewLifecycleOwner) { exams ->
                examsList = exams
                Log.d("", "$examsList")
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
            vaccineButton.setOnClickListener {
                val action =
                    PetDetailFragmentDirections.actionPetDetailFragmentToVaccineListFragment(
                        navigationArgs.petId
                    )
                findNavController().navigate(action)
            }
            medicationButton.setOnClickListener {
                val action =
                    PetDetailFragmentDirections.actionPetDetailFragmentToMedicationListFragment(
                        navigationArgs.petId
                    )
                findNavController().navigate(action)
            }
            examsButton.setOnClickListener {
                val action = PetDetailFragmentDirections.actionPetDetailFragmentToExamListFragment(
                    navigationArgs.petId
                )
                findNavController().navigate(action)
            }
            bathButton.setOnClickListener {
                val action = PetDetailFragmentDirections.actionPetDetailFragmentToBathListFragment(
                    navigationArgs.petId
                )
                findNavController().navigate(action)
            }
            groomingButton.setOnClickListener {
                val action =
                    PetDetailFragmentDirections.actionPetDetailFragmentToGroomingListFragment(
                        navigationArgs.petId
                    )
                findNavController().navigate(action)
            }
        }
    }

    private fun deletePet() {
        deleteImageFromInternalStorage()
        deleteExamResultsFromInternalStorage()

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
        val title = "Edit ${pet.petName}'s information"
        val action = PetDetailFragmentDirections.actionPetDetailFragmentToAddPetFragment(
            title,
            pet.petId
        )
        this.findNavController().navigate(action)
    }

    private fun deleteImageFromInternalStorage() {
        val file = File(pet.petImage)
        file.delete()
    }

    private fun deleteExamResultsFromInternalStorage() {
        for (exam in examsList) {
            if (exam.examinationResults != null) {
                for (result in exam.examinationResults) {
                    listOfExamsForDelete.add(result)
                }
            }
        }

        for(result in listOfExamsForDelete){
            val file = File(result)
            file.delete()
        }
    }
}

