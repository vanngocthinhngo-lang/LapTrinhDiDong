package com.ute.studentmvvm.network

import com.google.gson.annotations.SerializedName
import com.ute.studentmvvm.model.Student

/** DTO nhận JSON từ API demo; API này trả user profile, không có trường GPA/chuyên ngành. */
data class StudentDto(
    @SerializedName("id") val id: Int,
    @SerializedName("name") val name: String,
    @SerializedName("email") val email: String,
    @SerializedName("company") val company: CompanyDto? = null
)

data class CompanyDto(
    @SerializedName("name") val name: String? = null
)

/** Chuyển user nhận từ REST API sang Entity để lưu Room (SSOT). */
fun StudentDto.toEntity(): Student {
    return Student(
        id = 0, // Room tự sinh khóa chính cục bộ.
        fullName = name,
        studentCode = "API${id.toString().padStart(5, '0')}",
        email = email,
        gpa = 0.0, // API mẫu không cung cấp GPA nên không tự bịa điểm.
        major = "Chưa cập nhật từ REST API"
    )
}
