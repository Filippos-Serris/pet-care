package com.example.petcare.ui.bath

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.petcare.PetCareApplication
import com.example.petcare.database.bath.Bath
import com.example.petcare.databinding.FragmentAddBathBinding
import com.example.petcare.viewmodels.BathViewModel
import com.example.petcare.viewmodels.BathViewModelFactory

class AddBathFragment : Fragment() {
    private var _binding: FragmentAddBathBinding? = null
    private val binding get() = _binding!!

    private val bathViewModel: BathViewModel by activityViewModels {
        BathViewModelFactory((activity?.application as PetCareApplication).database.bathDao())
    }

    lateinit var bath: Bath

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
        }
        val action =
            AddBathFragmentDirections.actionAddBathFragmentToBathListFragment(navigationArgs.petId)
        findNavController().navigate(action)
    }
}