package com.example.petcare.ui.medication

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.petcare.R
import com.example.petcare.databinding.FragmentMedicationListBinding


class MedicationListFragment : Fragment() {
    private var _binding: FragmentMedicationListBinding? = null
    private val binding get() = _binding!!

    private val navigationArgs: MedicationListFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentMedicationListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val petId = navigationArgs.petId
        binding.addButton.setOnClickListener {
            val action=MedicationListFragmentDirections.actionMedicationListFragmentToAddMedicationFragment(petId,-1)
            findNavController().navigate(action)
        }
    }
}