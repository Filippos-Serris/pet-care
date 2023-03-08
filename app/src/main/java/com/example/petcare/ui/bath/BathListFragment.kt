package com.example.petcare.ui.bath

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
import com.example.petcare.R
import com.example.petcare.adapters.BathListAdapter
import com.example.petcare.database.bath.Bath
import com.example.petcare.databinding.FragmentBathListBinding
import com.example.petcare.viewmodels.BathViewModel
import com.example.petcare.viewmodels.BathViewModelFactory

class BathListFragment : Fragment() {
    private var _binding: FragmentBathListBinding? = null
    private val binding get() = _binding!!

    lateinit var bath: Bath

    private val navigationArgs: BathListFragmentArgs by navArgs()

    private val bathViewModel: BathViewModel by activityViewModels {
        BathViewModelFactory((activity?.application as PetCareApplication).database.bathDao())
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentBathListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val adapter = BathListAdapter {
            val action = BathListFragmentDirections.actionBathListFragmentToAddBathFragment(
                it.petId,
                it.bathId,
                getString(R.string.update_bath)
            )
            findNavController().navigate(action)

        }
        binding.recycler.adapter = adapter

        bathViewModel.retrieveBaths(navigationArgs.petId)
            .observe(this.viewLifecycleOwner) { baths -> baths.let { adapter.submitList(it) } }

        binding.recycler.layoutManager = LinearLayoutManager(this.context)

        binding.addButton.setOnClickListener {
            val action =
                BathListFragmentDirections.actionBathListFragmentToAddBathFragment(
                    navigationArgs.petId,
                    -1, getString(R.string.add_bath)
                )
            findNavController().navigate(action)
        }
    }
}