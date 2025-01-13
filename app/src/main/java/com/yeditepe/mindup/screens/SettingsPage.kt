package com.yeditepe.mindup.screens

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
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
    val context = LocalContext.current
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
    var showAboutDialog by remember { mutableStateOf(false) }

    val currentUser = authViewModel.getCurrentUser()
    val auth = authViewModel.getAuth()

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFFF5F5F5))
            .padding(16.dp),
        horizontalAlignment = Alignment.Start
    ) {
        PageHeader(
            title = "Settings",
            onMenuClick = onMenuClick
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Account Section
        SettingsSection(title = "Account") {
            SettingsItem(
                icon = Icons.Default.Person,
                title = "Email",
                subtitle = currentUser?.email ?: "Not signed in",
                showDivider = true
            )
            SettingsItem(
                icon = Icons.Default.Email,
                title = "Change Email",
                onClick = { showChangeEmailDialog = true },
                showDivider = true
            )
            SettingsItem(
                icon = Icons.Default.Lock,
                title = "Change Password",
                onClick = { showChangePasswordDialog = true }
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Support Section
        SettingsSection(title = "Support") {
            SettingsItem(
                icon = Icons.Default.Email,
                title = "Contact Us",
                subtitle = "mindup@contact.com"
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // About Section
        SettingsSection(title = "About") {
            SettingsItem(
                icon = Icons.Default.Info,
                title = "About MindUP",
                onClick = { showAboutDialog = true }
            )
        }

        Spacer(modifier = Modifier.weight(1f))

        // Delete Account Button
        TextButton(
            onClick = { showDeleteDialog = true },
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text(
                "Delete Account",
                color = Color.Red,
                fontWeight = FontWeight.Medium
            )
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

        // About Dialog
        if (showAboutDialog) {
            AlertDialog(
                onDismissRequest = { showAboutDialog = false },
                title = { Text("About MindUP") },
                text = {
                    Text(
                        "MindUp - Daily Mind App seeks to assist users in observing and improving " +
                        "their mental health through features like daily mood tracking and mindfulness " +
                        "activities.",
                        lineHeight = 24.sp
                    )
                },
                confirmButton = {
                    TextButton(onClick = { showAboutDialog = false }) {
                        Text("Close")
                    }
                }
            )
        }
    }
}

@Composable
private fun SettingsSection(
    title: String,
    content: @Composable () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(
            text = title,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
            color = MaterialTheme.colorScheme.primary,
            fontWeight = FontWeight.Medium
        )
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            colors = CardDefaults.cardColors(
                containerColor = Color.White
            ),
            shape = RoundedCornerShape(12.dp)
        ) {
            content()
        }
    }
}

@Composable
private fun SettingsItem(
    icon: ImageVector,
    title: String,
    subtitle: String? = null,
    showDivider: Boolean = false,
    onClick: (() -> Unit)? = null
) {
    val modifier = if (onClick != null) {
        Modifier.clickable(onClick = onClick)
    } else {
        Modifier
    }

    Column(
        modifier = modifier
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(24.dp)
            )
            Spacer(modifier = Modifier.width(16.dp))
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = title,
                    fontSize = 16.sp,
                    color = Color.Black
                )
                if (subtitle != null) {
                    Text(
                        text = subtitle,
                        fontSize = 14.sp,
                        color = Color.Gray
                    )
                }
            }
            if (onClick != null) {
                Icon(
                    imageVector = Icons.Default.ChevronRight,
                    contentDescription = null,
                    tint = Color.Gray
                )
            }
        }
        if (showDivider) {
            Divider(
                modifier = Modifier.padding(start = 56.dp),
                color = Color(0xFFE0E0E0)
            )
        }
    }
}