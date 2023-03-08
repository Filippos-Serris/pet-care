package com.example.petcare.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.petcare.database.bath.Bath
import com.example.petcare.databinding.BathListBathBinding


class BathListAdapter(private val onBathClicked: (Bath) -> Unit) :
    ListAdapter<Bath, BathListAdapter.BathViewHolder>(DiffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BathViewHolder {
        return BathViewHolder(BathListBathBinding.inflate(LayoutInflater.from(parent.context)))
    }

    override fun onBindViewHolder(holder: BathViewHolder, position: Int) {
        val current = getItem(position)
        holder.itemView.setOnClickListener { onBathClicked(current) }
        holder.bind(current)

    }

    class BathViewHolder(private var binding: BathListBathBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(bath: Bath) {
            binding.apply {
                bathDate.text = bath.bathDate
                if (!bath.nextBathDate.isNullOrEmpty()) {
                    nextBathDate.text = bath.nextBathDate
                }
            }
        }
    }

    companion object {
        private val DiffCallback = object : DiffUtil.ItemCallback<Bath>() {
            override fun areItemsTheSame(
                oldBath: Bath,
                newBath: Bath
            ): Boolean {
                return oldBath === newBath
            }

            override fun areContentsTheSame(
                oldBath: Bath,
                newBath: Bath
            ): Boolean {
                return oldBath.bathDate == newBath.bathDate
            }
        }
    }
}