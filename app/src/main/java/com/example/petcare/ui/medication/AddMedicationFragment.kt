package com.example.petcare.ui.medication

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.petcare.PetCareApplication
import com.example.petcare.R
import com.example.petcare.database.medication.Medication
import com.example.petcare.databinding.FragmentAddMedicationBinding
import com.example.petcare.viewmodels.MedicationViewModel
import com.example.petcare.viewmodels.MedicationViewModelFactory

class AddMedicationFragment : Fragment() {
    private var _binding: FragmentAddMedicationBinding? = null
    private val binding get() = _binding!!

    private val medicationViewModel: MedicationViewModel by activityViewModels {
        MedicationViewModelFactory((activity?.application as PetCareApplication).database.medicationDao())
    }

    lateinit var medication: Medication

    private val navigationArgs: AddMedicationFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentAddMedicationBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.saveButton.setOnClickListener { addNewMedication() }
    }

    private fun isEntryValid(): Boolean {
        return this.medicationViewModel.isEntryValid(
            binding.medicationName.text.toString(),
            binding.medicationDescription.text.toString(),
            binding.medicationDosage.text.toString(),
            binding.medicationStartDate.text.toString(),
            binding.medicationEndDate.text.toString(),
            binding.medicationRepetition.text.toString()
        )
    }

    private fun addNewMedication() {
        if (isEntryValid()) {
            this.medicationViewModel.addNewMedication(
                navigationArgs.petId,
                binding.medicationName.text.toString(),
                binding.medicationDescription.text.toString(),
                binding.medicationDosage.text.toString(),
                binding.medicationStartDate.text.toString(),
                binding.medicationEndDate.text.toString(),
                binding.medicationRepetition.text.toString()
            )
        }
        val action =
            AddMedicationFragmentDirections.actionAddMedicationFragmentToMedicationListFragment(
                navigationArgs.petId
            )
        findNavController().navigate(action)
    }

}