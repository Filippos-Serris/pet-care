package com.example.petcare.ui.pet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
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
            val title = "${it.petName}'s information"
            val action =
                PetListFragmentDirections.actionPetListFragmentToPetDetailFragment(it.petId, title)
            this.findNavController().navigate(action)
        }
        binding.recycler.adapter = adapter

        petViewModel.allPets.observe(this.viewLifecycleOwner) { pets ->
            pets.let { adapter.submitList(it) }
        }

        binding.recycler.layoutManager = LinearLayoutManager(this.context)

        binding.addButton.setOnClickListener {
            val action =
                PetListFragmentDirections.actionPetListFragmentToAddPetFragment(getString(R.string.add_pet))
            this.findNavController().navigate(action)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    fun givePetIcon() {

    }
}