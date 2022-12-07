package com.example.petcare.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.petcare.database.medication.Medication
import com.example.petcare.databinding.MedicationListMedicationBinding

class MedicationListAdapter(private val onMedicationClicked: (Medication) -> Unit) :
    ListAdapter<Medication, MedicationListAdapter.MedicationViewHolder>(DiffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MedicationViewHolder {
        return MedicationViewHolder(
            MedicationListMedicationBinding.inflate(
                LayoutInflater.from(
                    parent.context
                )
            )
        )
    }

    override fun onBindViewHolder(holder: MedicationViewHolder, position: Int) {
        val current = getItem(position)
        holder.itemView.setOnClickListener { onMedicationClicked(current) }
        holder.bind(current)

    }

    class MedicationViewHolder(private var binding: MedicationListMedicationBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(medication: Medication) {
            binding.apply {
                medicationName.text = medication.medicationName
                medicationDescription.text = medication.medicationDescription
                medicationDosage.text = medication.medicationDosage
                medicationStartDate.text = medication.medicationStartDate
                medicationEndDate.text = medication.medicationEndDate
                medicationRepetition.text = medication.medicationRepetition
            }
        }
    }

    companion object {
        private val DiffCallback = object : DiffUtil.ItemCallback<Medication>() {
            override fun areItemsTheSame(
                oldMedication: Medication,
                newMedication: Medication
            ): Boolean {
                return oldMedication === newMedication
            }

            override fun areContentsTheSame(
                oldMedication: Medication,
                newMedication: Medication
            ): Boolean {
                return oldMedication.medicationName == newMedication.medicationName
            }
        }
    }
}


