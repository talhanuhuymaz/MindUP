package com.yeditepe.mindup.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.yeditepe.mindup.viewmodel.AuthViewModel
import com.google.firebase.auth.FirebaseAuth
import com.yeditepe.mindup.components.PageHeader

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsPage(
    modifier: Modifier = Modifier,
    navController: NavController,
    authViewModel: AuthViewModel,
    onMenuClick: () -> Unit
) {
    var showDeleteDialog by remember { mutableStateOf(false) }
    var showChangeEmailDialog by remember { mutableStateOf(false) }
    var showChangePasswordDialog by remember { mutableStateOf(false) }
    var deleteReason by remember { mutableStateOf("") }
    var newEmail by remember { mutableStateOf("") }
    var newPassword by remember { mutableStateOf("") }
    var currentPassword by remember { mutableStateOf("") }
    var confirmNewEmail by remember { mutableStateOf("") }
    var confirmNewPassword by remember { mutableStateOf("") }
    var emailChangePassword by remember { mutableStateOf("") }

    val currentUser = authViewModel.getCurrentUser()
    val auth = authViewModel.getAuth()

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.Start
    ) {
        PageHeader(
            title = "Settings",
            onMenuClick = onMenuClick
        )

        // Email ve Şifre Bilgileri
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFFF5F5F5)),
            shape = RoundedCornerShape(12.dp)
        ) {
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                Text(
                    text = "Email",
                    fontSize = 14.sp,
                    color = Color.Gray
                )
                Text(
                    text = currentUser?.email ?: "Not signed in",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium
                )
            }
        }

        // Butonlar
        OutlinedButton(
            onClick = { showChangeEmailDialog = true },
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            shape = RoundedCornerShape(8.dp)
        ) {
            Icon(
                Icons.Default.Email,
                contentDescription = null,
                modifier = Modifier.padding(end = 8.dp)
            )
            Text("Change Email")
        }

        OutlinedButton(
            onClick = { showChangePasswordDialog = true },
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            shape = RoundedCornerShape(8.dp)
        ) {
            Icon(
                Icons.Default.Lock,
                contentDescription = null,
                modifier = Modifier.padding(end = 8.dp)
            )
            Text("Change Password")
        }

        OutlinedButton(
            onClick = { /* About Logic */ },
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            shape = RoundedCornerShape(8.dp)
        ) {
            Icon(
                Icons.Default.Info,
                contentDescription = null,
                modifier = Modifier.padding(end = 8.dp)
            )
            Text("About")
        }

        Spacer(modifier = Modifier.weight(1f))

        // Delete Account Button
        Button(
            onClick = { showDeleteDialog = true },
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(containerColor = Color.Red),
            shape = RoundedCornerShape(8.dp)
        ) {
            Icon(
                Icons.Default.Delete,
                contentDescription = null,
                modifier = Modifier.padding(end = 8.dp)
            )
            Text("Delete Account")
        }

        // Change Email Dialog
        if (showChangeEmailDialog) {
            AlertDialog(
                onDismissRequest = { showChangeEmailDialog = false },
                title = { Text("Change Email") },
                text = {
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        OutlinedTextField(
                            value = currentUser?.email ?: "",
                            onValueChange = { },
                            label = { Text("Current Email") },
                            enabled = false,
                            modifier = Modifier.fillMaxWidth()
                        )
                        OutlinedTextField(
                            value = emailChangePassword,
                            onValueChange = { emailChangePassword = it },
                            label = { Text("Current Password") },
                            singleLine = true,
                            visualTransformation = PasswordVisualTransformation(),
                            modifier = Modifier.fillMaxWidth()
                        )
                        OutlinedTextField(
                            value = newEmail,
                            onValueChange = { newEmail = it },
                            label = { Text("New Email") },
                            singleLine = true,
                            modifier = Modifier.fillMaxWidth()
                        )
                        OutlinedTextField(
                            value = confirmNewEmail,
                            onValueChange = { confirmNewEmail = it },
                            label = { Text("Confirm New Email") },
                            singleLine = true,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                },
                confirmButton = {
                    TextButton(
                        onClick = {
                            if (newEmail == confirmNewEmail) {
                                auth.signInWithEmailAndPassword(
                                    currentUser?.email ?: "",
                                    emailChangePassword
                                ).addOnCompleteListener { task ->
                                    if (task.isSuccessful) {
                                        authViewModel.updateEmail(newEmail) { success ->
                                            if (success) {
                                                showChangeEmailDialog = false
                                                newEmail = ""
                                                confirmNewEmail = ""
                                                emailChangePassword = ""
                                            }
                                        }
                                    }
                                }
                            }
                        },
                        enabled = newEmail.isNotEmpty() &&
                                confirmNewEmail.isNotEmpty() &&
                                newEmail == confirmNewEmail &&
                                emailChangePassword.isNotEmpty()
                    ) {
                        Text("Update")
                    }
                },
                dismissButton = {
                    TextButton(onClick = {
                        showChangeEmailDialog = false
                        newEmail = ""
                        confirmNewEmail = ""
                        emailChangePassword = ""
                    }) {
                        Text("Cancel")
                    }
                }
            )
        }

        // Change Password Dialog
        if (showChangePasswordDialog) {
            AlertDialog(
                onDismissRequest = { showChangePasswordDialog = false },
                title = { Text("Change Password") },
                text = {
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        OutlinedTextField(
                            value = currentPassword,
                            onValueChange = { currentPassword = it },
                            label = { Text("Current Password") },
                            singleLine = true,
                            visualTransformation = PasswordVisualTransformation(),
                            modifier = Modifier.fillMaxWidth()
                        )
                        OutlinedTextField(
                            value = newPassword,
                            onValueChange = { newPassword = it },
                            label = { Text("New Password") },
                            singleLine = true,
                            visualTransformation = PasswordVisualTransformation(),
                            modifier = Modifier.fillMaxWidth()
                        )
                        OutlinedTextField(
                            value = confirmNewPassword,
                            onValueChange = { confirmNewPassword = it },
                            label = { Text("Confirm New Password") },
                            singleLine = true,
                            visualTransformation = PasswordVisualTransformation(),
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                },
                confirmButton = {
                    TextButton(
                        onClick = {
                            if (newPassword == confirmNewPassword) {
                                auth.signInWithEmailAndPassword(
                                    currentUser?.email ?: "",
                                    currentPassword
                                ).addOnCompleteListener { task ->
                                    if (task.isSuccessful) {
                                        authViewModel.updatePassword(newPassword) { success ->
                                            if (success) {
                                                showChangePasswordDialog = false
                                                currentPassword = ""
                                                newPassword = ""
                                                confirmNewPassword = ""
                                            }
                                        }
                                    }
                                }
                            }
                        },
                        enabled = currentPassword.isNotEmpty() &&
                                newPassword.isNotEmpty() &&
                                confirmNewPassword.isNotEmpty() &&
                                newPassword == confirmNewPassword
                    ) {
                        Text("Update")
                    }
                },
                dismissButton = {
                    TextButton(onClick = {
                        showChangePasswordDialog = false
                        currentPassword = ""
                        newPassword = ""
                        confirmNewPassword = ""
                    }) {
                        Text("Cancel")
                    }
                }
            )
        }

        // Delete Account Dialog
        if (showDeleteDialog) {
            AlertDialog(
                onDismissRequest = { showDeleteDialog = false },
                title = { Text("Delete Account") },
                text = {
                    Column {
                        Text(
                            "Are you sure you want to delete your account? Let us know why:",
                            modifier = Modifier.padding(bottom = 16.dp)
                        )
                        OutlinedTextField(
                            value = deleteReason,
                            onValueChange = { deleteReason = it },
                            placeholder = { Text("Reason for deletion") },
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                },
                confirmButton = {
                    TextButton(
                        onClick = {
                            authViewModel.deleteAccount { success ->
                                if (success) {
                                    showDeleteDialog = false
                                    navController.navigate("login")
                                }
                            }
                        }
                    ) {
                        Text("Delete My Account", color = Color.Red)
                    }
                },
                dismissButton = {
                    TextButton(onClick = { showDeleteDialog = false }) {
                        Text("Cancel")
                    }
                }
            )
        }
    }
}