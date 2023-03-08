package com.example.petcare.ui.exams

import android.app.DatePickerDialog
import android.content.Context
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.icu.util.Calendar
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.petcare.PetCareApplication
import com.example.petcare.R
import com.example.petcare.database.exams.Exams
import com.example.petcare.databinding.FragmentAddExamBinding
import com.example.petcare.viewmodels.ExamsViewModel
import com.example.petcare.viewmodels.ExamsViewModelFactory
import com.google.android.material.textfield.TextInputEditText
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*


class AddExamFragment : Fragment() {
    private var _binding: FragmentAddExamBinding? = null
    private val binding get() = _binding!!

    private val examsViewModel: ExamsViewModel by activityViewModels {
        ExamsViewModelFactory((activity?.application as PetCareApplication).database.examsDao())
    }

    lateinit var exam: Exams

    private val navigationArgs: AddExamFragmentArgs by navArgs()

    private var cal = Calendar.getInstance()

    var imgList: List<String> = emptyList()

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
            examsViewModel.retrieveExam(examId).observe(this.viewLifecycleOwner) { selectedExam ->
                exam = selectedExam
                bind(exam)
            }
        } else {
            binding.saveInfoButton.setOnClickListener { addNewExam() }
        }

        binding.apply {
            examinationDate.setOnClickListener { pickDate(binding.examinationDate) }
            nextExaminationDate.setOnClickListener { pickDate(binding.nextExaminationDate) }
            addImageButton.setOnClickListener { selectImageFromGalleryResult.launch("image/*") }
            addFileButton.setOnClickListener { }
        }

    }

    //--------------------------------------------------------------------------------------
    private fun isEntryValid(): Boolean {
        return examsViewModel.isEntryValid(
            binding.examType.text.toString(), binding.examinationDate.text.toString()
        )
    }

    private fun addNewExam() {
        if (isEntryValid()) {
            examsViewModel.addNewExams(
                navigationArgs.petId,
                binding.examType.text.toString(),
                binding.examDescription.text.toString(),
                binding.examinationDate.text.toString(),
                binding.nextExaminationDate.text.toString(),
                imgList
            )
            val action =
                AddExamFragmentDirections.actionAddExamFragmentToExamListFragment(navigationArgs.petId)
            findNavController().navigate(action)
        } else {
            Toast.makeText(context, getString(R.string.exams_fields), Toast.LENGTH_SHORT).show()

        }

    }

    private fun bind(exam: Exams) {
        binding.apply {
            examType.setText(exam.examType, TextView.BufferType.SPANNABLE)
            examDescription.setText(exam.examDescription, TextView.BufferType.SPANNABLE)
            examinationDate.setText(exam.examinationDate, TextView.BufferType.SPANNABLE)
            nextExaminationDate.setText(exam.nextExaminationDate, TextView.BufferType.SPANNABLE)
            saveInfoButton.setOnClickListener { updateExams() }
        }
    }

    private fun updateExams() {
        if (isEntryValid()) {
            examsViewModel.updateExams(
                navigationArgs.examsId,
                navigationArgs.petId,
                binding.examType.text.toString(),
                binding.examDescription.text.toString(),
                binding.examinationDate.text.toString(),
                binding.nextExaminationDate.text.toString(),
                "null"
            )
            val action =
                AddExamFragmentDirections.actionAddExamFragmentToExamListFragment(navigationArgs.petId)
            findNavController().navigate(action)
        } else {
            Toast.makeText(context, getString(R.string.exams_fields), Toast.LENGTH_SHORT).show()
        }
    }

    private fun pickDate(date: TextInputEditText) {
        if (date == binding.examinationDate) {
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

    private val selectImageFromGalleryResult =
        registerForActivityResult(ActivityResultContracts.GetMultipleContents()) { uri ->
            try {
                uri?.let { saveImageToInternalStorage(it) }
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }

    private fun saveImageToInternalStorage(uri: List<Uri>) {

        val mutableList = mutableListOf<String>()

        for (imageUri in uri) {
            //binding.uriText.text = "${binding.uriText.text} ${imageUri.toString()}"
            val fileName = "${UUID.randomUUID()}" + ".jpg"
            requireContext().openFileOutput(fileName, Context.MODE_PRIVATE)
                .use { stream ->
                    val source =
                        ImageDecoder.createSource(requireContext().contentResolver, imageUri)
                    val bitmap = ImageDecoder.decodeBitmap(source)

                    val bos = ByteArrayOutputStream()
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 50, bos)
                    val bitmapData = bos.toByteArray()

                    stream.write(bitmapData)

                    val path = requireContext().filesDir.absolutePath
                    val file = File(
                        "$path/$fileName"
                    )
                    mutableList.add(file.path.toString())
                }
        }

        imgList = mutableList
    }

}