package com.ute.studentmvvm.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.ute.studentmvvm.R
import com.ute.studentmvvm.databinding.FragmentStudentListBinding
import com.ute.studentmvvm.model.Student
import com.ute.studentmvvm.ui.adapter.StudentAdapter
import com.ute.studentmvvm.viewmodel.StudentViewModel

class StudentListFragment : Fragment() {

    // Backing Property: Fragment có thể sống lâu hơn View của nó.
    private var _binding: FragmentStudentListBinding? = null
    private val binding get() = _binding!!

    private val viewModel: StudentViewModel by activityViewModels()
    private lateinit var studentAdapter: StudentAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentStudentListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
        observeViewModel()
        setupListeners()
    }

    private fun setupRecyclerView() {
        studentAdapter = StudentAdapter(
            onStudentClick = { student -> showStudentDetailDialog(student) },
            onDeleteClick = { student -> confirmDelete(student) }
        )

        binding.rvStudents.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = studentAdapter
            setHasFixedSize(true)
        }
    }

    private fun observeViewModel() {
        viewModel.allStudents.observe(viewLifecycleOwner) { students ->
            studentAdapter.submitList(students)
            binding.layoutEmpty.visibility = if (students.isEmpty()) View.VISIBLE else View.GONE
            binding.rvStudents.visibility = if (students.isEmpty()) View.GONE else View.VISIBLE
            binding.tvStudentCountBadge.text = "${students.size} sinh viên"
        }

        viewModel.statusMessage.observe(viewLifecycleOwner) { message ->
            if (!message.isNullOrBlank()) {
                Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun setupListeners() {
        binding.fabAdd.setOnClickListener {
            AddStudentDialogFragment().show(childFragmentManager, "ADD_STUDENT_DIALOG")
        }

        binding.toolbar.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.action_sync -> {
                    viewModel.syncFromCloud()
                    true
                }
                else -> false
            }
        }
    }

    private fun confirmDelete(student: Student) {
        AlertDialog.Builder(requireContext())
            .setTitle("Xác nhận xóa")
            .setMessage("Bạn có chắc muốn xóa ${student.fullName} khỏi CSDL Room không?")
            .setPositiveButton("Xóa") { _, _ -> viewModel.delete(student) }
            .setNegativeButton("Hủy", null)
            .show()
    }

    private fun showStudentDetailDialog(student: Student) {
        AlertDialog.Builder(requireContext())
            .setTitle("Thông tin sinh viên")
            .setMessage(
                "ID: ${student.id}\n" +
                    "Họ và tên: ${student.fullName}\n" +
                    "MSSV: ${student.studentCode}\n" +
                    "Email: ${student.email}\n" +
                    "GPA: ${student.gpa}\n" +
                    "Chuyên ngành: ${student.major}"
            )
            .setPositiveButton("Đóng", null)
            .show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null // Bắt buộc giải phóng ViewBinding.
    }
}
