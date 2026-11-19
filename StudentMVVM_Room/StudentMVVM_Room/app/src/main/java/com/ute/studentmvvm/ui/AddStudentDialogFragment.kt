package com.ute.studentmvvm.ui

import android.app.Dialog
import android.os.Bundle
import android.util.Patterns
import android.view.LayoutInflater
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import com.ute.studentmvvm.databinding.DialogAddStudentBinding
import com.ute.studentmvvm.model.Student
import com.ute.studentmvvm.viewmodel.StudentViewModel

class AddStudentDialogFragment : DialogFragment() {

    private val viewModel: StudentViewModel by activityViewModels()

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val binding = DialogAddStudentBinding.inflate(LayoutInflater.from(requireContext()))
        val dialog = AlertDialog.Builder(requireContext())
            .setTitle("Thêm sinh viên mới")
            .setView(binding.root)
            .setPositiveButton("Lưu vào Room", null)
            .setNegativeButton("Hủy", null)
            .create()

        // Gắn listener sau khi dialog hiển thị để không tự đóng khi dữ liệu chưa hợp lệ.
        dialog.setOnShowListener {
            dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener {
                val name = binding.etFullName.text?.toString()?.trim().orEmpty()
                val code = binding.etStudentCode.text?.toString()?.trim().orEmpty()
                val email = binding.etEmail.text?.toString()?.trim().orEmpty()
                val gpaText = binding.etGpa.text?.toString()?.trim().orEmpty()
                val major = binding.etMajor.text?.toString()?.trim().orEmpty()
                val gpa = gpaText.toDoubleOrNull()

                when {
                    name.isBlank() -> binding.etFullName.error = "Vui lòng nhập họ tên"
                    code.isBlank() -> binding.etStudentCode.error = "Vui lòng nhập MSSV"
                    email.isNotBlank() && !Patterns.EMAIL_ADDRESS.matcher(email).matches() ->
                        binding.etEmail.error = "Email chưa đúng định dạng"
                    gpa == null || gpa !in 0.0..4.0 ->
                        binding.etGpa.error = "GPA phải nằm trong khoảng 0.0 đến 4.0"
                    else -> {
                        val student = Student(
                            fullName = name,
                            studentCode = code,
                            email = email.ifBlank { "${code.lowercase()}@ute.udn.vn" },
                            gpa = gpa,
                            major = major.ifBlank { "Công Nghệ Thông Tin" }
                        )
                        viewModel.insert(student)
                        dialog.dismiss()
                    }
                }
            }
        }
        return dialog
    }
}
