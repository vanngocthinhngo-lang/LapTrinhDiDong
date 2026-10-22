package com.example.myapplication

import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.myapplication.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnRegister.setOnClickListener {
            val name = binding.edtFullName.text.toString().trim()
            val email = binding.edtEmail.text.toString().trim()
            val phone = binding.edtPhone.text.toString().trim()
            val studentId = binding.edtStudentId.text.toString().trim()
            val className = binding.edtClass.text.toString().trim()

            // Reset tất cả các lỗi trước khi kiểm tra lại
            binding.tilFullName.error = null
            binding.tilEmail.error = null
            binding.tilPhone.error = null
            binding.tilStudentId.error = null
            binding.tilClass.error = null

            var isValid = true

            // Validation Họ Tên
            if (name.isEmpty()) {
                binding.tilFullName.error = "Không được để trống họ tên"
                isValid = false
            }

            // Validation Email
            if (email.isEmpty()) {
                binding.tilEmail.error = "Không được để trống email"
                isValid = false
            } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                binding.tilEmail.error = "Email sai định dạng"
                isValid = false
            }

            // Validation Số điện thoại (Chỉ nhận số, bắt đầu bằng 0, độ dài 10 số)
            val phoneRegex = Regex("^0[0-9]{9}$")
            if (phone.isEmpty()) {
                binding.tilPhone.error = "Không được để trống số điện thoại"
                isValid = false
            } else if (!phone.matches(phoneRegex)) {
                binding.tilPhone.error = "SĐT phải gồm 10 số và bắt đầu bằng 0"
                isValid = false
            }

            // Validation MSV
            if (studentId.isEmpty()) {
                binding.tilStudentId.error = "Không được để trống mã sinh viên"
                isValid = false
            }

            // Validation Lớp
            if (className.isEmpty()) {
                binding.tilClass.error = "Không được để trống lớp"
                isValid = false
            }

            // Nếu tất cả điều kiện đều pass --> Chuyển Activity
            if (isValid) {
                val intent = Intent(this, DetailActivity::class.java).apply {
                    putExtra("EXTRA_NAME", name)
                    putExtra("EXTRA_EMAIL", email)
                    putExtra("EXTRA_PHONE", phone)
                    putExtra("EXTRA_STUDENT_ID", studentId)
                    putExtra("EXTRA_CLASS", className)
                }
                startActivity(intent)
            } else {
                Toast.makeText(this, "Vui lòng kiểm tra lại thông tin!", Toast.LENGTH_SHORT).show()
            }
        }
    }
}