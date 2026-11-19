package com.ute.studentmvvm.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.ute.studentmvvm.model.Student
import kotlinx.coroutines.flow.Flow

@Dao
interface StudentDao {

    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insertStudent(student: Student): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(students: List<Student>)

    @Update
    suspend fun updateStudent(student: Student)

    @Delete
    suspend fun deleteStudent(student: Student)

    @Query("DELETE FROM students_table WHERE id = :id")
    suspend fun deleteStudentById(id: Int)

    // Flow tự phát danh sách mới khi bảng students_table thay đổi.
    @Query("SELECT * FROM students_table ORDER BY id DESC")
    fun getAllStudents(): Flow<List<Student>>

    @Query("SELECT EXISTS(SELECT 1 FROM students_table WHERE student_code = :code LIMIT 1)")
    suspend fun existsByStudentCode(code: String): Boolean
}
