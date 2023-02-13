package com.example.petcare.ui.vaccine

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
import com.example.petcare.adapters.VaccineListAdapter
import com.example.petcare.database.vaccine.Vaccine
import com.example.petcare.databinding.FragmentVaccineListBinding
import com.example.petcare.viewmodels.VaccineViewModel
import com.example.petcare.viewmodels.VaccineViewModelFactory


class VaccineListFragment : Fragment() {
    private var _binding: FragmentVaccineListBinding? = null
    private val binding get() = _binding!!

    lateinit var vaccine: Vaccine

    private val navigationArgs: VaccineListFragmentArgs by navArgs()

    /*private val vaccineViewModel: PetCareViewModel by activityViewModels {
        PetCareViewModelFactory(
            (activity?.application as PetCareApplication).database.petDao(),
            (activity?.application as PetCareApplication).database.vaccineDao()
        )
    }*/

    private val vaccineViewModel: VaccineViewModel by activityViewModels {
        VaccineViewModelFactory((activity?.application as PetCareApplication).database.vaccineDao())
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
                VaccineListFragmentDirections.actionVaccineListFragmentToAddVaccineFragment(
                    it.petId,
                    it.vaccineId
                )
            findNavController().navigate(action)
        }
        binding.recycler.adapter = adapter

        val petId = navigationArgs.petId

        vaccineViewModel.retrieveVaccines(petId).observe(this.viewLifecycleOwner) { vaccines ->
            vaccines.let { adapter.submitList(it) }
        }

        binding.recycler.layoutManager = LinearLayoutManager(this.context)

        binding.addButton.setOnClickListener {
            val action =
                VaccineListFragmentDirections.actionVaccineListFragmentToAddVaccineFragment(
                    petId, -1
                )
            findNavController().navigate(action)
        }
    }

}