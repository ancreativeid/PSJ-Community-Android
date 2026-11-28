@file:OptIn(androidx.compose.material3.ExperimentalMaterial3Api::class)

package com.psj.community

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.tasks.await

private const val PSJ_DATABASE_URL =
    "https://psj-community-v2-default-rtdb.asia-southeast1.firebasedatabase.app"

private data class PSJMemberItem(
    val id: String,
    val name: String
)

@Composable
fun MemberListScreen(
    onBack: () -> Unit
) {
    var members by remember {
        mutableStateOf<List<PSJMemberItem>>(emptyList())
    }

    var searchQuery by remember {
        mutableStateOf("")
    }

    var isLoading by remember {
        mutableStateOf(true)
    }

    var errorMessage by remember {
        mutableStateOf("")
    }

   var reloadKey by remember {
    mutableStateOf(0)
}

    LaunchedEffect(reloadKey) {
        isLoading = true
        errorMessage = ""

        try {
            val snapshot = FirebaseDatabase
                .getInstance(PSJ_DATABASE_URL)
                .reference
                .child("members")
                .child("active")
                .get()
                .await()

            val result = snapshot.children.mapNotNull { child ->

                val memberId = child.key ?: return@mapNotNull null

                // Hanya mengambil ID anggota PSJ.
                if (!memberId.matches(Regex("PSJ-\\d{3}"))) {
                    return@mapNotNull null
                }

                val name = child.child("name")
                    .getValue(String::class.java)
                    ?.takeIf { it.isNotBlank() }
                    ?: "Nama belum tersedia"

                PSJMemberItem(
                    id = memberId,
                    name = name
                )
            }.sortedBy {
                it.id.substringAfter("-").toIntOrNull() ?: 0
            }

            members = result

            if (result.isEmpty()) {
                errorMessage = "Belum ada data anggota aktif."
            }

        } catch (e: Exception) {
            errorMessage = e.localizedMessage
                ?: "Gagal mengambil data anggota dari Firebase."
        }

        isLoading = false
    }

    val filteredMembers = members.filter {
        it.name.contains(searchQuery, ignoreCase = true) ||
                it.id.contains(searchQuery, ignoreCase = true)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFEAF3FC))
    ) {

        // HEADER HALAMAN

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFF0756A5))
                .padding(
                    horizontal = 16.dp,
                    vertical = 16.dp
                ),
            verticalAlignment = Alignment.CenterVertically
        ) {

            TextButton(
                onClick = onBack,
                colors = ButtonDefaults.textButtonColors(
                    contentColor = Color.White
                )
            ) {
                Text(
                    "‹",
                    fontSize = 30.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(Modifier.width(8.dp))

            Column {
                Text(
                    "DATA ANGGOTA",
                    color = Color.White,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.ExtraBold
                )

                Text(
                    "Keluarga Besar PSJ",
                    color = Color.White,
                    fontSize = 12.sp
                )
            }
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {

            // TOTAL ANGGOTA

            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                color = Color.White,
                shadowElevation = 3.dp
            ) {
                Row(
                    modifier = Modifier.padding(18.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(
                        modifier = Modifier.weight(1f)
                    ) {
                        Text(
                            "TOTAL ANGGOTA AKTIF",
                            color = Color(0xFF52657D),
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold
                        )

                        Spacer(Modifier.height(5.dp))

                        Text(
                            if (isLoading) "..." else "${members.size} Anggota",
                            color = Color(0xFF0756A5),
                            fontSize = 25.sp,
                            fontWeight = FontWeight.ExtraBold
                        )
                    }

                    Text(
                        "👥",
                        fontSize = 35.sp
                    )
                }
            }

            Spacer(Modifier.height(14.dp))

            // PENCARIAN

            OutlinedTextField(
                value = searchQuery,
                onValueChange = {
                    searchQuery = it
                },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                label = {
                    Text("Cari nama atau ID anggota")
                },
                placeholder = {
                    Text("Contoh: Alpian / PSJ-006")
                },
                shape = RoundedCornerShape(12.dp)
            )

            Spacer(Modifier.height(14.dp))

            // DAFTAR ANGGOTA

            when {
                isLoading -> {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            CircularProgressIndicator(
                                color = Color(0xFF0756A5)
                            )

                            Spacer(Modifier.height(12.dp))

                            Text(
                                "Memuat data anggota...",
                                color = Color(0xFF52657D),
                                fontSize = 13.sp
                            )
                        }
                    }
                }

                errorMessage.isNotBlank() -> {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                errorMessage,
                                color = Color(0xFFB3261E),
                                fontSize = 13.sp
                            )

                            Spacer(Modifier.height(12.dp))

                            Button(
                                onClick = {
                                    reloadKey++
                                },
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = Color(0xFF0756A5)
                                )
                            ) {
                                Text("Coba Lagi")
                            }
                        }
                    }
                }

                filteredMembers.isEmpty() -> {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            "Anggota tidak ditemukan.",
                            color = Color(0xFF52657D),
                            fontSize = 13.sp
                        )
                    }
                }

                else -> {
                    LazyColumn(
                        modifier = Modifier.weight(1f),
                        verticalArrangement = Arrangement.spacedBy(9.dp)
                    ) {
                        items<PSJMemberItem>(
    items = filteredMembers,
    key = { member -> member.id }
) { member ->

                            Surface(
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(13.dp),
                                color = Color.White,
                                shadowElevation = 2.dp
                            ) {
                                Row(
                                    modifier = Modifier.padding(14.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {

                                    Surface(
                                        modifier = Modifier.size(43.dp),
                                        shape = RoundedCornerShape(12.dp),
                                        color = Color(0xFFEAF3FC)
                                    ) {
                                        Box(
                                            contentAlignment = Alignment.Center
                                        ) {
                                            Text(
                                                "👤",
                                                fontSize = 22.sp
                                            )
                                        }
                                    }

                                    Spacer(Modifier.width(12.dp))

                                    Column(
                                        modifier = Modifier.weight(1f)
                                    ) {
                                        Text(
                                            member.name,
                                            color = Color(0xFF172B4D),
                                            fontSize = 14.sp,
                                            fontWeight = FontWeight.Bold
                                        )

                                        Spacer(Modifier.height(3.dp))

                                        Text(
                                            member.id,
                                            color = Color(0xFF52657D),
                                            fontSize = 11.sp
                                        )
                                    }

                                    Surface(
                                        shape = RoundedCornerShape(20.dp),
                                        color = Color(0xFFE5F5EC)
                                    ) {
                                        Text(
                                            "AKTIF",
                                            modifier = Modifier.padding(
                                                horizontal = 10.dp,
                                                vertical = 5.dp
                                            ),
                                            color = Color(0xFF167044),
                                            fontSize = 9.sp,
                                            fontWeight = FontWeight.Bold
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
