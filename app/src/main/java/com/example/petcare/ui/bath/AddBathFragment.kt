package com.example.petcare.ui.bath

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
import com.example.petcare.database.bath.Bath
import com.example.petcare.databinding.FragmentAddBathBinding
import com.example.petcare.viewmodels.BathViewModel
import com.example.petcare.viewmodels.BathViewModelFactory
import com.google.android.material.textfield.TextInputEditText
import java.text.SimpleDateFormat
import java.util.*

class AddBathFragment : Fragment() {
    private var _binding: FragmentAddBathBinding? = null
    private val binding get() = _binding!!

    private val bathViewModel: BathViewModel by activityViewModels {
        BathViewModelFactory((activity?.application as PetCareApplication).database.bathDao())
    }

    lateinit var bath: Bath

    private val navigationArgs: AddBathFragmentArgs by navArgs()

    private var cal = Calendar.getInstance()

    private val dateSetListener =
        DatePickerDialog.OnDateSetListener { _, year, monthOfYear, dayOfMonth ->
            cal.set(Calendar.YEAR, year)
            cal.set(Calendar.MONTH, monthOfYear)
            cal.set(Calendar.DAY_OF_MONTH, dayOfMonth)
            updateDateInView(binding.bathDate)
        }

    private val nextDateSetListener =
        DatePickerDialog.OnDateSetListener { _, year, monthOfYear, dayOfMonth ->
            cal.set(Calendar.YEAR, year)
            cal.set(Calendar.MONTH, monthOfYear)
            cal.set(Calendar.DAY_OF_MONTH, dayOfMonth)
            updateDateInView(binding.nextBathDate)
        }

    //------------------------------------------------------------------------------------------------------

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentAddBathBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val bathId = navigationArgs.bathId

        if (bathId > 0) {
            this.bathViewModel.retrieveBath(bathId)
                .observe(this.viewLifecycleOwner) { selectedBath ->
                    bath = selectedBath
                    bind(bath)
                }
        } else {
            binding.saveButton.setOnClickListener { addNewBath() }
        }

        binding.bathDate.setOnClickListener {
            pickDate(binding.bathDate)
            context?.hideKeyboard(it)
        }
        binding.nextBathDate.setOnClickListener { pickDate(binding.nextBathDate) }
    }

    private fun isEntryValid(): Boolean {
        return this.bathViewModel.isEntryValid(
            binding.bathDate.text.toString()
        )
    }

    private fun addNewBath() {
        if (isEntryValid()) {
            this.bathViewModel.addNewBath(
                navigationArgs.petId,
                binding.bathDate.text.toString(),
                binding.nextBathDate.text.toString()
            )
            val action =
                AddBathFragmentDirections.actionAddBathFragmentToBathListFragment(navigationArgs.petId)
            findNavController().navigate(action)
        } else {
            Toast.makeText(context, getString(R.string.bath_fields), Toast.LENGTH_SHORT).show()
        }
    }

    private fun bind(bath: Bath) {
        binding.apply {
            bathDate.setText(bath.bathDate, TextView.BufferType.SPANNABLE)
            nextBathDate.setText(bath.nextBathDate, TextView.BufferType.SPANNABLE)
            saveButton.setOnClickListener { updateBath() }
        }
    }

    private fun updateBath() {
        if (isEntryValid()) {
            this.bathViewModel.updateBath(
                this.navigationArgs.bathId,
                this.navigationArgs.petId,
                this.binding.bathDate.text.toString(),
                this.binding.nextBathDate.text.toString()
            )
            val action =
                AddBathFragmentDirections.actionAddBathFragmentToBathListFragment(navigationArgs.petId)
            findNavController().navigate(action)
        } else {
            Toast.makeText(context, getString(R.string.bath_fields), Toast.LENGTH_SHORT).show()
        }

    }

    private fun pickDate(date: TextInputEditText) {
        if (date == binding.bathDate) {
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