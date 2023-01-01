package com.example.petcare.ui.pet

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.petcare.PetCareApplication
import com.example.petcare.R
import com.example.petcare.adapters.PetListAdapter
import com.example.petcare.databinding.FragmentPetListBinding
import com.example.petcare.viewmodels.PetViewModel
import com.example.petcare.viewmodels.PetViewModelFactory


class PetListFragment : Fragment() {
    private var _binding: FragmentPetListBinding? = null
    private val binding get() = _binding!!

    /*private val petViewModel: PetCareViewModel by activityViewModels {
        PetCareViewModelFactory(
            (activity?.application as PetCareApplication).database.petDao(),
            (activity?.application as PetCareApplication).database.vaccineDao()
        )
    }*/

    private val petViewModel: PetViewModel by activityViewModels {
        PetViewModelFactory((activity?.application as PetCareApplication).database.petDao())
    }
    // ------------------------------------------------------------------------------------

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentPetListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val adapter = PetListAdapter {
            //findNavController().navigate(R.id.action_petListFragment_to_petDetailFragment)
            val title = it.petName + " information"
            val action =
                PetListFragmentDirections.actionPetListFragmentToPetDetailFragment(it.petId, title)
            this.findNavController().navigate(action)
        }
        binding.recyclerView.adapter = adapter

        this.petViewModel.allPets.observe(this.viewLifecycleOwner) { pets ->
            pets.let { adapter.submitList(it) }
        }

        binding.recyclerView.layoutManager = LinearLayoutManager(this.context)

        binding.newPetButton.setOnClickListener {
            val action =
                PetListFragmentDirections.actionPetListFragmentToAddPetFragment(getString(R.string.add_pet_title))
            this.findNavController().navigate(action)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}