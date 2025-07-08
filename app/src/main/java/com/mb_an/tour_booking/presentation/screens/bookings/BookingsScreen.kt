package com.mb_an.tour_booking.presentation.screens.bookings

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.mb_an.tour_booking.data.models.BookingModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BookingsScreen(
    bookings: List<BookingModel>,
    onCancel: (BookingModel) -> Unit
) {
    Scaffold(
        topBar = { TopAppBar(title = { Text("Lịch sử đặt tour") }) }
    ) { padding ->
        if (bookings.isEmpty()) {
            Box(
                Modifier
                    .fillMaxSize()
                    .padding(padding),
                contentAlignment = Alignment.Center
            ) {
                Text("😔 Bạn chưa đặt tour nào")
            }
        } else {
            LazyColumn(
                Modifier
                    .fillMaxSize()
                    .padding(padding)
            ) {
                items(bookings) { booking ->
                    Card(
                        Modifier
                            .fillMaxWidth()
                            .padding(8.dp),
                        elevation = CardDefaults.cardElevation(4.dp)
                    ) {
                        Row(
                            Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment     = Alignment.CenterVertically
                        ) {
                            Column {
                                Text(
                                    text  = booking.tourTitle,
                                    style = MaterialTheme.typography.titleMedium
                                )
                                Spacer(Modifier.height(4.dp))
                                Text(
                                    text  = "Ngày: ${booking.date}",
                                    style = MaterialTheme.typography.bodyMedium
                                )
                            }
                            Button(onClick = { onCancel(booking) }) {
                                Text("Hủy")
                            }
                        }
                    }
                }
            }
        }
    }
}
