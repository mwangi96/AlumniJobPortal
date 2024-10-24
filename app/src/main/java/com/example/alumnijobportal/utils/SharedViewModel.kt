import android.content.Context
import android.util.Log
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.alumnijobportal.screen.ApplicationData
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.FirebaseFirestore

class SharedViewModel : ViewModel() {

    // Firebase Realtime Database and Firestore instances
    private val db = FirebaseFirestore.getInstance()
    private val realtimeDb = FirebaseDatabase.getInstance().reference

    // User information states
    var userEmail = mutableStateOf<String?>(null)
        private set
    var userRole = mutableStateOf<String?>(null)
        private set
    var userName = mutableStateOf<String?>(null)
        private set
    var selectedJobId = mutableStateOf<String?>(null)
        private set

    private val appliedJobs = mutableStateListOf<Pair<String, String>>() // Caching applied jobs

    private val _toastMessage = MutableLiveData<String>()
    val toastMessage: LiveData<String> = _toastMessage

    // LiveData to hold fetched applications
    private val _applications = MutableLiveData<List<ApplicationData>>()
    val applications: LiveData<List<ApplicationData>> = _applications

    // Add this method to set user information
    fun setUserInfo(name: String, email: String, role: String) {
        setUserName(name)
        setUserEmail(email)
        setUserRole(role)
    }

    fun setUserEmail(email: String) {
        userEmail.value = email
    }

    fun setUserRole(role: String) {
        userRole.value = role
    }

    fun setUserName(name: String) {
        userName.value = name
    }

    fun setSelectedJobId(jobId: String) {
        selectedJobId.value = jobId
    }

    // Check if the user has applied for a specific job by querying Firestore
    fun hasUserAppliedForJob(jobId: String, userEmail: String, onResult: (Boolean) -> Unit) {
        db.collection("applications")
            .whereEqualTo("jobId", jobId)
            .whereEqualTo("userEmail", userEmail)
            .get()
            .addOnSuccessListener { documents ->
                onResult(!documents.isEmpty) // Return true if documents exist
            }
            .addOnFailureListener { exception ->
                Log.e("SharedViewModel", "Error checking if user has applied", exception)
                onResult(false) // Default to false if there’s an error
            }
    }

    // Mark a job as applied locally (optional, for caching)
    fun markAsApplied(jobId: String, userEmail: String) {
        appliedJobs.add(jobId to userEmail)
    }

    // Apply for a job
    fun applyForJob(
        jobId: String,
        firstName: String,
        lastName: String,
        email: String,
        phoneNumber: String,
        location: String,
        context: Context // Pass the context from the Composable
    ) {
        hasUserAppliedForJob(jobId, email) { hasApplied ->
            if (hasApplied) {
                Log.w("SharedViewModel", "User has already applied for job ID: $jobId")
                return@hasUserAppliedForJob
            }

            saveApplication(
                jobId = jobId,
                firstName = firstName,
                lastName = lastName,
                email = email,
                phoneNumber = phoneNumber,
                location = location
            )
        }
    }

    // Save application to Firestore
    private fun saveApplication(
        jobId: String,
        firstName: String,
        lastName: String,
        email: String,
        phoneNumber: String,
        location: String
    ) {
        val applicationData = hashMapOf(
            "jobId" to jobId,
            "firstName" to firstName,
            "lastName" to lastName,
            "userEmail" to email,
            "phoneNumber" to phoneNumber,
            "location" to location,
            "status" to "Application Sent"
        )

        db.collection("applications")
            .add(applicationData)
            .addOnSuccessListener {
                markAsApplied(jobId, email) // Mark the job as applied locally
            }
            .addOnFailureListener { e ->
                Log.e("SharedViewModel", "Error saving application", e)
            }
    }

    // Fetch applications for a specific user from Firestore and job details from Realtime Database
    fun fetchApplications(userEmail: String) {
        db.collection("applications")
            .whereEqualTo("userEmail", userEmail)
            .get()
            .addOnSuccessListener { documents ->
                val apps = mutableListOf<ApplicationData>()
                for (doc in documents) {
                    val jobId = doc.getString("jobId") ?: ""
                    val status = doc.getString("status") ?: "Application Sent"

                    // Fetch job details from Realtime Database
                    fetchJobDetails(jobId) { jobTitle, companyName ->
                        val applicationData = ApplicationData(
                            jobId = jobId,
                            jobTitle = jobTitle,
                            companyName = companyName,
                            status = status
                        )
                        apps.add(applicationData)
                        _applications.value = apps
                    }
                }
            }
            .addOnFailureListener { e ->
                Log.e("SharedViewModel", "Error fetching applications", e)
            }
    }

    // Fetch job details from Realtime Database based on jobId
    private fun fetchJobDetails(jobId: String, onResult: (String, String) -> Unit) {
        realtimeDb.child("jobs").child(jobId).get()
            .addOnSuccessListener { dataSnapshot ->
                val jobTitle = dataSnapshot.child("jobTitle").getValue(String::class.java) ?: ""
                val companyName = dataSnapshot.child("companyName").getValue(String::class.java) ?: ""
                onResult(jobTitle, companyName)
            }
            .addOnFailureListener { e ->
                Log.e("SharedViewModel", "Error fetching job details", e)
                onResult("", "")
            }
    }
}