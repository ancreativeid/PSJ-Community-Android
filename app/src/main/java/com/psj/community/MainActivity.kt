package com.psj.community

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.firebase.auth.FirebaseAuth
import com.psj.community.data.auth.FirebaseAuthRepository
import com.psj.community.data.model.UserAccount
import com.psj.community.ui.theme.MyComposeApplicationTheme
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import androidx.compose.material3.OutlinedTextFieldDefaults

private val PSJBlue = Color(0xFF0756A5)
private val PSJLightBlue = Color(0xFFEAF3FC)
private val PSJDark = Color(0xFF172B4D)
private val PSJMuted = Color(0xFF52657D)

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

@Composable
fun PSJApp() {
   var currentPage by remember { mutableStateOf(0) }
    var loggedInAccount by remember { mutableStateOf<UserAccount?>(null) }
    val authRepository = remember { FirebaseAuthRepository() }
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        kotlinx.coroutines.delay(1800)
        currentPage = 1
    }

    Surface(Modifier.fillMaxSize(), color = PSJLightBlue) {
        when (currentPage) {
            0 -> SplashScreen()
            1 -> WelcomeScreen(onStart = { currentPage = 2 })
            2 -> LoginScreen(
                authRepository = authRepository,
                onBack = { currentPage = 1 },
                onLoginSuccess = {
                    loggedInAccount = it
                    currentPage = 3
                },
                onRegistration = {
                    Toast.makeText(
                        context,
                        "Formulir pendaftaran akan disiapkan pada tahap berikutnya.",
                        Toast.LENGTH_LONG
                    ).show()
                }
            )
            3 -> loggedInAccount?.let { account ->
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

@Composable
private fun PSJLogo(modifier: Modifier = Modifier) {
    Image(
        painter = painterResource(id = R.drawable.psj_logo),
        contentDescription = "Logo PSJ",
        modifier = modifier
    )
}

@Composable
fun SplashScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(PSJBlue)
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        PSJLogo(Modifier.widthIn(max = 260.dp).fillMaxWidth().height(130.dp))
        Spacer(Modifier.height(14.dp))
        Text(
            "PSJ COMMUNITY",
            color = Color.White,
            fontSize = 29.sp,
            fontWeight = FontWeight.ExtraBold
        )
        Spacer(Modifier.height(8.dp))
        Text(
            "Solid • Profesional • Maju",
            color = Color.White,
            fontSize = 15.sp,
            fontWeight = FontWeight.Medium
        )
        Spacer(Modifier.height(42.dp))
        CircularProgressIndicator(color = Color.White)
        Spacer(Modifier.height(14.dp))
        Text("Mohon Tunggu...", color = Color.White, fontSize = 14.sp)
    }
}

@Composable
fun WelcomeScreen(onStart: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(PSJLightBlue)
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 22.dp, vertical = 18.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        PSJLogo(Modifier.widthIn(max = 220.dp).fillMaxWidth().height(105.dp))
        Spacer(Modifier.height(8.dp))
        Text(
            "PSJ COMMUNITY",
            color = PSJBlue,
            fontSize = 25.sp,
            fontWeight = FontWeight.ExtraBold,
            textAlign = TextAlign.Center
        )
        Spacer(Modifier.height(18.dp))
        Text(
            "SELAMAT DATANG",
            color = PSJBlue,
            fontSize = 25.sp,
            fontWeight = FontWeight.ExtraBold,
            textAlign = TextAlign.Center
        )
        Spacer(Modifier.height(6.dp))
        Text(
            "PSJ - Pencari Spxinstant se-Jabodetabek",
            color = PSJDark,
            fontSize = 15.sp,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center
        )
        Spacer(Modifier.height(12.dp))
        Surface(
            color = PSJBlue,
            shape = RoundedCornerShape(24.dp)
        ) {
            Text(
                "(Version 2.0 - Anniversary 1st and New Year's 2027 Edition)",
                color = Color.White,
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 10.dp)
            )
        }
        Spacer(Modifier.height(18.dp))
        Text(
            "Terima kasih telah menjadi bagian dari perjalanan 1 tahun PSJ.\n\n" +
                "Aplikasi ini dibuat sebagai langkah awal digitalisasi komunitas " +
                "untuk memberikan kemudahan dalam pelayanan anggota, administrasi, " +
                "serta kegiatan komunitas.\n\n" +
                "Semoga aplikasi ini dapat memberikan manfaat bagi seluruh Keluarga " +
                "Besar PSJ - Pencari Spxinstant se-Jabodetabek.\n\n" +
                "Mari bersama-sama membangun komunitas yang semakin modern, tertib, " +
                "kompak, dan profesional.",
            color = PSJDark,
            fontSize = 14.sp,
            lineHeight = 21.sp,
            textAlign = TextAlign.Center
        )
        Spacer(Modifier.height(22.dp))
        Button(
            onClick = onStart,
            modifier = Modifier.fillMaxWidth().height(52.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = PSJBlue,
                contentColor = Color.White
            ),
            shape = RoundedCornerShape(14.dp)
        ) {
            Text("MULAI", fontWeight = FontWeight.Bold, fontSize = 17.sp)
        }
        Spacer(Modifier.height(18.dp))
        Text(
            "Dipersembahkan untuk Anniversary 1 Tahun\ndan menyambut Tahun Baru 2027\n" +
                "PSJ - Pencari Spxinstant se-Jabodetabek",
            color = PSJDark,
            fontSize = 12.sp,
            fontWeight = FontWeight.SemiBold,
            textAlign = TextAlign.Center,
            lineHeight = 18.sp
        )
        Spacer(Modifier.height(8.dp))
        Text(
            "© 2026 Seluruh Hak Cipta Dilindungi",
            color = PSJMuted,
            fontSize = 11.sp,
            textAlign = TextAlign.Center
        )
        Spacer(Modifier.height(12.dp))
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(
    authRepository: FirebaseAuthRepository,
    onBack: () -> Unit,
    onLoginSuccess: (UserAccount) -> Unit,
    onRegistration: () -> Unit
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var showPassword by remember { mutableStateOf(false) }
    var rememberMe by remember { mutableStateOf(true) }
    var errorMessage by remember { mutableStateOf("") }
    var infoMessage by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(PSJLightBlue)
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 20.dp, vertical = 18.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        PSJLogo(Modifier.widthIn(max = 190.dp).fillMaxWidth().height(90.dp))
        Spacer(Modifier.height(6.dp))
        Text(
            "PSJ COMMUNITY",
            color = PSJBlue,
            fontSize = 25.sp,
            fontWeight = FontWeight.ExtraBold
        )
        Spacer(Modifier.height(18.dp))

        Surface(
            modifier = Modifier.fillMaxWidth(),
            color = Color.White,
            shape = RoundedCornerShape(20.dp),
            shadowElevation = 5.dp
        ) {
            Column(
                modifier = Modifier.padding(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    "LOGIN ANGGOTA",
                    color = PSJBlue,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.ExtraBold
                )
                Spacer(Modifier.height(6.dp))
                Text(
                    "Masukan Email dan Password Anda untuk masuk ke Aplikasi",
                    color = PSJDark,
                    fontSize = 13.sp,
                    textAlign = TextAlign.Center
                )
                Spacer(Modifier.height(18.dp))

                OutlinedTextField(
                    value = email,
                    onValueChange = {
                        email = it
                        errorMessage = ""
                        infoMessage = ""
                    },
                    modifier = Modifier.fillMaxWidth(),
                    label = { Text("Email", color = PSJBlue) },
                    placeholder = { Text("Masukkan email terdaftar", color = PSJMuted) },
                    singleLine = true,
                    enabled = !isLoading,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                    colors = TextFieldDefaults.outlinedTextFieldColors(
                        focusedTextColor = PSJDark,
                        unfocusedTextColor = PSJDark,
                        focusedBorderColor = PSJBlue,
                        unfocusedBorderColor = PSJMuted,
                        cursorColor = PSJBlue
                    )
                )
                Spacer(Modifier.height(10.dp))

                OutlinedTextField(
                    value = "",
                    onValueChange = {},
                    modifier = Modifier.fillMaxWidth(),
                    readOnly = true,
                    label = { Text("ID PSJ", color = PSJBlue) },
                    placeholder = {
                        Text(
                            "Otomatis muncul setelah akun dikenali",
                            color = PSJMuted,
                            fontSize = 12.sp
                        )
                    },
                    singleLine = true,
                    colors = TextFieldDefaults.outlinedTextFieldColors(
                        disabledTextColor = PSJDark,
                        disabledBorderColor = PSJMuted,
                        disabledLabelColor = PSJBlue
                    ),
                    enabled = false
                )
                Spacer(Modifier.height(10.dp))

                OutlinedTextField(
                    value = password,
                    onValueChange = {
                        password = it
                        errorMessage = ""
                    },
                    modifier = Modifier.fillMaxWidth(),
                    label = { Text("Password", color = PSJBlue) },
                    singleLine = true,
                    enabled = !isLoading,
                    visualTransformation = if (showPassword) {
                        VisualTransformation.None
                    } else {
                        PasswordVisualTransformation()
                    },
                    trailingIcon = {
                        TextButton(onClick = { showPassword = !showPassword }) {
                            Text(
                                if (showPassword) "SEMBUNYIKAN" else "LIHAT",
                                color = PSJBlue,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                    colors = TextFieldDefaults.outlinedTextFieldColors(
                        focusedTextColor = PSJDark,
                        unfocusedTextColor = PSJDark,
                        focusedBorderColor = PSJBlue,
                        unfocusedBorderColor = PSJMuted,
                        cursorColor = PSJBlue
                    )
                )

                Spacer(Modifier.height(8.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Checkbox(
                            checked = rememberMe,
                            onCheckedChange = { rememberMe = it },
                            colors = CheckboxDefaults.colors(checkedColor = PSJBlue)
                        )
                        Text("Ingat saya", color = PSJDark, fontSize = 13.sp)
                    }
                    TextButton(
                        enabled = email.contains("@") && !isLoading,
                        onClick = {
                            scope.launch {
                                infoMessage = ""
                                errorMessage = ""
                                try {
                                    FirebaseAuth.getInstance()
                                        .sendPasswordResetEmail(email.trim())
                                        .await()
                                    infoMessage = "Link reset password telah dikirim ke email."
                                } catch (e: Exception) {
                                    errorMessage = e.localizedMessage
                                        ?: "Tidak dapat mengirim reset password."
                                }
                            }
                        }
                    ) {
                        Text("Lupa Password", color = PSJBlue, fontSize = 13.sp)
                    }
                }

                if (errorMessage.isNotBlank()) {
                    Text(
                        errorMessage,
                        color = Color(0xFFB3261E),
                        fontSize = 12.sp,
                        textAlign = TextAlign.Center
                    )
                    Spacer(Modifier.height(6.dp))
                }
                if (infoMessage.isNotBlank()) {
                    Text(
                        infoMessage,
                        color = Color(0xFF167044),
                        fontSize = 12.sp,
                        textAlign = TextAlign.Center
                    )
                    Spacer(Modifier.height(6.dp))
                }

                Button(
                    onClick = {
                        if (email.isBlank() || password.isBlank()) {
                            errorMessage = "Email dan password wajib diisi."
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
                                    onSuccess = { onLoginSuccess(it) },
                                    onFailure = {
                                        errorMessage = it.message
                                            ?: "Login gagal. Periksa email dan password."
                                    }
                                )
                            }
                        }
                    },
                    modifier = Modifier.fillMaxWidth().height(50.dp),
                    enabled = !isLoading,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = PSJBlue,
                        contentColor = Color.White
                    ),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    if (isLoading) {
                        CircularProgressIndicator(
                            color = Color.White,
                            modifier = Modifier.size(22.dp),
                            strokeWidth = 2.dp
                        )
                    } else {
                        Text("LOGIN", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                    }
                }

                Spacer(Modifier.height(8.dp))
                Text(
                    "Belum punya akun? Hubungi Pengurus",
                    color = PSJDark,
                    fontSize = 12.sp,
                    textAlign = TextAlign.Center
                )
                OutlinedButton(
                    onClick = onRegistration,
                    modifier = Modifier.fillMaxWidth().padding(top = 8.dp),
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = PSJBlue)
                ) {
                    Text("+  Formulir Pendaftaran Anggota", fontWeight = FontWeight.Bold)
                }
                TextButton(onClick = onBack, enabled = !isLoading) {
                    Text("Kembali", color = PSJBlue)
                }
            }
        }
    }
}

@Composable
fun LoginSuccessScreen(
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
        PSJLogo(Modifier.widthIn(max = 180.dp).fillMaxWidth().height(85.dp))
        Spacer(Modifier.height(12.dp))
        Text(
            "SELAMAT DATANG",
            color = PSJBlue,
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold
        )
        Spacer(Modifier.height(16.dp))
        Surface(
            modifier = Modifier.fillMaxWidth(),
            color = Color.White,
            shape = RoundedCornerShape(16.dp),
            shadowElevation = 4.dp
        ) {
            Column(
                modifier = Modifier.padding(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    account.displayName,
                    color = PSJDark,
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold
                )
                Spacer(Modifier.height(8.dp))
                Text("ID Anggota: ${account.memberId}", color = PSJDark)
                Text("Email: ${account.email}", color = PSJDark)
                Text("Jabatan: ${account.role}", color = PSJDark)
                Spacer(Modifier.height(8.dp))
                Text(
                    "Status: ${account.status}",
                    color = Color(0xFF167044),
                    fontWeight = FontWeight.Bold
                )
            }
        }
        Spacer(Modifier.height(24.dp))
        Button(
            onClick = onLogout,
            modifier = Modifier.fillMaxWidth().height(50.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = PSJBlue,
                contentColor = Color.White
            ),
            shape = RoundedCornerShape(12.dp)
        ) {
            Text("KELUAR AKUN", fontWeight = FontWeight.Bold)
        }
    }
}
