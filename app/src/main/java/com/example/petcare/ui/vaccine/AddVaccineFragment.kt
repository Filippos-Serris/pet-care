package com.example.petcare.ui.vaccine

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
import com.example.petcare.database.vaccine.Vaccine
import com.example.petcare.databinding.FragmentAddVaccineBinding
import com.example.petcare.viewmodels.VaccineViewModel
import com.example.petcare.viewmodels.VaccineViewModelFactory
import com.google.android.material.textfield.TextInputEditText
import java.text.SimpleDateFormat
import java.util.*

class AddVaccineFragment : Fragment() {
    private var _binding: FragmentAddVaccineBinding? = null
    private val binding get() = _binding!!

    private val vaccineViewModel: VaccineViewModel by activityViewModels {
        VaccineViewModelFactory((activity?.application as PetCareApplication).database.vaccineDao())
    }

    private val navigationArgs: AddVaccineFragmentArgs by navArgs()

    lateinit var vaccine: Vaccine

    private var cal = Calendar.getInstance()

    private val dateSetListener =
        DatePickerDialog.OnDateSetListener { _, year, monthOfYear, dayOfMonth ->
            cal.set(Calendar.YEAR, year)
            cal.set(Calendar.MONTH, monthOfYear)
            cal.set(Calendar.DAY_OF_MONTH, dayOfMonth)
            updateDateInView(binding.vaccinationDate)
        }

    private val nextDateSetListener =
        DatePickerDialog.OnDateSetListener { _, year, monthOfYear, dayOfMonth ->
            cal.set(Calendar.YEAR, year)
            cal.set(Calendar.MONTH, monthOfYear)
            cal.set(Calendar.DAY_OF_MONTH, dayOfMonth)
            updateDateInView(binding.nextVaccinationDate)
        }
    //-----------------------------------------------------------------------------

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentAddVaccineBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val vaccineId = navigationArgs.vacccineId

        if (vaccineId > 0) {
            this.vaccineViewModel.retrieveVaccine(vaccineId)
                .observe(this.viewLifecycleOwner) { selectedVaccine ->
                    vaccine = selectedVaccine
                    bind(vaccine)
                }
        } else {
            binding.saveButton.setOnClickListener { addNewVaccine() }
        }

        binding.vaccinationDate.setOnClickListener {
            context?.hideKeyboard(it)
            pickDate(binding.vaccinationDate)
        }
        binding.nextVaccinationDate.setOnClickListener {
            context?.hideKeyboard(it)
            pickDate(binding.nextVaccinationDate)
        }
    }

    private fun isEntryValid(): Boolean {
        return this.vaccineViewModel.isEntryValid(
            binding.vaccineName.text.toString(),
            binding.vaccinationDate.text.toString(),
            binding.nextVaccinationDate.text.toString()
        )
    }

    private fun addNewVaccine() {
        if (isEntryValid()) {
            this.vaccineViewModel.addNewVaccine(
                navigationArgs.petId,
                binding.vaccineName.text.toString(),
                binding.vaccineDescription.text.toString(),
                binding.vaccinationDate.text.toString(),
                binding.nextVaccinationDate.text.toString()
            )
            val action =
                AddVaccineFragmentDirections.actionAddVaccineFragmentToVaccineListFragment(
                    navigationArgs.petId
                )
            findNavController().navigate(action)
        } else {
            Toast.makeText(context, getString(R.string.vaccine_fields), Toast.LENGTH_LONG).show()
        }

    }

    private fun bind(vaccine: Vaccine) {
        binding.apply {
            vaccineName.setText(vaccine.vaccineName, TextView.BufferType.SPANNABLE)
            vaccineDescription.setText(vaccine.vaccineDescription, TextView.BufferType.SPANNABLE)
            vaccinationDate.setText(vaccine.vaccinationDate, TextView.BufferType.SPANNABLE)
            nextVaccinationDate.setText(vaccine.nextVaccinationDate, TextView.BufferType.SPANNABLE)
            saveButton.setOnClickListener { updateVaccine() }

        }
    }

    private fun updateVaccine() {
        if (isEntryValid()) {
            this.vaccineViewModel.updateVaccine(
                this.navigationArgs.vacccineId,
                this.navigationArgs.petId,
                this.binding.vaccineName.text.toString(),
                this.binding.vaccineDescription.text.toString(),
                this.binding.vaccinationDate.text.toString(),
                this.binding.nextVaccinationDate.text.toString()
            )
            val action = AddVaccineFragmentDirections.actionAddVaccineFragmentToVaccineListFragment(
                navigationArgs.petId
            )
            findNavController().navigate(action)
        } else {
            Toast.makeText(context, getString(R.string.vaccine_fields), Toast.LENGTH_LONG).show()
        }

    }

    private fun pickDate(date: TextInputEditText) {
        if (date == binding.vaccinationDate) {
            DatePickerDialog(
                requireContext(), dateSetListener, cal.get(Calendar.YEAR),
                cal.get(Calendar.MONTH),
                cal.get(Calendar.DAY_OF_MONTH)
            ).show()
        } else {
            DatePickerDialog(
                requireContext(), nextDateSetListener, cal.get(Calendar.YEAR),
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