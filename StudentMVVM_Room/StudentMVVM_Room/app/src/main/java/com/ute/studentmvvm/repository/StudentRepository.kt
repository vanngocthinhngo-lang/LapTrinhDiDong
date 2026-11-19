package com.ute.studentmvvm.repository

import com.ute.studentmvvm.data.StudentDao
import com.ute.studentmvvm.model.Student
import com.ute.studentmvvm.network.RetrofitClient
import com.ute.studentmvvm.network.StudentApiService
import com.ute.studentmvvm.network.toEntity
import kotlinx.coroutines.flow.Flow

class StudentRepository(
    private val studentDao: StudentDao,
    private val apiService: StudentApiService = RetrofitClient.apiService
) {
    // Single Source of Truth: UI chỉ đọc dữ liệu từ Room.
    val allStudents: Flow<List<Student>> = studentDao.getAllStudents()

    suspend fun insert(student: Student): Long = studentDao.insertStudent(student)

    suspend fun delete(student: Student) = studentDao.deleteStudent(student)

    suspend fun studentCodeExists(code: String): Boolean =
        studentDao.existsByStudentCode(code)

    /** Lấy JSON từ REST API, chuyển thành Entity rồi ghi Room trước khi UI nhận dữ liệu. */
    suspend fun syncFromCloud(): Result<Int> {
        return try {
            val remoteList = apiService.getStudents()
            val entities = remoteList.map { it.toEntity() }
            studentDao.insertAll(entities)
            Result.success(entities.size)
        } catch (exception: Exception) {
            Result.failure(exception)
        }
    }
}
