package com.example.petcare.ui.medication

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

        val medicationId = navigationArgs.medicationId
        if (medicationId > 0) {
            this.medicationViewModel.retrieveMedication(medicationId)
                .observe(this.viewLifecycleOwner) { selectedMedication ->
                    medication = selectedMedication
                    bind(medication)
                }
        } else {
            binding.saveButton.setOnClickListener { addNewMedication() }
        }
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

    private fun bind(medication: Medication) {
        binding.apply {
            medicationName.setText(medication.medicationName, TextView.BufferType.SPANNABLE)
            medicationDescription.setText(
                medication.medicationDescription,
                TextView.BufferType.SPANNABLE
            )
            medicationDosage.setText(medication.medicationDosage, TextView.BufferType.SPANNABLE)
            medicationStartDate.setText(
                medication.medicationStartDate,
                TextView.BufferType.SPANNABLE
            )
            medicationEndDate.setText(medication.medicationEndDate, TextView.BufferType.SPANNABLE)
            medicationRepetition.setText(
                medication.medicationRepetition,
                TextView.BufferType.SPANNABLE
            )
            saveButton.setOnClickListener { updateMedication() }
        }
    }

    private fun updateMedication() {
        if (isEntryValid()) {
            this.medicationViewModel.updateMedication(
                this.navigationArgs.medicationId,
                this.navigationArgs.petId,
                this.binding.medicationName.text.toString(),
                this.binding.medicationDescription.text.toString(),
                this.binding.medicationDosage.text.toString(),
                this.binding.medicationStartDate.text.toString(),
                this.binding.medicationEndDate.text.toString(),
                this.binding.medicationRepetition.text.toString()
            )
        }
        val action =
            AddMedicationFragmentDirections.actionAddMedicationFragmentToMedicationListFragment(
                navigationArgs.petId
            )
        findNavController().navigate(action)
    }

}