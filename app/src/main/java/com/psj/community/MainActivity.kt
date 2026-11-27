
package com.psj.community

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.psj.community.data.auth.FirebaseAuthRepository
import com.psj.community.data.model.UserAccount
import com.psj.community.ui.theme.MyComposeApplicationTheme
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            MyComposeApplicationTheme {
                PSJApp()
            }
        }
    }
}

private val PSJBlue = Color(0xFF0756A5)
private val PSJLightBlue = Color(0xFFEAF3FC)
private val PSJDark = Color(0xFF172B4D)

@Composable
fun PSJApp() {
    var currentPage by remember { mutableStateOf(0) }

    var loggedInAccount by remember {
        mutableStateOf<UserAccount?>(null)
    }

    val authRepository = remember {
        FirebaseAuthRepository()
    }

    LaunchedEffect(Unit) {
        delay(2000)
        currentPage = 1
    }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = Color.White
    ) {
        when (currentPage) {
            0 -> SplashScreen()

            1 -> WelcomeScreen(
                onStart = { currentPage = 2 }
            )

            2 -> LoginScreen(
                authRepository = authRepository,
                onBack = { currentPage = 1 },
                onLoginSuccess = { account ->
                    loggedInAccount = account
                    currentPage = 3
                }
            )

            3 -> {
                val account = loggedInAccount

                if (account != null) {
                    LoginSuccessScreen(
                        account = account,
                        onLogout = {
                            authRepository.logout()
                            loggedInAccount = null
                            currentPage = 1
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun SplashScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(PSJBlue),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "PSJ",
            color = Color.White,
            fontSize = 54.sp,
            fontWeight = FontWeight.Bold
        )

        Text(
            text = "COMMUNITY",
            color = Color.White,
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(12.dp))

        Text(
            text = "Pencari Spxinstant se-Jabodetabek",
            color = Color.White,
            fontSize = 13.sp
        )

        Spacer(modifier = Modifier.height(30.dp))

        CircularProgressIndicator(
            color = Color.White
        )
    }
}

@Composable
fun WelcomeScreen(
    onStart: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(PSJLightBlue)
            .padding(28.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "PSJ",
            color = PSJBlue,
            fontSize = 52.sp,
            fontWeight = FontWeight.Bold
        )

        Text(
            text = "COMMUNITY",
            color = PSJDark,
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Solid • Profesional • Maju",
            color = PSJDark,
            fontSize = 15.sp
        )

        Spacer(modifier = Modifier.height(48.dp))

        Button(
            onClick = onStart,
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(
                containerColor = PSJBlue
            ),
            shape = RoundedCornerShape(12.dp)
        ) {
            Text(
                text = "MULAI",
                modifier = Modifier.padding(8.dp),
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Composable
fun LoginScreen(
    authRepository: FirebaseAuthRepository,
    onBack: () -> Unit,
    onLoginSuccess: (UserAccount) -> Unit
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }

    val scope = rememberCoroutineScope()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(PSJLightBlue)
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "PSJ COMMUNITY",
            color = PSJBlue,
            fontSize = 25.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "Masuk ke akun PSJ kamu",
            color = PSJDark,
            fontSize = 14.sp
        )

        Spacer(modifier = Modifier.height(28.dp))

        Surface(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(18.dp),
            color = Color.White,
            shadowElevation = 4.dp
        ) {
            Column(
                modifier = Modifier.padding(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "LOGIN AKUN",
                    color = PSJBlue,
                    fontSize = 19.sp,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(20.dp))

                OutlinedTextField(
                    value = email,
                    onValueChange = {
                        email = it
                        errorMessage = ""
                    },
                    modifier = Modifier.fillMaxWidth(),
                    label = { Text("Email") },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Email
                    )
                )

                Spacer(modifier = Modifier.height(12.dp))

                OutlinedTextField(
                    value = password,
                    onValueChange = {
                        password = it
                        errorMessage = ""
                    },
                    modifier = Modifier.fillMaxWidth(),
                    label = { Text("Password") },
                    singleLine = true,
                    visualTransformation =
                        PasswordVisualTransformation(),
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Password
                    )
                )

                Spacer(modifier = Modifier.height(16.dp))

                if (errorMessage.isNotBlank()) {
                    Text(
                        text = errorMessage,
                        color = Color.Red,
                        fontSize = 13.sp,
                        textAlign = TextAlign.Center
                    )

                    Spacer(modifier = Modifier.height(12.dp))
                }

                Button(
                    onClick = {
                        if (email.isBlank() || password.isBlank()) {
                            errorMessage =
                                "Email dan password wajib diisi."
                        } else {
                            scope.launch {
                                isLoading = true
                                errorMessage = ""

                                val result = authRepository.login(
                                    email = email.trim(),
                                    password = password
                                )

                                isLoading = false

                                result.fold(
                                    onSuccess = { account ->
                                        onLoginSuccess(account)
                                    },
                                    onFailure = { error ->
                                        errorMessage =
                                            error.message
                                                ?: "Login gagal. Periksa akun kamu."
                                    }
                                )
                            }
                        }
                    },
                    modifier = Modifier.fillMaxWidth(),
                    enabled = !isLoading,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = PSJBlue
                    ),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    if (isLoading) {
                        CircularProgressIndicator(
                            color = Color.White,
                            modifier = Modifier.height(22.dp),
                            strokeWidth = 2.dp
                        )
                    } else {
                        Text(
                            text = "MASUK",
                            modifier = Modifier.padding(6.dp),
                            fontWeight = FontWeight.Bold
                        )
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                TextButton(
                    onClick = onBack,
                    enabled = !isLoading
                ) {
                    Text(
                        text = "Kembali",
                        color = PSJBlue
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(
    account: UserAccount,
    onLogout: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(PSJLightBlue)
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "LOGIN BERHASIL",
            color = PSJBlue,
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(24.dp))

        Surface(
            modifier = Modifier.fillMaxWidth(),
            color = Color.White,
            shape = RoundedCornerShape(16.dp),
            shadowElevation = 4.dp
        ) {
            Column(
                modifier = Modifier.padding(20.dp)
            ) {
                Text(
                    text = "Selamat datang,",
                    color = PSJDark
                )

                Text(
                    text = account.displayName,
                    color = PSJBlue,
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(18.dp))

                Text("ID Anggota: ${account.memberId}")
                Spacer(modifier = Modifier.height(8.dp))

                Text("Email: ${account.email}")
                Spacer(modifier = Modifier.height(8.dp))

                Text("Jabatan: ${account.role}")
                Spacer(modifier = Modifier.height(8.dp))

                Text("Status: ${account.status}")
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = onLogout,
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(
                containerColor = PSJBlue
            )
        ) {
            Text("KELUAR AKUN")
        }
    }
}
