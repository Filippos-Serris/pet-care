package com.example.petcare.ui.bath

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.petcare.R
import com.example.petcare.database.bath.Bath
import com.example.petcare.databinding.FragmentBathListBinding

class BathListFragment : Fragment() {
    private var _binding: FragmentBathListBinding? = null
    private val binding get() = _binding!!

    private val navigationArgs: BathListFragmentArgs by navArgs()

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

        binding.addButton.setOnClickListener {
            val action =
                BathListFragmentDirections.actionBathListFragmentToAddBathFragment(
                    navigationArgs.petId,
                    -1
                )
            findNavController().navigate(action)
        }
    }
}