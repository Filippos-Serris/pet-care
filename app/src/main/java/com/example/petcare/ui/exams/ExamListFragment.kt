package com.example.petcare.ui.exams

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.petcare.PetCareApplication
import com.example.petcare.R
import com.example.petcare.adapters.ExamListAdapter
import com.example.petcare.databinding.FragmentExamListBinding
import com.example.petcare.viewmodels.ExamsViewModel
import com.example.petcare.viewmodels.ExamsViewModelFactory

class ExamListFragment : Fragment() {
    private var _binding: FragmentExamListBinding? = null
    private val binding get() = _binding!!

    private val navigationArgs: ExamListFragmentArgs by navArgs()

    private val examsViewModel: ExamsViewModel by activityViewModels {
        ExamsViewModelFactory((activity?.application as PetCareApplication).database.examsDao())
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentExamListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val adapter = ExamListAdapter {
            val action =
                ExamListFragmentDirections.actionExamListFragmentToExamDetailFragment(navigationArgs.petId)
            findNavController().navigate(action)
        }
        binding.recycler.adapter = adapter

        examsViewModel.retrieveExams(navigationArgs.petId)
            .observe(this.viewLifecycleOwner) { exams -> exams.let { adapter.submitList(it) } }

        binding.recycler.layoutManager = LinearLayoutManager(this.context)


        binding.addButton.setOnClickListener {
            val action =
                ExamListFragmentDirections.actionExamListFragmentToAddExamFragment(
                    navigationArgs.petId,
                    -1, getString(R.string.add_exam)
                )
            findNavController().navigate(action)
        }
    }

}