package com.example.petcare.ui.exams

import android.app.DatePickerDialog
import android.icu.util.Calendar
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.navArgs
import com.example.petcare.PetCareApplication
import com.example.petcare.databinding.FragmentAddExamBinding
import com.example.petcare.viewmodels.ExamsViewModel
import com.example.petcare.viewmodels.ExamsViewModelFactory
import com.google.android.material.textfield.TextInputEditText


class AddExamFragment : Fragment() {
    private var _binding: FragmentAddExamBinding? = null
    private val binding get() = _binding!!

    private val examsViewModel: ExamsViewModel by activityViewModels {
        ExamsViewModelFactory((activity?.application as PetCareApplication).database.examsDao())
    }

    private val navigationArgs: AddExamFragmentArgs by navArgs()

    private var cal = Calendar.getInstance()

    private val dateSetListener =
        DatePickerDialog.OnDateSetListener { _, year, monthOfYear, dayOfMonth ->
            cal.set(Calendar.YEAR, year)
            cal.set(Calendar.MONTH, monthOfYear)
            cal.set(Calendar.DAY_OF_MONTH, dayOfMonth)
            updateDateInView(binding.examinationDate)
        }

    private val nextDateSetListener =
        DatePickerDialog.OnDateSetListener { _, year, monthOfYear, dayOfMonth ->
            cal.set(Calendar.YEAR, year)
            cal.set(Calendar.MONTH, monthOfYear)
            cal.set(Calendar.DAY_OF_MONTH, dayOfMonth)
            updateDateInView(binding.nextExaminationDate)
        }
    //----------------------------------------------------------------------------

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentAddExamBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val examId = navigationArgs.examsId

        if (examId > 0) {
            examsViewModel.retrieveExam(examId)
        } else {
            binding.saveInfoButton.setOnClickListener { addNewExam() }
        }
    }

    //--------------------------------------------------------------------------------------

    private fun addNewExam() {

    }

    private fun pickDate() {

    }

    private fun updateDateInView(date: TextInputEditText) {

    }

}