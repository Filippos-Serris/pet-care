package com.example.petcare.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.petcare.database.vaccine.Vaccine
import com.example.petcare.databinding.VaccineListVaccineBinding

class VaccineListAdapter(private val onVaccineClicked: (Vaccine) -> Unit) :
    ListAdapter<Vaccine, VaccineListAdapter.VaccineViewHolder>(DiffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VaccineViewHolder {
        return VaccineViewHolder(VaccineListVaccineBinding.inflate(LayoutInflater.from(parent.context)))
    }

    override fun onBindViewHolder(holder: VaccineViewHolder, position: Int) {
        val current = getItem(position)
        holder.itemView.setOnClickListener { onVaccineClicked(current) }
        holder.bind(current)
    }

    class VaccineViewHolder(private var binding: VaccineListVaccineBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(vaccine: Vaccine) {
            binding.apply {
                vaccineName.text = /*vaccine.vaccines.elementAt(0).toString()*/vaccine.vaccineName
                vaccineDescription.text = vaccine.vaccineDescription
                vaccinationDate.text = vaccine.vaccinationDate
                nextVaccinationDate.text = vaccine.nextVaccinationDate
            }

        }
    }

    companion object {
        private val DiffCallback = object : DiffUtil.ItemCallback<Vaccine>() {
            override fun areItemsTheSame(oldVaccine: Vaccine, newVaccine: Vaccine): Boolean {
                return oldVaccine === newVaccine
            }

            override fun areContentsTheSame(oldVaccine: Vaccine, newVaccine: Vaccine): Boolean {
                return oldVaccine.vaccineName == newVaccine.vaccineName
            }
        }
    }

}