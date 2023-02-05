package com.example.petcare.ui.grooming

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
import com.example.petcare.adapters.GroomingListAdapter
import com.example.petcare.database.grooming.Grooming
import com.example.petcare.databinding.FragmentGroomingListBinding
import com.example.petcare.viewmodels.GroomingViewModel
import com.example.petcare.viewmodels.GroomingViewModelFactory


class GroomingListFragment : Fragment() {
    private var _binding: FragmentGroomingListBinding? = null
    private val binding get() = _binding!!

    lateinit var grooming: Grooming

    private val navigationArgs: GroomingListFragmentArgs by navArgs()

    private val groomingViewModel: GroomingViewModel by activityViewModels {
        GroomingViewModelFactory((activity?.application as PetCareApplication).database.groomingDao())
    }

    //------------------------------------------------------------------------------------------------------------

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentGroomingListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val adapter = GroomingListAdapter {
            val action =
                GroomingListFragmentDirections.actionGroomingListFragmentToAddGroomingFragment(
                    it.petId,
                    it.groomingId
                )
            findNavController().navigate(action)
        }
        binding.recycler.adapter = adapter

        this.groomingViewModel.retrieveGroomings(navigationArgs.petId)
            .observe(this.viewLifecycleOwner) { groomings ->
                groomings.let { adapter.submitList(it) }
            }

        binding.recycler.layoutManager = LinearLayoutManager(this.context)

        binding.addButton.setOnClickListener {
            val action =
                GroomingListFragmentDirections.actionGroomingListFragmentToAddGroomingFragment(
                    navigationArgs.petId,
                    -1
                )
            findNavController().navigate(action)
        }


    }

}