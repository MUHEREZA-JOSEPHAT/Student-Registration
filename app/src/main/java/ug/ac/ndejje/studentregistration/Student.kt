package ug.ac.ndejje.studentregistration

data class Student(
    val id: Int,
    val name: String,
    val regNumber: String,
    val programme: String,
    val profileImageId: Int, // Resource ID (e.g., R.drawable.female_student)
    var isVerified: Boolean = false

)
