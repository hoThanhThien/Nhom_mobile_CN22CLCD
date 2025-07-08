// app/src/main/java/com/mb_an/tour_booking/presentation/screens/bookings/BookingsScreen.kt
package com.mb_an.tour_booking.presentation.screens.bookings

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.mb_an.tour_booking.data.models.BookingModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BookingsScreen(
    bookings: List<BookingModel>,
    onCancel: (BookingModel) -> Unit,
    onViewDetail: (BookingModel) -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Danh sách Đã đặt") })
        }
    ) { paddingValues ->
        LazyColumn(
            contentPadding = paddingValues,
            verticalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp, vertical = 8.dp)
        ) {
            items(bookings) { booking ->
                Card(
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier
                        .fillMaxWidth(),
                    elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        // 1. Ảnh Tour
                        AsyncImage(
                            model = booking.tour.imageUrl,
                            contentDescription = booking.tour.title,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(180.dp)
                                .clip(RoundedCornerShape(8.dp)),
                            contentScale = ContentScale.Crop
                        )

                        Spacer(Modifier.height(8.dp))

                        // 2. Thông tin Tour
                        Text(
                            text = booking.tour.title,
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.SemiBold
                        )
                        Spacer(Modifier.height(4.dp))

                        Text("Ngày khởi hành: ${booking.date}", style = MaterialTheme.typography.bodyMedium)
                        Text("${booking.days} ngày - ${booking.nights} đêm", style = MaterialTheme.typography.bodyMedium)
                        Text("Số lượng: ${booking.quantity}", style = MaterialTheme.typography.bodyMedium)

                        Spacer(Modifier.height(4.dp))
                        Text(
                            "Trạng thái: ${booking.status}",
                            color = if (booking.status == "Hoàn tất") Color(0xFF4CAF50) else Color(0xFFFFA000),
                            style = MaterialTheme.typography.bodyMedium
                        )

                        Spacer(Modifier.height(8.dp))

                        // 3. Tổng tiền
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text("Tổng: ", fontWeight = FontWeight.SemiBold)
                            Text(
                                text = booking.totalPrice.toString() + " ₫",
                                color = Color(0xFFD32F2F),
                                fontWeight = FontWeight.Bold
                            )
                        }

                        Spacer(Modifier.height(12.dp))

                        // 4. Nút Chi tiết & Hủy
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Button(
                                onClick = { onViewDetail(booking) },
                                modifier = Modifier.weight(1f)
                            ) {
                                Text("Chi tiết Tour")
                            }
                            OutlinedButton(
                                onClick = { onCancel(booking) },
                                modifier = Modifier.weight(1f)
                            ) {
                                Text("Hủy đặt")
                            }
                        }
                    }
                }
            }
        }
    }
}
