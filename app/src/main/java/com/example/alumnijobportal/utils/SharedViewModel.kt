import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel

class SharedViewModel : ViewModel() {
    var userEmail = mutableStateOf<String?>(null) // Use nullable type
        private set

    // You might have methods to set the user email
    fun setUserEmail(email: String) {
        userEmail.value = email
    }
}
