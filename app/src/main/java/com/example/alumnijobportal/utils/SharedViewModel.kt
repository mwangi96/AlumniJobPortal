import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel

class SharedViewModel : ViewModel() {
    // User information states
    var userEmail = mutableStateOf<String?>(null) // Observes user email
        private set

    var userRole = mutableStateOf<String?>(null) // Observes user role
        private set

    var userName = mutableStateOf<String?>(null) // Observes user name
        private set

    var selectedJobId = mutableStateOf<String?>(null) // Observes selected job ID
        private set

    // Methods to set individual properties
    fun setUserEmail(email: String) {
        userEmail.value = email // Update the user email state
    }

    fun setUserRole(role: String) {
        userRole.value = role // Update the user role state
    }

    fun setUserName(name: String) {
        userName.value = name // Update the user name state
    }

    fun setSelectedJobId(jobId: String) {
        selectedJobId.value = jobId // Update the selected job ID state
    }

    // Method to set user information all at once
    fun setUserInfo(name: String, email: String, role: String) {
        userName.value = name
        userEmail.value = email
        userRole.value = role
    }
}
