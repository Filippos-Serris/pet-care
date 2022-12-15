package com.example.petcare.ui.medication

import android.app.Activity
import android.app.DatePickerDialog
import android.content.Context
import android.icu.util.Calendar
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.petcare.PetCareApplication
import com.example.petcare.R
import com.example.petcare.database.medication.Medication
import com.example.petcare.databinding.FragmentAddMedicationBinding
import com.example.petcare.viewmodels.MedicationViewModel
import com.example.petcare.viewmodels.MedicationViewModelFactory
import com.google.android.material.textfield.TextInputEditText
import java.text.SimpleDateFormat
import java.util.*

class AddMedicationFragment : Fragment() {
    private var _binding: FragmentAddMedicationBinding? = null
    private val binding get() = _binding!!

    private val medicationViewModel: MedicationViewModel by activityViewModels {
        MedicationViewModelFactory((activity?.application as PetCareApplication).database.medicationDao())
    }

    lateinit var medication: Medication

    private val navigationArgs: AddMedicationFragmentArgs by navArgs()

    private var cal = Calendar.getInstance()

    private val startDateSetListener =
        DatePickerDialog.OnDateSetListener { _, year, monthOfYear, dayOfMonth ->
            cal.set(Calendar.YEAR, year)
            cal.set(Calendar.MONTH, monthOfYear)
            cal.set(Calendar.DAY_OF_MONTH, dayOfMonth)
            updateDateInView(binding.medicationStartDate)
        }

    private val endDateSetListener =
        DatePickerDialog.OnDateSetListener { _, year, monthOfYear, dayOfMonth ->
            cal.set(Calendar.YEAR, year)
            cal.set(Calendar.MONTH, monthOfYear)
            cal.set(Calendar.DAY_OF_MONTH, dayOfMonth)
            updateDateInView(binding.medicationEndDate)
        }

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

        binding.medicationStartDate.setOnClickListener {
            context?.hideKeyboard(it)
            pickDate(binding.medicationStartDate)
        }
        binding.medicationEndDate.setOnClickListener {
            context?.hideKeyboard(it)
            pickDate(binding.medicationEndDate)
        }
    }

    private fun isEntryValid(): Boolean {
        return this.medicationViewModel.isEntryValid(
            binding.medicationName.text.toString(),
            binding.medicationDosage.text.toString(),
            binding.medicationStartDate.text.toString()
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
            val action =
                AddMedicationFragmentDirections.actionAddMedicationFragmentToMedicationListFragment(
                    navigationArgs.petId
                )
            findNavController().navigate(action)
        } else {
            Toast.makeText(context, getString(R.string.medication_fields), Toast.LENGTH_LONG)
                .show()
        }

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
            val action =
                AddMedicationFragmentDirections.actionAddMedicationFragmentToMedicationListFragment(
                    navigationArgs.petId
                )
            findNavController().navigate(action)
        } else {
            Toast.makeText(context, getString(R.string.medication_fields), Toast.LENGTH_LONG)
                .show()
        }

    }

    private fun pickDate(date: TextInputEditText) {
        if (date == binding.medicationStartDate) {
            DatePickerDialog(
                requireContext(), startDateSetListener, cal.get(Calendar.YEAR),
                cal.get(Calendar.MONTH),
                cal.get(Calendar.DAY_OF_MONTH)
            ).show()
        } else {
            DatePickerDialog(
                requireContext(), endDateSetListener, cal.get(Calendar.YEAR),
                cal.get(Calendar.MONTH),
                cal.get(Calendar.DAY_OF_MONTH)
            ).show()
        }
    }

    private fun updateDateInView(date: TextInputEditText) {
        val myFormat = "dd/MM/yyyy" // mention the format you need
        val sdf = SimpleDateFormat(myFormat, Locale.US)
        date.setText(sdf.format(cal.time))
    }

    private fun Context.hideKeyboard(view: View) {
        val inputMethodManager =
            getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
    }

}