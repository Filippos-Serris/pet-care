package com.example.petcare.viewmodels

import androidx.lifecycle.*
import com.example.petcare.database.exams.Exams
import com.example.petcare.database.exams.ExamsDao
import kotlinx.coroutines.launch

class ExamsViewModel(private val examsDao: ExamsDao) : ViewModel() {
    //private val _examImages = MutableLiveData<List<String>>()
    //var examImages: LiveData<List<String>> = _examImages

    private fun insertExam(exams: Exams) {
        viewModelScope.launch { examsDao.insertExam(exams) }
    }

    fun retrieveExams(id: Int): LiveData<List<Exams>> {
        return examsDao.getExams(id).asLiveData()
    }

    fun retrieveExam(id: Int): LiveData<Exams> {
        return examsDao.getExam(id).asLiveData()
    }

    private fun updateExams(exams: Exams) {
        viewModelScope.launch { examsDao.updateExam(exams) }
    }

    //-------------
    private fun getNewExamsEntry(
        petId: Int,
        examType: String,
        examDescription: String,
        examinationDate: String,
        nextExaminationDate: String,
        examsResult: List<String>
    ): Exams {
        return Exams(
            petId = petId,
            examType = examType,
            examDescription = examDescription,
            examinationDate = examinationDate,
            nextExaminationDate = nextExaminationDate,
            examinationResults = examsResult
        )

    }

    fun addNewExams(
        petId: Int,
        examType: String,
        examDescription: String,
        examinationDate: String,
        nextExaminationDate: String,
        examResults: List<String>
    ) {
        val newExam =
            getNewExamsEntry(
                petId,
                examType,
                examDescription,
                examinationDate,
                nextExaminationDate,
                examResults
            )
        insertExam(newExam)

    }

    //-------------
    private fun getUpdatedExamsEntry(
        examId: Int, petId: Int,
        examType: String,
        examDescription: String,
        examinationDate: String,
        nextExaminationDate: String,
        examinationResults: String
    ): Exams {
        return Exams(
            examId = examId,
            petId = petId,
            examType = examType,
            examDescription = examDescription,
            examinationDate = examinationDate,
            nextExaminationDate = nextExaminationDate,
            examinationResults = emptyList()
        )
    }

    fun updateExams(
        examId: Int,
        petId: Int,
        examType: String,
        examDescription: String,
        examinationDate: String,
        nextExaminationDate: String,
        examinationResults: String
    ) {
        val updatedExam =
            getUpdatedExamsEntry(
                examId,
                petId,
                examType,
                examDescription,
                examinationDate,
                nextExaminationDate,
                examinationResults
            )
        updateExams(updatedExam)

    }

    //----------
    fun isEntryValid(examType: String, examinationDate: String): Boolean {
        if (examType.isBlank() || examinationDate.isBlank())
            return false
        return true
    }


}

class ExamsViewModelFactory(private val examsDao: ExamsDao) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ExamsViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return ExamsViewModel(examsDao) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}