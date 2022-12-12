package com.example.petcare.ui.grooming

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.petcare.PetCareApplication
import com.example.petcare.R
import com.example.petcare.database.grooming.Grooming
import com.example.petcare.databinding.FragmentAddGroomingBinding
import com.example.petcare.databinding.FragmentAddMedicationBinding
import com.example.petcare.databinding.FragmentPetDetailBinding
import com.example.petcare.viewmodels.GroomingViewModel
import com.example.petcare.viewmodels.GroomingViewModelFactory

class AddGroomingFragment : Fragment() {
    private var _binding: FragmentAddGroomingBinding? = null
    private val binding get() = _binding!!

    private val groomingViewModel: GroomingViewModel by activityViewModels {
        GroomingViewModelFactory((activity?.application as PetCareApplication).database.groomingDao())
    }

    lateinit var grooming: Grooming

    private val navigationArg: AddGroomingFragmentArgs by navArgs()

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

        binding.saveInfoButton.setOnClickListener { addNewGrooming() }
    }

    private fun isEntryValid(): Boolean {
        return this.groomingViewModel.isEntryValid(
            binding.groomingDate.text.toString(),
            binding.nextGroomingDate.text.toString()
        )
    }


    private fun addNewGrooming() {
        if (isEntryValid()) {
            this.groomingViewModel.addNewGrooming(
                navigationArg.petId,
                binding.groomingDate.text.toString(), binding.nextGroomingDate.text.toString()
            )
        }
        val action =
            AddGroomingFragmentDirections.actionAddGroomingFragmentToGroomingListFragment(
                navigationArg.petId
            )
        findNavController().navigate(action)
    }
}