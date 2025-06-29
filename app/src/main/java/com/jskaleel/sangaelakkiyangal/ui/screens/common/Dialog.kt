package com.jskaleel.sangaelakkiyangal.ui.screens.common

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable

@Composable
fun OpenReaderDialog(
    onDismiss: () -> Unit,
    onInternal: () -> Unit,
    onExternal: () -> Unit
) {
    AlertDialog(
        containerColor = MaterialTheme.colorScheme.background,
        onDismissRequest = onDismiss,
        title = { Text("Open PDF") },
        text = { Text("Choose how you'd like to open this book") },
        confirmButton = {
            TextButton(onClick = { onInternal() }) {
                Text("In-App Reader")
            }
        },
        dismissButton = {
            TextButton(onClick = { onExternal() }) {
                Text("External Reader")
            }
        }
    )
}
