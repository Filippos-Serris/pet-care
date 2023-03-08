package com.example.petcare.ui.exams

import ExamResultAdapter
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.petcare.PetCareApplication
import com.example.petcare.database.exams.Exams
import com.example.petcare.databinding.FragmentExamDetailBinding
import com.example.petcare.viewmodels.ExamsViewModel
import com.example.petcare.viewmodels.ExamsViewModelFactory

class ExamDetailFragment : Fragment() {
    private var _binding: FragmentExamDetailBinding? = null
    private val binding get() = _binding!!

    private val examsViewModel: ExamsViewModel by activityViewModels { ExamsViewModelFactory((activity?.application as PetCareApplication).database.examsDao()) }

    private val navigationArgs: ExamDetailFragmentArgs by navArgs()

    private lateinit var exam: Exams

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentExamDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val examId = navigationArgs.examId

        examsViewModel.retrieveExam(examId)
            .observe(this.viewLifecycleOwner) { selectedExam ->
                exam = selectedExam
                bind(exam)
            }
    }

    private fun bind(exam: Exams) {
        binding.apply {
            examType.text = exam.examType
            examinationDate.text = exam.examinationDate
            nextExaminationDate.text = exam.nextExaminationDate

            Glide.with(img1).load(exam.examinationResults?.get(0)).into(img1)
            Glide.with(img2).load(exam.examinationResults?.get(1)).into(img2)

            val adapter = ExamResultAdapter(exam.examinationResults, requireContext()) {
                val action =
                    ExamDetailFragmentDirections.actionExamDetailFragmentToExamResultPreviewFragment(
                        it
                    )
                findNavController().navigate(action)

            }

            binding.recycler.adapter = adapter
            binding.recycler.layoutManager = LinearLayoutManager(context)
        }
    }
}