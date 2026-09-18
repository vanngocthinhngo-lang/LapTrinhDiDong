import java.util.Scanner

data class Student(
    val studentId: String,
    val fullName: String,
    val age: Int,
    val major: String,
    val gpa: Double
)

fun main() {
    val scanner = Scanner(System.`in`)
    val studentList = mutableListOf(
        Student("SV049", "Ngo Van Ngoc Thinh", 20, "CNTT", 8.6),
        Student("SV050", "Tran Thi Mai", 21, "Kinh Te", 4.5),
        Student("SV051", "Le Hoang Nam", 22, "CNTT", 7.8),
        Student("SV052", "Pham Thi Lan", 20, "QTKD", 9.2),
        Student("SV053", "Hoang Van Hung", 23, "CNTT", 6.4)
    )

    var choice: Int
    do {
        showMenu()
        print("Choose: ")
        choice = scanner.nextInt()
        scanner.nextLine()

        when (choice) {
            1 -> addStudent(studentList, scanner)
            2 -> displayStudents(studentList)
            3 -> searchStudent(studentList, scanner)
            4 -> calculateAverageGpa(studentList)
            5 -> findHighestGpa(studentList)
            6 -> removeStudent(studentList, scanner)
            7 -> advancedFeatures(studentList, scanner)
            0 -> println("Thoat chuong trinh!")
            else -> println("Lua chon khong hop le!")
        }
    } while (choice != 0)
}

fun showMenu() {
    println("\n========== STUDENT MANAGEMENT ==========")
    println("1. Add student")
    println("2. Display all students")
    println("3. Search student")
    println("4. Calculate average GPA")
    println("5. Find student with highest GPA")
    println("6. Remove student")
    println("7. Thuc hien cac yeu cau nang cao (Thong ke & Sap xep)")
    println("0. Exit")
    println("========================================")
}

fun addStudent(list: MutableList<Student>, scanner: Scanner) {
    print("Nhap ID: "); val id = scanner.nextLine()
    print("Nhap Ho ten: "); val name = scanner.nextLine()
    print("Nhap Tuoi: "); val age = scanner.nextInt(); scanner.nextLine()
    print("Nhap Nganh: "); val major = scanner.nextLine()
    print("Nhap GPA: "); val gpa = scanner.nextDouble(); scanner.nextLine()
    list.add(Student(id, name, age, major, gpa))
    println("-> Da them thanh cong!")
}

fun displayStudents(list: List<Student>) {
    println("\n--- DANH SACH SINH VIEN ---")
    list.forEach { println(it) }
}

fun searchStudent(list: List<Student>, scanner: Scanner) {
    print("Nhap ten can tim: ")
    val keyword = scanner.nextLine()
    list.filter { it.fullName.contains(keyword, true) }.forEach { println(it) }
}

fun calculateAverageGpa(list: List<Student>) {
    if (list.isNotEmpty()) {
        println("-> GPA trung binh: ${list.map { it.gpa }.average()}")
    } else println("Danh sach trong!")
}

fun findHighestGpa(list: List<Student>) {
    val best = list.maxByOrNull { it.gpa }
    println("-> Sinh vien co GPA cao nhat: $best")
}

fun removeStudent(list: MutableList<Student>, scanner: Scanner) {
    print("Nhap ID sinh vien can xoa: ")
    val id = scanner.nextLine()
    val removed = list.removeIf { it.studentId.equals(id, true) }
    if (removed) println("-> Da xoa thanh cong!") else println("-> Khong tim thay ID.")
}

fun advancedFeatures(list: List<Student>, scanner: Scanner) {
    println("\n--- CAC YEU CAU NANG CAO ---")
    println("1. So SV co GPA >= 8.0: ${list.count { it.gpa >= 8.0 }}")
    println("2. So SV co GPA < 5.0: ${list.count { it.gpa < 5.0 }}")

    print("3. Nhap ten nganh can tinh GPA TB: ")
    val majorName = scanner.nextLine()
    val mList = list.filter { it.major.equals(majorName, true) }
    if (mList.isNotEmpty()) println("   GPA TB nganh $majorName: ${mList.map { it.gpa }.average()}")
    else println("   Khong co sinh vien nganh nay.")

    println("4. Sinh vien lon tuoi nhat: ${list.maxByOrNull { it.age }}")

    println("5. SV co GPA tu 7.0 den 8.5:")
    list.filter { it.gpa in 7.0..8.5 }.forEach { println("   $it") }

    print("6. Nhap nganh can tim kiem danh sach: ")
    val sMajor = scanner.nextLine()
    list.filter { it.major.equals(sMajor, true) }.forEach { println("   $it") }

    println("7. Top 3 sinh vien GPA cao nhat:")
    list.sortedByDescending { it.gpa }.take(3).forEach { println("   $it") }

    println("8. Sap xep theo tuoi tang dan:")
    list.sortedBy { it.age }.forEach { println("   $it") }

    println("9. Sap xep theo ten (Alphabet):")
    list.sortedBy { it.fullName }.forEach { println("   $it") }
}