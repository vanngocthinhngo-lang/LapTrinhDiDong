# StudentMVVM - Room Database & REST API

Ứng dụng Android Kotlin quản lý sinh viên theo tài liệu **HUONG_DAN_THUC_HANH_STEP_BY_STEP_ROOM_MVVM.pdf** của học phần Lập trình trên ĐTDĐ.

## 1. Công nghệ và kiến trúc

- Kotlin, Android Views, Single-Activity + Fragment.
- MVVM 4 tầng: UI → ViewModel → Repository → Data Sources.
- Room Database (SQLite), DAO, Flow, Kotlin Coroutines.
- LiveData để Fragment quan sát dữ liệu; ListAdapter + DiffUtil để cập nhật RecyclerView.
- Retrofit + Gson để gọi REST API.
- ViewBinding; Fragment giải phóng `_binding` trong `onDestroyView()`.

Luồng dữ liệu: người dùng thao tác trong UI → ViewModel → Repository → Room DAO → Flow → ViewModel/LiveData → Fragment/ListAdapter.

## 2. Cấu trúc thư mục chính

```text
com.ute.studentmvvm/
├── model/Student.kt
├── data/StudentDao.kt
├── data/AppDatabase.kt
├── network/StudentDto.kt
├── network/StudentApiService.kt
├── network/RetrofitClient.kt
├── repository/StudentRepository.kt
├── viewmodel/StudentViewModel.kt
├── viewmodel/StudentViewModelFactory.kt
└── ui/
    ├── MainActivity.kt
    ├── StudentListFragment.kt
    ├── AddStudentDialogFragment.kt
    └── adapter/StudentAdapter.kt
```

## 3. Yêu cầu môi trường

- Android Studio có hỗ trợ Android Gradle Plugin 8.5.2.
- JDK 17.
- Android SDK 34 (compile/target) và thiết bị/máy ảo có API 24 trở lên.
- Kết nối Internet khi Gradle tải các thư viện lần đầu và khi bấm Đồng bộ Cloud.

## 4. Mở project

1. Giải nén `StudentMVVM_Room.zip`.
2. Mở Android Studio → **Open** → chọn thư mục `StudentMVVM_Room`.
3. Chọn JDK 17 trong Gradle JDK nếu Android Studio yêu cầu.
4. Chờ Gradle Sync tải dependencies, sau đó chọn Emulator hoặc điện thoại Android và bấm Run.

> Ghi chú đóng gói: thư mục Gradle Wrapper có `gradle-wrapper.properties`; gói nguồn này không kèm `gradle-wrapper.jar` và hai script `gradlew`/`gradlew.bat`. Nếu Android Studio báo thiếu wrapper, tạo một project Empty Views Activity mới bằng Android Studio để IDE sinh wrapper, sau đó chép các thư mục `app/src` và các file build/settings của gói này vào project vừa tạo (giữ lại `gradlew`, `gradlew.bat`, `gradle-wrapper.jar` do Android Studio tạo). Có thể dùng Gradle cài sẵn trên máy nếu đã cấu hình.

## 5. Chức năng

1. Hiển thị 4 sinh viên mẫu được Room chèn vào SQLite ở lần tạo database đầu tiên.
2. Thêm sinh viên qua hộp thoại; bắt buộc họ tên, MSSV, GPA; kiểm tra GPA từ 0.0 đến 4.0 và định dạng email.
3. Xem thông tin chi tiết sinh viên khi chạm vào thẻ.
4. Xóa sinh viên có hộp thoại xác nhận.
5. Đồng bộ hồ sơ từ REST API mẫu; dữ liệu API được ghi vào Room trước khi hiển thị.
6. Danh sách cập nhật tự động qua Room Flow → LiveData → ListAdapter/DiffUtil.

## 6. REST API demo và ánh xạ dữ liệu

Tài liệu bài thực hành yêu cầu có `StudentDto`, `StudentApiService` và `RetrofitClient`, nhưng không ghi cụ thể base URL, JSON schema hay endpoint. Để nút **Đồng bộ Cloud** có thể chạy demo ngay, project chọn API công khai JSONPlaceholder: `https://jsonplaceholder.typicode.com/` với endpoint `GET /users`.

API JSONPlaceholder trả về 10 user profile (id, name, email, company...), không phải dữ liệu sinh viên thật và không có trường GPA. Vì vậy mapper trong `StudentDto.kt` chuyển đổi như sau:

- `name` → `fullName`.
- `email` → `email`.
- Tạo mã định danh demo dạng `API00001` từ `id` → `studentCode`.
- GPA để `0.0` và chuyên ngành ghi `Chưa cập nhật từ REST API`, không tự suy đoán dữ liệu mà API không cung cấp.
- Các bản ghi cloud được chèn vào Room; Room tiếp tục là Single Source of Truth cho UI. Index unique trên `student_code` giúp đồng bộ lặp lại không tạo bản sao cùng mã.

Khi mất Internet, danh sách cục bộ trong Room vẫn hoạt động; thao tác đồng bộ sẽ báo lỗi kết nối.

## 7. Kiểm chứng theo trang 15 của tài liệu

1. Chạy ứng dụng và kiểm tra 4 hồ sơ mẫu.
2. Nhấn nút `+`, thêm sinh viên, bấm Lưu vào Room và kiểm tra danh sách đổi ngay.
3. Nhấn biểu tượng thùng rác, xác nhận xóa và kiểm tra dòng biến mất.
4. Xoay màn hình để kiểm tra ViewModel giữ trạng thái.
5. Mở **View → Tool Windows → App Inspection → Database Inspector**.
6. Chọn database `ute_student_room.db`, mở bảng `students_table`, bật **Live updates**.
7. Thêm/xóa một sinh viên và quan sát dữ liệu Room thay đổi.
8. Nhấn biểu tượng đồng bộ trên Toolbar khi có Internet; kiểm tra các bản ghi `API...` xuất hiện trong danh sách và Database Inspector.

## 8. Gợi ý quay video demo

Quay liên tục các thao tác: mở app (4 sinh viên ban đầu) → thêm sinh viên → xem chi tiết → xóa một sinh viên → đồng bộ Cloud → mở Database Inspector và cho thấy bảng `students_table` thay đổi. Nên nói ngắn gọn vai trò Room, DAO, Repository, ViewModel, Flow và ListAdapter trong lúc demo.

## 9. Đưa source lên GitHub (Windows)

Mở Terminal tại thư mục project và chạy:

```bash
git init
git add .
git commit -m "Initial StudentMVVM Room project"
git branch -M main
git remote add origin https://github.com/<your-account>/StudentMVVM_Room.git
git push -u origin main
```

Thay `<your-account>` bằng tài khoản GitHub và tạo repository rỗng trước khi chạy lệnh `git remote add`.
