
package com.psj.community

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.psj.community.ui.theme.MyComposeApplicationTheme
import kotlinx.coroutines.delay

private val PSJBlue = Color(0xFF174A91)
private val PSJRed = Color(0xFFD50000)
private val PSJGold = Color(0xFFFFC857)
private val PSJWhite = Color.White
private val PSJLight = Color(0xFFF1F6FF)
private val PSJGray = Color(0xFF71829A)

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

    LaunchedEffect(Unit) {
        delay(2500)
        currentPage = 1
    }

    when (currentPage) {
        0 -> SplashScreen()
        1 -> WelcomeScreen(
            onStart = { currentPage = 2 }
        )
        2 -> LoginScreen(
            onBack = { currentPage = 1 }
        )
    }
}

@Composable
fun PSJLogo(
    modifier: Modifier = Modifier,
    size: androidx.compose.ui.unit.Dp = 180.dp
) {
    Image(
        painter = painterResource(
            id = R.drawable.psj_logo
        ),
        contentDescription = "Logo PSJ Community",
        modifier = modifier.size(size),
        contentScale = ContentScale.Fit
    )
}

// HALAMAN 1: SPLASH SCREEN

@Composable
fun SplashScreen() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(PSJBlue),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            PSJLogo(size = 210.dp)

            Spacer(modifier = Modifier.height(28.dp))

            Text(
                text = "PSJ COMMUNITY",
                color = PSJWhite,
                fontSize = 27.sp,
                fontWeight = FontWeight.ExtraBold
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "PENCARI SPXINSTANT JABODETABEK",
                color = PSJGold,
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(35.dp))

            CircularProgressIndicator(
                color = PSJGold,
                strokeWidth = 3.dp,
                modifier = Modifier.size(32.dp)
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Mempersiapkan aplikasi...",
                color = PSJWhite,
                fontSize = 12.sp
            )
        }

        Text(
            text = "SOLID • PROFESIONAL • MAJU",
            color = PSJWhite,
            fontSize = 11.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 30.dp)
        )
    }
}

// HALAMAN 2: SELAMAT DATANG

@Composable
fun WelcomeScreen(
    onStart: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(PSJBlue)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(22.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(30.dp))

            PSJLogo(size = 190.dp)

            Spacer(modifier = Modifier.height(18.dp))

            Text(
                text = "SELAMAT DATANG",
                color = PSJWhite,
                fontSize = 25.sp,
                fontWeight = FontWeight.ExtraBold,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "KELUARGA BESAR PSJ",
                color = PSJGold,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(25.dp))

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(22.dp),
                colors = CardDefaults.cardColors(
                    containerColor = PSJWhite
                ),
                elevation = CardDefaults.cardElevation(
                    defaultElevation = 5.dp
                )
            ) {
                Column(
                    modifier = Modifier.padding(22.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Satu Keluarga, Satu Tujuan",
                        color = PSJBlue,
                        fontSize = 19.sp,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    Text(
                        text = "Selamat datang di aplikasi resmi " +
                            "Pencari Spxinstant se-Jabodetabek. " +
                            "Tempat untuk mempererat persaudaraan, " +
                            "berbagi informasi, dan membangun " +
                            "komunitas yang solid.",
                        color = Color.DarkGray,
                        fontSize = 14.sp,
                        textAlign = TextAlign.Center,
                        lineHeight = 22.sp
                    )

                    Spacer(modifier = Modifier.height(18.dp))

                    Text(
                        text = "SOLID • PROFESIONAL • MAJU",
                        color = PSJBlue,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center
                    )
                }
            }

            Spacer(modifier = Modifier.height(28.dp))

            Button(
                onClick = onStart,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(55.dp),
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = PSJRed,
                    contentColor = PSJWhite
                )
            ) {
                Text(
                    text = "MULAI",
                    fontSize = 17.sp,
                    fontWeight = FontWeight.ExtraBold
                )
            }

            Spacer(modifier = Modifier.height(22.dp))

            Text(
                text = "PSJ COMMUNITY • ANDROID",
                color = PSJWhite,
                fontSize = 11.sp,
                fontWeight = FontWeight.Medium
            )

            Spacer(modifier = Modifier.height(20.dp))
        }
    }
}

// HALAMAN 3: LOGIN

@Composable
fun LoginScreen(
    onBack: () -> Unit
) {
    var memberId by remember {
        mutableStateOf("PSJ-006")
    }

    var password by remember {
        mutableStateOf("")
    }

    var rememberMe by remember {
        mutableStateOf(true)
    }

    var message by remember {
        mutableStateOf("")
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(PSJRed),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(22.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            PSJLogo(size = 155.dp)

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = "PSJ COMMUNITY",
                color = PSJWhite,
                fontSize = 23.sp,
                fontWeight = FontWeight.ExtraBold
            )

            Text(
                text = "Silakan masuk ke akun Anda",
                color = PSJWhite,
                fontSize = 13.sp
            )

            Spacer(modifier = Modifier.height(25.dp))

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(
                    containerColor = PSJWhite
                ),
                elevation = CardDefaults.cardElevation(
                    defaultElevation = 6.dp
                )
            ) {
                Column(
                    modifier = Modifier.padding(22.dp)
                ) {
                    Text(
                        text = "LOGIN ANGGOTA",
                        color = PSJBlue,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.ExtraBold
                    )

                    Spacer(modifier = Modifier.height(20.dp))

                    OutlinedTextField(
                        value = memberId,
                        onValueChange = {
                            memberId = it
                        },
                        modifier = Modifier.fillMaxWidth(),
                        label = { Text("ID PSJ") },
                        placeholder = {
                            Text("Contoh: PSJ-006")
                        },
                        singleLine = true,
                        shape = RoundedCornerShape(12.dp)
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    OutlinedTextField(
                        value = password,
                        onValueChange = {
                            password = it
                        },
                        modifier = Modifier.fillMaxWidth(),
                        label = { Text("Password") },
                        singleLine = true,
                        visualTransformation =
                            PasswordVisualTransformation(),
                        shape = RoundedCornerShape(12.dp)
                    )

                    Spacer(modifier = Modifier.height(12.dp))

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
                            text = "Ingat saya",
                            color = Color.DarkGray,
                            fontSize = 13.sp
                        )
                    }

                    Spacer(modifier = Modifier.height(18.dp))

                    Button(
                        onClick = {
                            message =
                                if (memberId.isBlank() ||
                                    password.isBlank()
                                ) {
                                    "ID PSJ dan password wajib diisi."
                                } else {
                                    "Sistem login belum dihubungkan ke Firebase."
                                }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(52.dp),
                        shape = RoundedCornerShape(13.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = PSJBlue
                        )
                    ) {
                        Text(
                            text = "MASUK",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    if (message.isNotEmpty()) {
                        Spacer(modifier = Modifier.height(12.dp))

                        Text(
                            text = message,
                            color = PSJRed,
                            fontSize = 12.sp,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }

                    Spacer(modifier = Modifier.height(18.dp))

                    Text(
                        text = "Lupa Password?",
                        color = PSJBlue,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.align(Alignment.CenterHorizontally)
                    )

                    Spacer(modifier = Modifier.height(15.dp))

                    TextButton(
                        onClick = onBack,
                        modifier = Modifier.align(Alignment.CenterHorizontally)
                    ) {
                        Text(
                            text = "Kembali",
                            color = PSJBlue
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(22.dp))

            Text(
                text = "PENCARI SPXINSTANT JABODETABEK",
                color = PSJWhite,
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )
        }
    }
}
