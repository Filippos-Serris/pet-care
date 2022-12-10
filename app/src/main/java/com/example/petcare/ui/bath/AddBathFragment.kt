package com.example.petcare.ui.bath

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.petcare.PetCareApplication
import com.example.petcare.databinding.FragmentAddBathBinding
import com.example.petcare.viewmodels.BathViewModel
import com.example.petcare.viewmodels.BathViewModelFactory

class AddBathFragment : Fragment() {
    private var _binding: FragmentAddBathBinding? = null
    private val binding get() = _binding!!

    private val bathViewModel: BathViewModel by activityViewModels {
        BathViewModelFactory((activity?.application as PetCareApplication).database.bathDao())
    }

    private val navigationArgs: AddBathFragmentArgs by navArgs()

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

        binding.saveButton.setOnClickListener { addNewBath() }
    }

    private fun isEntryValid(): Boolean {
        return this.bathViewModel.isEntryValid(
            binding.bathDate.text.toString(),
            binding.nextBathDate.text.toString()
        )
    }

    private fun addNewBath() {
        if (isEntryValid()) {
            this.bathViewModel.addNewBath(
                navigationArgs.petId,
                binding.bathDate.text.toString(),
                binding.nextBathDate.text.toString()
            )
        }

        val action =
            AddBathFragmentDirections.actionAddBathFragmentToBathListFragment(navigationArgs.petId)
        findNavController().navigate(action)
    }
}