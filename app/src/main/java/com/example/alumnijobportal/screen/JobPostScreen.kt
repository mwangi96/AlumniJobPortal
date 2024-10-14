package com.example.alumnijobportal.screen

import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.alumnijobportal.utils.SaveJobToFirebase

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun JobPostScreen(navController: NavHostController) {
    val saveJobToFirebase = SaveJobToFirebase()

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

    var skillInput by remember { mutableStateOf("") }
    var skills by remember { mutableStateOf(mutableListOf<String>()) }
    var screeningQuestions by remember { mutableStateOf(mutableListOf<String>()) }
    var questionInput by remember { mutableStateOf("") }

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Post a Job") })
        },
        content = { paddingValues ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // OutlinedTextFields for job details
                OutlinedTextField(
                    value = jobTitle,
                    onValueChange = { jobTitle = it },
                    label = { Text("Job Title") },
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = companyName,
                    onValueChange = { companyName = it },
                    label = { Text("Company Name") },
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = workplaceType,
                    onValueChange = { workplaceType = it },
                    label = { Text("Workplace Type") },
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = employmentType,
                    onValueChange = { employmentType = it },
                    label = { Text("Employment Type") },
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = currency,
                    onValueChange = { currency = it },
                    label = { Text("Currency") },
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = salaryType,
                    onValueChange = { salaryType = it },
                    label = { Text("Salary Type") },
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = minSalary,
                    onValueChange = { minSalary = it },
                    label = { Text("Min Salary") },
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = maxSalary,
                    onValueChange = { maxSalary = it },
                    label = { Text("Max Salary") },
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = location,
                    onValueChange = { location = it },
                    label = { Text("Location") },
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = jobDescription,
                    onValueChange = { jobDescription = it },
                    label = { Text("Job Description") },
                    maxLines = 5,
                    modifier = Modifier.fillMaxWidth()
                )

                // Skills Section
                Text("Add Skills")
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    OutlinedTextField(
                        value = skillInput,
                        onValueChange = { skillInput = it },
                        label = { Text("Search/Add Skill") },
                        modifier = Modifier.weight(1f)
                    )
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
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    OutlinedTextField(
                        value = questionInput,
                        onValueChange = { questionInput = it },
                        label = { Text("Screening Question") },
                        modifier = Modifier.weight(1f)
                    )
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

                Spacer(modifier = Modifier.height(16.dp))

                // Preview and Post Job Buttons
                Row(
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Button(onClick = { /* Preview logic here */ }) {
                        Text("Preview")
                    }
                    Button(onClick = {
                        saveJobToFirebase.saveJobToFirebase(
                            jobTitle, companyName, workplaceType, employmentType, currency,
                            salaryType, minSalary, maxSalary, location, jobDescription,
                            skills, screeningQuestions,
                            onSuccess = {
                                navController.navigate("posted job")
                            },
                            onFailure = { e ->
                                Log.e("JobPostScreen", "Error posting job: ${e.message}")
                            }
                        )
                    }) {
                        Text("Post Job")
                    }
                }
            }
        }
    )
}
