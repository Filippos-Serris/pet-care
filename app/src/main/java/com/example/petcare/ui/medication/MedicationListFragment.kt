package com.example.petcare.ui.medication

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.petcare.PetCareApplication
import com.example.petcare.R
import com.example.petcare.adapters.MedicationListAdapter
import com.example.petcare.database.medication.Medication
import com.example.petcare.databinding.FragmentMedicationListBinding
import com.example.petcare.viewmodels.MedicationViewModel
import com.example.petcare.viewmodels.MedicationViewModelFactory


class MedicationListFragment : Fragment() {
    private var _binding: FragmentMedicationListBinding? = null
    private val binding get() = _binding!!

    lateinit var medication: Medication

    private val navigationArgs: MedicationListFragmentArgs by navArgs()

    private val medicationViewModel: MedicationViewModel by activityViewModels {
        MedicationViewModelFactory((activity?.application as PetCareApplication).database.medicationDao())
    }

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

        val adapter = MedicationListAdapter {
            val action =
                MedicationListFragmentDirections.actionMedicationListFragmentToAddMedicationFragment(
                    it.petId,
                    it.medicationId
                )
            findNavController().navigate(action)
        }
        binding.medicationRecycler.adapter = adapter

        val petId = navigationArgs.petId

        this.medicationViewModel.retrieveMedications(petId)
            .observe(this.viewLifecycleOwner) { medications ->
                medications.let { adapter.submitList(it) }
            }

        binding.medicationRecycler.layoutManager = LinearLayoutManager(this.context)


        binding.addButton.setOnClickListener {
            val action =
                MedicationListFragmentDirections.actionMedicationListFragmentToAddMedicationFragment(
                    petId,
                    -1
                )
            findNavController().navigate(action)
        }
    }
}