package com.ute.studentmvvm.ui

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.fragment.app.commit
import com.ute.studentmvvm.R
import com.ute.studentmvvm.data.AppDatabase
import com.ute.studentmvvm.repository.StudentRepository
import com.ute.studentmvvm.viewmodel.StudentViewModel
import com.ute.studentmvvm.viewmodel.StudentViewModelFactory

class MainActivity : AppCompatActivity() {

    private val database by lazy { AppDatabase.getDatabase(this) }
    private val repository by lazy { StudentRepository(database.studentDao()) }

    // StudentListFragment/AddStudentDialogFragment dùng activityViewModels().
    // Override factory để các Fragment nhận đúng Repository đã khởi tạo ở Activity.
    override val defaultViewModelProviderFactory: ViewModelProvider.Factory
        get() = StudentViewModelFactory(repository)

    private val viewModel: StudentViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        // Khởi tạo ViewModel một lần ở cấp Activity; ViewModel tồn tại khi Fragment thay đổi.
        @Suppress("UNUSED_VARIABLE")
        val keepViewModel = viewModel

        if (savedInstanceState == null) {
            supportFragmentManager.commit {
                setReorderingAllowed(true)
                replace(R.id.fragment_container_view, StudentListFragment())
            }
        }
    }
}
