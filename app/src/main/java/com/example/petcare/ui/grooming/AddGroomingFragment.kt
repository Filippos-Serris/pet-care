package com.example.petcare.ui.grooming

import android.app.Activity
import android.app.DatePickerDialog
import android.icu.util.Calendar
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.petcare.PetCareApplication
import com.example.petcare.R
import com.example.petcare.database.grooming.Grooming
import com.example.petcare.databinding.FragmentAddGroomingBinding
import com.example.petcare.viewmodels.GroomingViewModel
import com.example.petcare.viewmodels.GroomingViewModelFactory
import com.google.android.material.textfield.TextInputEditText
import java.text.SimpleDateFormat
import java.util.*

class AddGroomingFragment : Fragment() {
    private var _binding: FragmentAddGroomingBinding? = null
    private val binding get() = _binding!!

    private val groomingViewModel: GroomingViewModel by activityViewModels {
        GroomingViewModelFactory((activity?.application as PetCareApplication).database.groomingDao())
    }

    lateinit var grooming: Grooming

    private val navigationArg: AddGroomingFragmentArgs by navArgs()

    private var cal = Calendar.getInstance()

    private val dateSetListener =
        DatePickerDialog.OnDateSetListener { _, year, monthOfYear, dayOfMonth ->
            cal.set(Calendar.YEAR, year)
            cal.set(Calendar.MONTH, monthOfYear)
            cal.set(Calendar.DAY_OF_MONTH, dayOfMonth)
            updateDateInView(binding.groomingDate)
        }

    private val nextDateSetListener =
        DatePickerDialog.OnDateSetListener { _, year, monthOfYear, dayOfMonth ->
            cal.set(Calendar.YEAR, year)
            cal.set(Calendar.MONTH, monthOfYear)
            cal.set(Calendar.DAY_OF_MONTH, dayOfMonth)
            updateDateInView(binding.nextGroomingDate)
        }

    //----------------------------------------------------------------------------------------------------------

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentAddGroomingBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (navigationArg.groomingId > 0) {
            this.groomingViewModel.retrieveGrooming(navigationArg.groomingId)
                .observe(this.viewLifecycleOwner) { selectedGrooming ->
                    grooming = selectedGrooming
                    bind(grooming)
                }
        } else {
            binding.saveInfoButton.setOnClickListener { addNewGrooming() }
        }

        binding.groomingDate.setOnClickListener { pickDate(binding.groomingDate) }
        binding.nextGroomingDate.setOnClickListener { pickDate(binding.nextGroomingDate) }
    }

    private fun isEntryValid(): Boolean {
        return this.groomingViewModel.isEntryValid(
            binding.groomingDate.text.toString()
        )
    }


    private fun addNewGrooming() {
        if (isEntryValid()) {
            this.groomingViewModel.addNewGrooming(
                navigationArg.petId,
                binding.groomingDate.text.toString(), binding.nextGroomingDate.text.toString()
            )
            val action =
                AddGroomingFragmentDirections.actionAddGroomingFragmentToGroomingListFragment(
                    navigationArg.petId
                )
            findNavController().navigate(action)
        } else {
            Toast.makeText(context, getString(R.string.grooming_fields), Toast.LENGTH_SHORT).show()
        }

    }

    private fun bind(grooming: Grooming) {
        binding.apply {
            groomingDate.setText(grooming.groomingDate, TextView.BufferType.SPANNABLE)
            nextGroomingDate.setText(grooming.nextGroomingDate, TextView.BufferType.SPANNABLE)
            saveInfoButton.setOnClickListener { updateGrooming() }
        }
    }

    private fun updateGrooming() {
        if (isEntryValid()) {
            this.groomingViewModel.updateGrooming(
                this.navigationArg.groomingId,
                this.navigationArg.petId,
                this.binding.groomingDate.text.toString(),
                this.binding.nextGroomingDate.text.toString()
            )
            val action =
                AddGroomingFragmentDirections.actionAddGroomingFragmentToGroomingListFragment(
                    navigationArg.petId
                )
            findNavController().navigate(action)
        } else {
            Toast.makeText(context, getString(R.string.grooming_fields), Toast.LENGTH_SHORT).show()
        }

    }

    private fun pickDate(date: TextInputEditText) {
        if (date == binding.groomingDate) {
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
}