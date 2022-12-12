package com.example.petcare.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.petcare.database.grooming.Grooming
import com.example.petcare.database.medication.Medication
import com.example.petcare.databinding.GroomingListGroomingBinding

class GroomingListAdapter(private val onGroomingClicked: (Grooming) -> Unit) :
    ListAdapter<Grooming, GroomingListAdapter.GroomingViewHolder>(DiffCallback) {

    class GroomingViewHolder(private var binding: GroomingListGroomingBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(grooming: Grooming) {
            binding.apply {
                groomingDate.text = grooming.groomingDate
                nextGroomingDate.text = grooming.nextGroomingDate
            }
        }
    }

    companion object {
        private val DiffCallback = object : DiffUtil.ItemCallback<Grooming>() {
            override fun areItemsTheSame(
                oldGrooming: Grooming,
                newGrooming: Grooming
            ): Boolean {
                return oldGrooming === newGrooming
            }

            override fun areContentsTheSame(
                oldGrooming: Grooming,
                newGrooming: Grooming
            ): Boolean {
                return oldGrooming.groomingDate == newGrooming.groomingDate
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GroomingViewHolder {
        return GroomingViewHolder(GroomingListGroomingBinding.inflate(LayoutInflater.from(parent.context)))
    }

    override fun onBindViewHolder(holder: GroomingViewHolder, position: Int) {
        val current = getItem(position)
        holder.itemView.setOnClickListener { onGroomingClicked(current) }
        holder.bind(current)
    }
}