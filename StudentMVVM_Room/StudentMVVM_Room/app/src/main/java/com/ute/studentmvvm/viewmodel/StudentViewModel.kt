package com.ute.studentmvvm.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.ute.studentmvvm.model.Student
import com.ute.studentmvvm.repository.StudentRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class StudentViewModel(private val repository: StudentRepository) : ViewModel() {

    // Room Flow được chuyển thành LiveData để Fragment quan sát theo vòng đời.
    val allStudents: LiveData<List<Student>> = repository.allStudents.asLiveData(Dispatchers.IO)

    private val _statusMessage = MutableLiveData<String>()
    val statusMessage: LiveData<String> get() = _statusMessage

    fun insert(student: Student) = viewModelScope.launch(Dispatchers.IO) {
        try {
            if (repository.studentCodeExists(student.studentCode)) {
                _statusMessage.postValue("MSSV ${student.studentCode} đã tồn tại.")
                return@launch
            }
            repository.insert(student)
            _statusMessage.postValue("Đã thêm sinh viên: ${student.fullName}")
        } catch (exception: Exception) {
            _statusMessage.postValue("Lỗi khi thêm: ${exception.message ?: "Không xác định"}")
        }
    }

    fun delete(student: Student) = viewModelScope.launch(Dispatchers.IO) {
        try {
            repository.delete(student)
            _statusMessage.postValue("Đã xóa sinh viên: ${student.fullName}")
        } catch (exception: Exception) {
            _statusMessage.postValue("Lỗi khi xóa: ${exception.message ?: "Không xác định"}")
        }
    }

    fun syncFromCloud() = viewModelScope.launch(Dispatchers.IO) {
        _statusMessage.postValue("Đang kết nối máy chủ để đồng bộ...")
        val result = repository.syncFromCloud()
        if (result.isSuccess) {
            _statusMessage.postValue(
                "Đã đồng bộ ${result.getOrNull() ?: 0} hồ sơ từ REST API vào Room."
            )
        } else {
            _statusMessage.postValue(
                "Đồng bộ thất bại: ${result.exceptionOrNull()?.message ?: "Kiểm tra Internet và thử lại."}"
            )
        }
    }
}
