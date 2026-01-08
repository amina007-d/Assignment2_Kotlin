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
    val students = mutableListOf<Student>()
    var nextId = 1

    while (true) {
        printMenu()
        when (readln().trim()) {
            "1" -> nextId = addStudent(students, nextId)
            "2" -> listStudents(students)
            "3" -> findStudents(students)
            "4" -> editStudent(students)
            "5" -> deleteStudent(students)
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
    students: MutableList<Student>,
    nextId: Int
): Int {
    print("Enter name: ")
    val name = readln().trim()

    print("Enter age: ")
    val ageInput = readln().trim()
    val age = ageInput.toIntOrNull()

    if (age == null || age <= 0) {
        println("Invalid age")
        return nextId
    }

    val courses = mutableListOf<Course>()
    print("Add course name (or empty to skip): ")
    val courseName = readln().trim()
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

fun findStudents(students: List<Student>) {
    print("Search name: ")
    val query = readln().trim().lowercase()

    val results = students.filter {
        it.name.lowercase().contains(query)
    }

    if (results.isEmpty()) {
        println("No matches")
    } else {
        results.forEach { println(it) }
    }
}

fun editStudent(students: MutableList<Student>) {
    print("Enter student ID: ")
    val id = readln().toIntOrNull() ?: return

    val student = students.find { it.id == id }
    student?.let {
        print("New course: ")
        it.name = readln().trim()
        print("New age: ")
        it.age = readln().toIntOrNull() ?: it.age
        println("Updated")
    } ?: println("Student not found")
}

fun deleteStudent(students: MutableList<Student>) {
    print("Enter student ID to delete: ")
    val id = readln().toIntOrNull() ?: return

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
