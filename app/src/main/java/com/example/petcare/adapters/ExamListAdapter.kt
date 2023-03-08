package com.example.petcare.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.petcare.database.exams.Exams
import com.example.petcare.databinding.ExamListExamBinding

class ExamListAdapter(private val onExamsClicked: (Exams) -> Unit) :
    ListAdapter<Exams, ExamListAdapter.ExamsViewHolder>(DiffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ExamsViewHolder {
        return ExamsViewHolder(
            ExamListExamBinding.inflate(
                LayoutInflater.from(
                    parent.context
                )
            )
        )
    }

    override fun onBindViewHolder(holder: ExamsViewHolder, position: Int) {
        val current = getItem(position)
        holder.itemView.setOnClickListener { onExamsClicked(current) }
        holder.bind(current)
    }

    class ExamsViewHolder(private var binding: ExamListExamBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(exams: Exams) {
            binding.apply {
                examType.text = exams.examType
                examDescription.text = exams.examDescription
                examinationDate.text = exams.examinationDate
                nextExaminationDate.text = exams.nextExaminationDate
            }
        }

    }

    companion object {
        private val DiffCallback = object : DiffUtil.ItemCallback<Exams>() {
            override fun areItemsTheSame(
                oldExams: Exams,
                newExams: Exams
            ): Boolean {
                return oldExams === newExams
            }

            override fun areContentsTheSame(
                oldExams: Exams,
                newExams: Exams
            ): Boolean {
                return oldExams.examType == newExams.examType
            }
        }
    }
}