package com.example.alumnijobportal.screen

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

@Composable
fun JobsPostScreen(
    navController: NavController,
    onSubmit: (String, String, String, String, String, String, String, String, String, String, List<String>, List<String>) -> Unit
) {
    // Input fields state
    var jobTitle by remember { mutableStateOf("") }
    var companyName by remember { mutableStateOf("") }
    var workplaceType by remember { mutableStateOf("") }
    var employmentType by remember { mutableStateOf("") }
    var currency by remember { mutableStateOf("") }
    var salaryType by remember { mutableStateOf("") }
    var minSalary by remember { mutableStateOf("") }
    var maxSalary by remember { mutableStateOf("") }
    var location by remember { mutableStateOf("") }
    var jobDescription by remember { mutableStateOf("") }

    // State for skills and screening questions
    var skillInput by remember { mutableStateOf("") }
    var skills by remember { mutableStateOf(mutableListOf<String>()) }
    var screeningQuestions by remember { mutableStateOf(mutableListOf<String>()) }
    var questionInput by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        // Input fields for job details
        TextField(value = jobTitle, onValueChange = { jobTitle = it }, label = { Text("Job Title") })
        TextField(value = companyName, onValueChange = { companyName = it }, label = { Text("Company Name") })
        TextField(value = workplaceType, onValueChange = { workplaceType = it }, label = { Text("Workplace Type") })
        TextField(value = employmentType, onValueChange = { employmentType = it }, label = { Text("Employment Type") })
        TextField(value = currency, onValueChange = { currency = it }, label = { Text("Currency") })
        TextField(value = salaryType, onValueChange = { salaryType = it }, label = { Text("Salary Type") })
        TextField(value = minSalary, onValueChange = { minSalary = it }, label = { Text("Min Salary") })
        TextField(value = maxSalary, onValueChange = { maxSalary = it }, label = { Text("Max Salary") })
        TextField(value = location, onValueChange = { location = it }, label = { Text("Location") })
        TextField(value = jobDescription, onValueChange = { jobDescription = it }, label = { Text("Job Description") }, maxLines = 5)

        // Skills Section
        Text("Add Skills")
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp), verticalAlignment = Alignment.CenterVertically) {
            TextField(value = skillInput, onValueChange = { skillInput = it }, label = { Text("Search/Add Skill") }, modifier = Modifier.weight(1f))
            IconButton(onClick = {
                if (skillInput.isNotEmpty()) {
                    skills.add(skillInput)
                    skillInput = ""
                }
            }) {
                Icon(Icons.Default.Add, contentDescription = "Add Skill")
            }
        }
        skills.forEach { skill -> Text(skill) }

        // Screening Questions Section
        Text("Add Screening Questions")
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp), verticalAlignment = Alignment.CenterVertically) {
            TextField(value = questionInput, onValueChange = { questionInput = it }, label = { Text("Screening Question") }, modifier = Modifier.weight(1f))
            IconButton(onClick = {
                if (questionInput.isNotEmpty()) {
                    screeningQuestions.add(questionInput)
                    questionInput = ""
                }
            }) {
                Icon(Icons.Default.Add, contentDescription = "Add Screening Question")
            }
        }
        screeningQuestions.forEach { question -> Text(question) }

        // Preview and Post Job Buttons
        Row(horizontalArrangement = Arrangement.spacedBy(16.dp), modifier = Modifier.fillMaxWidth()) {
            Button(onClick = { /* Preview logic here */ }) { Text("Preview") }
            Button(onClick = {
                // Trigger the onSubmit lambda with the entered data
                onSubmit(
                    jobTitle, companyName, workplaceType, employmentType, currency,
                    salaryType, minSalary, maxSalary, location, jobDescription,
                    skills, screeningQuestions
                )
                // Navigate to another screen after posting the job
                navController.navigate("posted_jobs")
            }) {
                Text("Post Job")
            }
        }
    }
}
