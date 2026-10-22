package com.example.myapplication

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.myapplication.databinding.ActivityDetailBinding

class DetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Nhận dữ liệu từ Intent truyền sang
        val name = intent.getStringExtra("EXTRA_NAME") ?: "Không có thông tin"
        val email = intent.getStringExtra("EXTRA_EMAIL") ?: "Không có thông tin"
        val phone = intent.getStringExtra("EXTRA_PHONE") ?: "Không có thông tin"
        val studentId = intent.getStringExtra("EXTRA_STUDENT_ID") ?: "Không có thông tin"
        val className = intent.getStringExtra("EXTRA_CLASS") ?: "Không có thông tin"

        // Hiển thị dữ liệu lên giao diện
        binding.tvDetailName.text = "Họ và tên: $name"
        binding.tvDetailEmail.text = "Email: $email"
        binding.tvDetailPhone.text = "Số điện thoại: $phone"
        binding.tvDetailStudentId.text = "Mã sinh viên: $studentId"
        binding.tvDetailClass.text = "Lớp: $className"

        // Yêu cầu: Nút gọi intent chuyển qua trang kia (Quay về trang đăng ký)
        binding.btnBackToRegister.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            // Cờ này giúp tránh việc mở quá nhiều màn hình xếp chồng lên nhau
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP)
            startActivity(intent)
        }
    }
}