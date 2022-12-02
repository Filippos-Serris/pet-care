package com.example.petcare.ui.vaccine

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
import com.example.petcare.PetCareViewModel
import com.example.petcare.PetCareViewModelFactory
import com.example.petcare.adapters.VaccineListAdapter
import com.example.petcare.database.vaccine.Vaccine
import com.example.petcare.databinding.FragmentVaccineListBinding


class VaccineListFragment : Fragment() {
    private var _binding: FragmentVaccineListBinding? = null
    private val binding get() = _binding!!

    lateinit var vaccine: Vaccine

    private val navigationArgs: VaccineListFragmentArgs by navArgs()

    private val viewModel: PetCareViewModel by activityViewModels {
        PetCareViewModelFactory(
            (activity?.application as PetCareApplication).database.petDao(),
            (activity?.application as PetCareApplication).database.vaccineDao()
        )
    }
    //-------------------------------------------------------------------------------------------

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentVaccineListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val adapter = VaccineListAdapter {
            val action =
                VaccineListFragmentDirections.actionVaccineListFragmentToAddVaccineFragment(it.petId,
                    it.vaccineId
                )
            findNavController().navigate(action)
        }
        binding.vaccineRecycler.adapter = adapter

        val petId = navigationArgs.petId

        viewModel.retrieveVaccines(petId).observe(this.viewLifecycleOwner) { vaccines ->
            vaccines.let { adapter.submitList(it) }
        }

        binding.vaccineRecycler.layoutManager = LinearLayoutManager(this.context)

        binding.saveButton.setOnClickListener {
            val action =
                VaccineListFragmentDirections.actionVaccineListFragmentToAddVaccineFragment(
                    petId, -1
                )
            findNavController().navigate(action)
        }
    }

}