package com.example.petcare.ui.vaccine

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
import com.example.petcare.database.vaccine.Vaccine
import com.example.petcare.databinding.FragmentAddVaccineBinding
import com.example.petcare.viewmodels.VaccineViewModel
import com.example.petcare.viewmodels.VaccineViewModelFactory

class AddVaccineFragment : Fragment() {
    private var _binding: FragmentAddVaccineBinding? = null
    private val binding get() = _binding!!

    /*private val vaccineViewModel: PetCareViewModel by activityViewModels {
        PetCareViewModelFactory(
            (activity?.application as PetCareApplication).database.petDao(),
            (activity?.application as PetCareApplication).database.vaccineDao()
        )
    }*/

    private val vaccineViewModel: VaccineViewModel by activityViewModels {
        VaccineViewModelFactory((activity?.application as PetCareApplication).database.vaccineDao())
    }

    private val navigationArgs: AddVaccineFragmentArgs by navArgs()

    lateinit var vaccine: Vaccine
    //-----------------------------------------------------------------------------

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentAddVaccineBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val vaccineId = navigationArgs.vacccineId

        if (vaccineId > 0) {
            this.vaccineViewModel.retrieveVaccine(vaccineId)
                .observe(this.viewLifecycleOwner) { selectedVaccine ->
                    vaccine = selectedVaccine
                    bind(vaccine)
                }
        } else {
            binding.saveButton.setOnClickListener { addNewVaccine() }
        }
    }

    private fun isEntryValid(): Boolean {
        return this.vaccineViewModel.isEntryValid(
            binding.vaccineName.text.toString(),
            binding.vaccineDescription.text.toString(),
            binding.vaccinationDate.text.toString(),
            binding.nextVaccinationDate.text.toString()
        )
    }

    private fun addNewVaccine() {
        if (isEntryValid()) {
            this.vaccineViewModel.addNewVaccine(
                //petId,
                navigationArgs.petId,
                binding.vaccineName.text.toString(),
                binding.vaccineDescription.text.toString(),
                binding.vaccinationDate.text.toString(),
                binding.nextVaccinationDate.text.toString()
            )
        }
        val action =
            AddVaccineFragmentDirections.actionAddVaccineFragmentToVaccineListFragment(
                navigationArgs.petId
            )
        findNavController().navigate(action)
    }

    private fun bind(vaccine: Vaccine) {
        binding.apply {
            vaccineName.setText(vaccine.vaccineName, TextView.BufferType.SPANNABLE)
            vaccineDescription.setText(vaccine.vaccineDescription, TextView.BufferType.SPANNABLE)
            vaccinationDate.setText(vaccine.vaccinationDate, TextView.BufferType.SPANNABLE)
            nextVaccinationDate.setText(vaccine.nextVaccinationDate, TextView.BufferType.SPANNABLE)
            saveButton.setOnClickListener { updateVaccine() }

        }
    }

    private fun updateVaccine() {
        if (isEntryValid()) {
            this.vaccineViewModel.updateVaccine(
                this.navigationArgs.vacccineId,
                this.navigationArgs.petId,
                this.binding.vaccineName.text.toString(),
                this.binding.vaccineDescription.text.toString(),
                this.binding.vaccinationDate.text.toString(),
                this.binding.nextVaccinationDate.text.toString()
            )
        }
        val action = AddVaccineFragmentDirections.actionAddVaccineFragmentToVaccineListFragment(
            navigationArgs.petId
        )
        findNavController().navigate(action)
    }
}