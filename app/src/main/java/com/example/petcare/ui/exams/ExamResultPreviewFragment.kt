package com.example.petcare.ui.exams

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.example.petcare.databinding.FragmentExamResultPreviewBinding

class ExamResultPreviewFragment : Fragment() {
    private var _binding: FragmentExamResultPreviewBinding? = null
    private val binding get() = _binding!!

    private val navigationArgs: ExamResultPreviewFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentExamResultPreviewBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val uri = navigationArgs.uri

        Glide.with(binding.img).load(uri).into(binding.img)
    }


}