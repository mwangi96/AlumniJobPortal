package com.example.alumnijobportal.screen

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController

@Composable
fun ChatScreen(navController: NavHostController) {
    // State for messages and input
    var messages by remember { mutableStateOf(listOf<String>()) }
    var messageInput by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text("Chats", style = MaterialTheme.typography.titleLarge)

        // Display messages or a placeholder
        if (messages.isEmpty()) {
            EmptyChatPlaceholder()
        } else {
            MessageList(messages)
        }

        // Input field for new messages
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            TextField(
                value = messageInput,
                onValueChange = { messageInput = it },
                label = { Text("Type a message") },
                modifier = Modifier.weight(1f)
            )
            Button(
                onClick = {
                    if (messageInput.isNotEmpty()) {
                        messages = messages + messageInput // Add new message
                        messageInput = "" // Clear input
                    }
                }
            ) {
                Text("Send")
            }
        }
    }
}

@Composable
fun EmptyChatPlaceholder() {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text("No Chats Yet!", style = MaterialTheme.typography.titleMedium)
        Text("Post your first free job and start scheduling interviews with instant chats.")
    }
}

@Composable
fun MessageList(messages: List<String>) {
    Column(
        modifier = Modifier
            .fillMaxHeight(),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        messages.forEach { message ->
            Text(message)
        }
    }
}
