import java.util.Scanner

interface Identifiable {
    val id: Int
}

data class Course(
    val name: String,
    val credits: Int
)

data class Student(
    override val id: Int,
    var name: String,
    var age: Int,
    val courses: MutableList<Course>
) : Identifiable

fun main() {
    val scanner = Scanner(System.`in`)
    val students = mutableListOf<Student>()
    var nextId = 1

    while (true) {
        printMenu()
        when (scanner.nextLine().trim()) {
            "1" -> nextId = addStudent(scanner, students, nextId)
            "2" -> listStudents(students)
            "3" -> findStudents(scanner, students)
            "4" -> editStudent(scanner, students)
            "5" -> deleteStudent(scanner, students)
            "6" -> showStatistics(students)
            "7" -> return
            else -> println("Invalid option")
        }
    }
}

fun printMenu() {
    println(
        """
        |Student Manager 
        |1. Add student
        |2. List students
        |3. Find student by name
        |4. Edit student
        |5. Delete student
        |6. Statistics
        |7. Exit
        """.trimMargin()
    )
}


fun addStudent(
    scanner: Scanner,
    students: MutableList<Student>,
    nextId: Int
): Int {
    print("Enter name: ")
    val name = scanner.nextLine().trim()

    print("Enter age: ")
    val age = scanner.nextLine().toIntOrNull() ?: return nextId.also {
        println("Invalid age")
    }

    val courses = mutableListOf<Course>()
    print("Add course name (or empty to skip): ")
    val courseName = scanner.nextLine().trim()
    if (courseName.isNotEmpty()) {
        courses.add(Course(courseName, 3))
    }

    students.add(Student(nextId, name, age, courses))
    println("Student added with ID $nextId")
    return nextId + 1
}

fun listStudents(students: List<Student>) {
    if (students.isEmpty()) {
        println("No students found")
        return
    }

    students.sortedBy { it.name }.forEach {
        println("ID:${it.id}, Name:${it.name}, Age:${it.age}, Courses:${it.courses.map { c -> c.name }}")
    }
}

fun findStudents(scanner: Scanner, students: List<Student>) {
    print("Search name: ")
    val query = scanner.nextLine().trim().lowercase()

    val results = students.filter {
        it.name.lowercase().contains(query)
    }

    if (results.isEmpty()) {
        println("No matches")
    } else {
        results.forEach { println(it) }
    }
}

fun editStudent(scanner: Scanner, students: MutableList<Student>) {
    print("Enter student ID: ")
    val id = scanner.nextLine().toIntOrNull() ?: return

    val student = students.find { it.id == id }
    student?.let {
        print("New name: ")
        it.name = scanner.nextLine().trim()
        print("New age: ")
        it.age = scanner.nextLine().toIntOrNull() ?: it.age
        println("Updated")
    } ?: println("Student not found")
}


fun deleteStudent(scanner: Scanner, students: MutableList<Student>) {
    print("Enter student ID to delete: ")
    val id = scanner.nextLine().toIntOrNull() ?: return

    val removed = students.removeIf { it.id == id }
    println(if (removed) "Student deleted" else "Student not found")
}

fun showStatistics(students: List<Student>) {
    if (students.isEmpty()) {
        println("No data")
        return
    }

    val averageAge = students.map { it.age }.average()
    println("Total students: ${students.size}")
    println("Average age: %.2f".format(averageAge))
}
