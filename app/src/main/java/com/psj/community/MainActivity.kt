@file:OptIn(androidx.compose.material3.ExperimentalMaterial3Api::class)

package com.psj.community

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Brush
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

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = PSJLightBlue
    ) {
        when (currentPage) {
            0 -> SplashScreen()

            1 -> WelcomeScreen(
                onStart = { currentPage = 2 }
            )

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
                DashboardScreen(
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

// ======================================================
// SPLASH SCREEN
// ======================================================

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
        PSJLogo(
            Modifier
                .widthIn(max = 260.dp)
                .fillMaxWidth()
                .height(130.dp)
        )

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

        Text(
            "Mohon Tunggu...",
            color = Color.White,
            fontSize = 14.sp
        )
    }
}

// ======================================================
// WELCOME SCREEN
// ======================================================

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
        PSJLogo(
            Modifier
                .widthIn(max = 220.dp)
                .fillMaxWidth()
                .height(105.dp)
        )

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
                modifier = Modifier.padding(
                    horizontal = 16.dp,
                    vertical = 10.dp
                )
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
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = PSJBlue,
                contentColor = Color.White
            ),
            shape = RoundedCornerShape(14.dp)
        ) {
            Text(
                "MULAI",
                fontWeight = FontWeight.Bold,
                fontSize = 17.sp
            )
        }

        Spacer(Modifier.height(18.dp))

        Text(
            "Dipersembahkan untuk Anniversary 1 Tahun\n" +
                "dan menyambut Tahun Baru 2027\n" +
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

// ======================================================
// LOGIN SCREEN
// ======================================================

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
        PSJLogo(
            Modifier
                .widthIn(max = 190.dp)
                .fillMaxWidth()
                .height(90.dp)
        )

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
                    label = {
                        Text("Email", color = PSJBlue)
                    },
                    placeholder = {
                        Text(
                            "Masukkan email terdaftar",
                            color = PSJMuted
                        )
                    },
                    singleLine = true,
                    enabled = !isLoading,
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Email
                    ),
                    colors = TextFieldDefaults.outlinedTextFieldColors(
                        textColor = PSJDark,
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
                    label = {
                        Text("ID PSJ", color = PSJBlue)
                    },
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
                    label = {
                        Text("Password", color = PSJBlue)
                    },
                    singleLine = true,
                    enabled = !isLoading,
                    visualTransformation = if (showPassword) {
                        VisualTransformation.None
                    } else {
                        PasswordVisualTransformation()
                    },
                    trailingIcon = {
                        TextButton(
                            onClick = {
                                showPassword = !showPassword
                            }
                        ) {
                            Text(
                                if (showPassword) "SEMBUNYIKAN" else "LIHAT",
                                color = PSJBlue,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    },
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Password
                    ),
                    colors = TextFieldDefaults.outlinedTextFieldColors(
                        textColor = PSJDark,
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
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Checkbox(
                            checked = rememberMe,
                            onCheckedChange = {
                                rememberMe = it
                            },
                            colors = CheckboxDefaults.colors(
                                checkedColor = PSJBlue
                            )
                        )

                        Text(
                            "Ingat saya",
                            color = PSJDark,
                            fontSize = 13.sp
                        )
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

                                    infoMessage =
                                        "Link reset password telah dikirim ke email."
                                } catch (e: Exception) {
                                    errorMessage = e.localizedMessage
                                        ?: "Tidak dapat mengirim reset password."
                                }
                            }
                        }
                    ) {
                        Text(
                            "Lupa Password",
                            color = PSJBlue,
                            fontSize = 13.sp
                        )
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
                                    onSuccess = {
                                        onLoginSuccess(it)
                                    },
                                    onFailure = {
                                        errorMessage = it.message
                                            ?: "Login gagal. Periksa email dan password."
                                    }
                                )
                            }
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp),
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
                        Text(
                            "LOGIN",
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp
                        )
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
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 8.dp),
                    colors = ButtonDefaults.outlinedButtonColors(
                        contentColor = PSJBlue
                    )
                ) {
                    Text(
                        "+  Formulir Pendaftaran Anggota",
                        fontWeight = FontWeight.Bold
                    )
                }

                TextButton(
                    onClick = onBack,
                    enabled = !isLoading
                ) {
                    Text("Kembali", color = PSJBlue)
                }
            }
        }
    }
}

// ======================================================
// DASHBOARD + NAVIGATION DRAWER
// ======================================================

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashboardScreen(
    account: UserAccount,
    onLogout: () -> Unit
) {
    val context = LocalContext.current
    val drawerState = rememberDrawerState(
        initialValue = DrawerValue.Closed
    )
    val scope = rememberCoroutineScope()

    var selectedMenu by remember { mutableStateOf("Beranda") }
    var currentSlide by remember { mutableStateOf(0) }

    val slides = listOf(
        "DOKUMENTASI KOPDAR PSJ",
        "KEGIATAN KELUARGA BESAR PSJ",
        "MOMEN KEBERSAMAAN PSJ"
    )

    val slideScrollState = rememberScrollState()

    LaunchedEffect(slideScrollState.value) {
        currentSlide = ((slideScrollState.value + 151) / 302)
            .coerceIn(0, slides.lastIndex)
    }

    ModalNavigationDrawer(
        drawerState = drawerState,
        gesturesEnabled = true,
        drawerContent = {
            ModalDrawerSheet(
                modifier = Modifier.width(310.dp),
                drawerContainerColor = Color.White
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxHeight()
                        .verticalScroll(rememberScrollState())
                        .background(Color.White)
                ) {

                    // HEADER PROFIL DRAWER
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(
                                Brush.verticalGradient(
                                    listOf(
                                        Color(0xFF0B70C9),
                                        Color(0xFF063B78)
                                    )
                                )
                            )
                            .padding(
                                horizontal = 18.dp,
                                vertical = 24.dp
                            )
                    ) {
                        Column {
                            PSJLogo(
                                Modifier
                                    .size(72.dp)
                                    .background(
                                        Color.White,
                                        RoundedCornerShape(14.dp)
                                    )
                                    .padding(5.dp)
                            )

                            Spacer(Modifier.height(14.dp))

                            Text(
                                "KELUARGA BESAR PSJ",
                                color = Color.White,
                                fontSize = 17.sp,
                                fontWeight = FontWeight.ExtraBold
                            )

                            Spacer(Modifier.height(5.dp))

                            Text(
                                account.displayName,
                                color = Color.White,
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Bold
                            )

                            Text(
                                if (account.memberId.isBlank()) {
                                    "FOUNDER"
                                } else {
                                    account.memberId
                                },
                                color = Color.White,
                                fontSize = 12.sp
                            )

                            Spacer(Modifier.height(8.dp))

                            Surface(
                                color = Color.White.copy(alpha = 0.18f),
                                shape = RoundedCornerShape(20.dp)
                            ) {
                                Text(
                                    "AKUN PSJ",
                                    color = Color.White,
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier.padding(
                                        horizontal = 12.dp,
                                        vertical = 6.dp
                                    )
                                )
                            }
                        }
                    }

                    Spacer(Modifier.height(12.dp))

                    // MENU UTAMA
                    DrawerSectionTitle("MENU UTAMA")

                    DrawerMenuItem(
                        icon = "⌂",
                        title = "Dashboard",
                        selected = selectedMenu == "Beranda"
                    ) {
                        selectedMenu = "Beranda"
                        scope.launch { drawerState.close() }
                    }

                    DrawerMenuItem(
                        icon = "👥",
                        title = "Profil PSJ"
                    ) {
                        openDrawerMenu(
                            "Profil PSJ",
                            scope,
                            drawerState,
                            context
                        )
                    }

                   DrawerMenuItem(
    icon = "📋",
    title = "Data Anggota",
    selected = selectedMenu == "Data Anggota"
) {
    selectedMenu = "Data Anggota"

    scope.launch {
        drawerState.close()
    }
}

                    DrawerMenuItem(
                        icon = "📸",
                        title = "Dokumentasi"
                    ) {
                        openDrawerMenu(
                            "Dokumentasi",
                            scope,
                            drawerState,
                            context
                        )
                    }

                    DrawerMenuItem(
                        icon = "🚨",
                        title = "URC Darurat"
                    ) {
                        openDrawerMenu(
                            "URC Darurat",
                            scope,
                            drawerState,
                            context
                        )
                    }

                    DrawerDivider()

                    // LAYANAN DIGITAL
                    DrawerSectionTitle("LAYANAN DIGITAL")

                    DrawerMenuItem(
                        icon = "🪪",
                        title = "KTA Digital"
                    ) {
                        openDrawerMenu(
                            "KTA Digital",
                            scope,
                            drawerState,
                            context
                        )
                    }

                    DrawerMenuItem(
                        icon = "✓",
                        title = "Absensi Digital"
                    ) {
                        openDrawerMenu(
                            "Absensi Digital",
                            scope,
                            drawerState,
                            context
                        )
                    }

                    DrawerMenuItem(
                        icon = "💰",
                        title = "Kas Digital"
                    ) {
                        openDrawerMenu(
                            "Kas Digital",
                            scope,
                            drawerState,
                            context
                        )
                    }

                    DrawerMenuItem(
                        icon = "📄",
                        title = "Proposal Otomatis"
                    ) {
                        openDrawerMenu(
                            "Proposal Otomatis",
                            scope,
                            drawerState,
                            context
                        )
                    }

                    DrawerDivider()

                    // KEANGGOTAAN
                    DrawerSectionTitle("KEANGGOTAAN")

                    DrawerMenuItem(
                        icon = "➕",
                        title = "Pendaftaran Anggota"
                    ) {
                        openDrawerMenu(
                            "Pendaftaran Anggota",
                            scope,
                            drawerState,
                            context
                        )
                    }

                    DrawerMenuItem(
                        icon = "📤",
                        title = "Pengunduran Diri"
                    ) {
                        openDrawerMenu(
                            "Pengunduran Diri",
                            scope,
                            drawerState,
                            context
                        )
                    }

                    DrawerDivider()

                    // LAINNYA
                    DrawerSectionTitle("LAINNYA")

                    DrawerMenuItem(
                        icon = "ℹ",
                        title = "Tentang Aplikasi"
                    ) {
                        openDrawerMenu(
                            "Tentang Aplikasi",
                            scope,
                            drawerState,
                            context
                        )
                    }

                    Spacer(Modifier.height(12.dp))

                    DrawerDivider()

                    // KELUAR AKUN
                    DrawerMenuItem(
                        icon = "⇥",
                        title = "Keluar Akun",
                        iconColor = Color(0xFFC62828),
                        textColor = Color(0xFFC62828)
                    ) {
                        scope.launch {
                            drawerState.close()
                            onLogout()
                        }
                    }

                    Spacer(Modifier.height(20.dp))

                    Text(
                        "PSJ COMMUNITY V2.0",
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 18.dp),
                        color = PSJMuted,
                        fontSize = 10.sp,
                        textAlign = TextAlign.Center,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
       ) {

        if (selectedMenu == "Data Anggota") {

            MemberListScreen(
                onBack = {
                    selectedMenu = "Beranda"
                }
            )

        } else {

        // HALAMAN UTAMA
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(PSJLightBlue)
        ) {

            // HEADER DASHBOARD
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(PSJBlue)
                    .padding(
                        horizontal = 14.dp,
                        vertical = 13.dp
                    ),
                verticalAlignment = Alignment.CenterVertically
            ) {

                // TOMBOL HAMBURGER
                Surface(
                    modifier = Modifier
                        .size(42.dp)
                        .clickable {
                            scope.launch {
                                drawerState.open()
                            }
                        },
                    shape = RoundedCornerShape(10.dp),
                    color = Color.White.copy(alpha = 0.15f)
                ) {
                    Box(
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            "☰",
                            color = Color.White,
                            fontSize = 25.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }

                Spacer(Modifier.width(12.dp))

                Column(
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        "PSJ COMMUNITY",
                        color = Color.White,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.ExtraBold
                    )

                    Text(
                        "Solid • Profesional • Maju",
                        color = Color.White,
                        fontSize = 11.sp
                    )
                }

                Column(
                    horizontalAlignment = Alignment.End
                ) {
                    Text(
                        account.displayName,
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 12.sp
                    )

                    Text(
                        account.memberId.ifBlank { "FOUNDER" },
                        color = Color.White,
                        fontSize = 10.sp
                    )
                }
            }

            // KONTEN DASHBOARD
            Column(
                modifier = Modifier
                    .weight(1f)
                    .verticalScroll(rememberScrollState())
                    .padding(16.dp)
            ) {
                Text(
                    "Selamat Datang 👋",
                    color = PSJDark,
                    fontSize = 21.sp,
                    fontWeight = FontWeight.Bold
                )

                Text(
                    "Keluarga Besar PSJ - Pencari Spxinstant se-Jabodetabek",
                    color = PSJMuted,
                    fontSize = 12.sp
                )

                Spacer(Modifier.height(18.dp))

                // SLIDE DOKUMENTASI
                Text(
                    "Dokumentasi Kegiatan",
                    color = PSJDark,
                    fontSize = 17.sp,
                    fontWeight = FontWeight.Bold
                )

                Spacer(Modifier.height(10.dp))

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .horizontalScroll(slideScrollState),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    slides.forEachIndexed { index, title ->
                        Surface(
                            modifier = Modifier
                                .width(290.dp)
                                .height(165.dp)
                                .clickable {
                                    currentSlide = index
                                    scope.launch {
                                        slideScrollState.animateScrollTo(
                                            index * 302
                                        )
                                    }
                                },
                            shape = RoundedCornerShape(18.dp),
                            color = PSJBlue,
                            shadowElevation = 4.dp
                        ) {
                            Box(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .background(
                                        Brush.verticalGradient(
                                            listOf(
                                                Color(0xFF0B70C9),
                                                Color(0xFF063B78)
                                            )
                                        )
                                    ),
                                contentAlignment = Alignment.Center
                            ) {
                                Column(
                                    horizontalAlignment = Alignment.CenterHorizontally,
                                    verticalArrangement = Arrangement.Center
                                ) {
                                    Text(
                                        "📸",
                                        fontSize = 38.sp
                                    )

                                    Spacer(Modifier.height(10.dp))

                                    Text(
                                        title,
                                        color = Color.White,
                                        fontSize = 14.sp,
                                        fontWeight = FontWeight.ExtraBold,
                                        textAlign = TextAlign.Center
                                    )

                                    Text(
                                        "Foto kegiatan PSJ",
                                        color = Color.White,
                                        fontSize = 11.sp
                                    )
                                }
                            }
                        }
                    }
                }

                Spacer(Modifier.height(10.dp))

                // INDIKATOR SLIDE
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center
                ) {
                    slides.indices.forEach { index ->
                        Box(
                            modifier = Modifier
                                .padding(horizontal = 4.dp)
                                .size(
                                    if (currentSlide == index) 10.dp else 7.dp
                                )
                                .background(
                                    if (currentSlide == index) {
                                        PSJBlue
                                    } else {
                                        Color.LightGray
                                    },
                                    CircleShape
                                )
                                .clickable {
                                    currentSlide = index
                                    scope.launch {
                                        slideScrollState.animateScrollTo(
                                            index * 302
                                        )
                                    }
                                }
                        )
                    }
                }

                Spacer(Modifier.height(24.dp))

                // RINGKASAN KAS
                Text(
                    "Ringkasan Keuangan PSJ",
                    color = PSJDark,
                    fontSize = 17.sp,
                    fontWeight = FontWeight.Bold
                )

                Spacer(Modifier.height(10.dp))

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .horizontalScroll(rememberScrollState()),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    KasDashboardCard(
                        "KAS BULANAN",
                        "Rp0",
                        "💰"
                    )

                    KasDashboardCard(
                        "KAS ANNIVERSARY",
                        "Rp0",
                        "🎉"
                    )

                    KasDashboardCard(
                        "KAS SOLIDARITAS",
                        "Rp0",
                        "🤝"
                    )
                }

                Spacer(Modifier.height(24.dp))

                // FLYER ACARA
                Text(
                    "Acara PSJ Terdekat",
                    color = PSJDark,
                    fontSize = 17.sp,
                    fontWeight = FontWeight.Bold
                )

                Spacer(Modifier.height(10.dp))

                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(205.dp),
                    shape = RoundedCornerShape(18.dp),
                    color = Color.White,
                    shadowElevation = 4.dp
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(PSJBlue),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                "📅",
                                fontSize = 42.sp
                            )

                            Spacer(Modifier.height(8.dp))

                            Text(
                                "FLYER ACARA PSJ",
                                color = Color.White,
                                fontSize = 19.sp,
                                fontWeight = FontWeight.ExtraBold
                            )

                            Text(
                                "Informasi kegiatan akan ditampilkan di sini",
                                color = Color.White,
                                fontSize = 11.sp,
                                textAlign = TextAlign.Center
                            )
                        }
                    }
                }

                Spacer(Modifier.height(20.dp))
            }

            // BOTTOM NAVIGATION TETAP DIPERTAHANKAN
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.White)
                    .padding(
                        horizontal = 5.dp,
                        vertical = 8.dp
                    ),
                verticalAlignment = Alignment.CenterVertically
            ) {
                DashboardNavItem(
                    "Beranda",
                    "⌂",
                    selectedMenu == "Beranda"
                ) {
                    selectedMenu = "Beranda"
                }

                DashboardNavItem(
                    "Kas",
                    "💰",
                    selectedMenu == "Kas"
                ) {
                    selectedMenu = "Kas"

                    Toast.makeText(
                        context,
                        "Menu Kas Digital",
                        Toast.LENGTH_SHORT
                    ).show()
                }

                // TOMBOL QRIS TENGAH
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .offset(y = (-12).dp),
                    contentAlignment = Alignment.Center
                ) {
                    Surface(
                        modifier = Modifier
                            .size(62.dp)
                            .clickable {
                                Toast.makeText(
                                    context,
                                    "Menu QRIS PSJ",
                                    Toast.LENGTH_SHORT
                                ).show()
                            },
                        shape = CircleShape,
                        color = PSJBlue,
                        shadowElevation = 8.dp
                    ) {
                        Box(
                            contentAlignment = Alignment.Center
                        ) {
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text(
                                    "▦",
                                    color = Color.White,
                                    fontSize = 27.sp
                                )

                                Text(
                                    "QRIS",
                                    color = Color.White,
                                    fontSize = 9.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }
                }

                DashboardNavItem(
                    "Absensi",
                    "✓",
                    selectedMenu == "Absensi"
                ) {
                    selectedMenu = "Absensi"

                    Toast.makeText(
                        context,
                        "Menu Absensi Digital",
                        Toast.LENGTH_SHORT
                    ).show()
                }

                DashboardNavItem(
                    "Profil",
                    "👤",
                    selectedMenu == "Profil"
                ) {
                    selectedMenu = "Profil"

                                        Toast.makeText(
                        context,
                        "Menu Profil PSJ",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
        }
    }
}

// ======================================================
// FUNGSI MEMBUKA MENU DRAWER
// ======================================================

private fun openDrawerMenu(
    menu: String,
    scope: kotlinx.coroutines.CoroutineScope,
    drawerState: DrawerState,
    context: android.content.Context
) {
    scope.launch {
        drawerState.close()

        Toast.makeText(
            context,
            "Menu $menu akan dikembangkan pada tahap berikutnya.",
            Toast.LENGTH_SHORT
        ).show()
    }
}

// ======================================================
// JUDUL KELOMPOK MENU DRAWER
// ======================================================

@Composable
fun DrawerSectionTitle(title: String) {
    Text(
        title,
        color = PSJBlue,
        fontSize = 11.sp,
        fontWeight = FontWeight.ExtraBold,
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                start = 20.dp,
                end = 16.dp,
                top = 13.dp,
                bottom = 6.dp
            )
    )
}

// ======================================================
// ITEM MENU DRAWER
// ======================================================

@Composable
fun DrawerMenuItem(
    icon: String,
    title: String,
    selected: Boolean = false,
    iconColor: Color = PSJBlue,
    textColor: Color = PSJDark,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 10.dp, vertical = 2.dp)
            .background(
                color = if (selected) {
                    PSJLightBlue
                } else {
                    Color.Transparent
                },
                shape = RoundedCornerShape(10.dp)
            )
            .clickable {
                onClick()
            }
            .padding(
                horizontal = 12.dp,
                vertical = 11.dp
            ),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            icon,
            color = iconColor,
            fontSize = 19.sp,
            modifier = Modifier.width(34.dp)
        )

        Text(
            title,
            color = textColor,
            fontSize = 13.sp,
            fontWeight = if (selected) {
                FontWeight.Bold
            } else {
                FontWeight.Medium
            }
        )
    }
}

// ======================================================
// GARIS PEMBATAS DRAWER
// ======================================================

@Composable
fun DrawerDivider() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                horizontal = 18.dp,
                vertical = 7.dp
            )
            .height(1.dp)
            .background(Color(0xFFE6ECF2))
    )
}

// ======================================================
// KARTU RINGKASAN KAS
// ======================================================

@Composable
fun KasDashboardCard(
    title: String,
    amount: String,
    icon: String
) {
    Surface(
        modifier = Modifier.width(165.dp),
        shape = RoundedCornerShape(16.dp),
        color = Color.White,
        shadowElevation = 3.dp
    ) {
        Column(
            modifier = Modifier.padding(14.dp)
        ) {
            Text(
                icon,
                fontSize = 24.sp
            )

            Spacer(Modifier.height(8.dp))

            Text(
                title,
                color = PSJMuted,
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(Modifier.height(6.dp))

            Text(
                amount,
                color = PSJBlue,
                fontSize = 19.sp,
                fontWeight = FontWeight.ExtraBold
            )
        }
    }
}

// ======================================================
// ITEM BOTTOM NAVIGATION
// ======================================================

@Composable
fun RowScope.DashboardNavItem(
    title: String,
    icon: String,
    selected: Boolean,
    onClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .weight(1f)
            .clickable {
                onClick()
            }
            .padding(vertical = 4.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            icon,
            fontSize = 21.sp,
            color = if (selected) PSJBlue else PSJMuted
        )

        Text(
            title,
            fontSize = 10.sp,
            color = if (selected) PSJBlue else PSJMuted,
            fontWeight = if (selected) {
                FontWeight.Bold
            } else {
                FontWeight.Normal
            }
        )
    }
}
