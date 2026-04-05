package ug.ac.ndejje.studentregistration

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.TextField
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.tooling.preview.Preview
import ug.ac.ndejje.studentregistration.ui.theme.StudentRegistrationTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            StudentRegistrationTheme {
                StudentDirectory()
            }
        }
    }
}

@Composable
fun StudentInfo(student: Student, attendance: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Image(
            painter = painterResource(student.profileImageId),
            contentDescription = "Profile Image",
            modifier = Modifier
                .size(120.dp)
                .clip(RoundedCornerShape(percent = 50))
                .padding(bottom = 8.dp),
            contentScale = ContentScale.Crop
        )

        Text(
            text = student.name,
            style = MaterialTheme.typography.headlineSmall
        )

        Text(
            text = student.regNumber,
            color = Color.Black,
            fontWeight = FontWeight.Bold
        )

        if (attendance.isNotEmpty()) {
            Text(
                text = attendance,
                color = if (attendance == "Present") Color(0xFF4CAF50) else Color.Red,
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.padding(top = 8.dp)
            )
        }
    }
}

@Composable
fun StudentIdCard(student: Student) {
    var attendance by remember { mutableStateOf(student.attendance) }
    var showOptions by remember { mutableStateOf(false) }

    ElevatedCard(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        shape = RoundedCornerShape(size = 16.dp),
        colors = CardDefaults.elevatedCardColors(
            containerColor = MaterialTheme.colorScheme.primary
        )
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            StudentInfo(student, attendance)

            if (attendance.isEmpty()) {
                if (showOptions) {
                    Button(onClick = {
                        attendance = "Present"
                        student.attendance = "Present"
                        showOptions = false
                    }) {
                        Text("Present")
                    }
                }

                Button(onClick = {
                    showOptions = !showOptions
                }) {
                    Text("Mark Attendance")
                }

                if (showOptions) {
                    Button(onClick = {
                        attendance = "Absent"
                        student.attendance = "Absent"
                        showOptions = false
                    }) {
                        Text("Absent")
                    }
                }
            }
        }
    }
}

@Composable
fun StudentDirectory() {
    var querySearch by remember { mutableStateOf("") }
    val allStudents = StudentProvider.studentList
    val filteredStudent by remember(querySearch) {
        derivedStateOf {
            if (querySearch.isBlank()) {
                allStudents
            } else {
                allStudents.filter {
                    it.name.contains(querySearch, ignoreCase = true)
                }
            }
        }
    }
    LazyColumn(
        modifier = Modifier
            .fillMaxWidth(),
        contentPadding = PaddingValues(16.dp)
    ) {
        item {
            TextField(
                value = querySearch,
                onValueChange = { querySearch = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                placeholder = { Text("Search Student") },
                leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
                trailingIcon = { Icon(Icons.Default.Person, contentDescription = null) },
                shape = RoundedCornerShape(12.dp),
            )
        }

        items(filteredStudent) { student ->
            StudentIdCard(student = student)
        }
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    StudentRegistrationTheme {
    }
}
