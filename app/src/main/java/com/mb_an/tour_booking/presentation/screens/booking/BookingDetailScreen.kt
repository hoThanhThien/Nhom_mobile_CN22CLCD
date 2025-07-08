package com.mb_an.tour_booking.presentation.screens.booking

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.mb_an.tour_booking.data.models.TourModel
import com.mb_an.tour_booking.presentation.viewmodel.BookingState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
//fun BookingDetailScreen(
//    tour: TourModel,
//    bookingState: BookingState,
//    onBook: () -> Unit,
//    onBookingSuccess: () -> Unit,
//    onBookingError: (String) -> Unit,
//    onResetState: () -> Unit // ✅ Callback để reset state
//) {
//    Scaffold(
//        topBar = {
//            TopAppBar(
//                title = { Text("Chi tiết Tour") }
//            )
//        }
//    ) { paddingValues ->
//
//        Box(
//            modifier = Modifier
//                .padding(paddingValues)
//                .fillMaxSize()
//        ) {
//            Column(
//                modifier = Modifier
//                    .padding(16.dp)
//                    .fillMaxWidth()
//            ) {
//                // Ảnh tour
//                AsyncImage(
//                    model = tour.imageUrl,
//                    contentDescription = "Ảnh tour",
//                    contentScale = ContentScale.Crop,
//                    modifier = Modifier
//                        .fillMaxWidth()
//                        .height(200.dp)
//                        .padding(bottom = 16.dp)
//                )
//
//                Text(
//                    text = tour.title,
//                    style = MaterialTheme.typography.titleLarge
//                )
//
//                Spacer(modifier = Modifier.height(8.dp))
//
//                Text("🗓 Thời gian: ${tour.date}")
//                Text("📍 Địa điểm: ${tour.location}")
//                Text("💵 Giá: ${tour.price}₫")
//
//                Spacer(modifier = Modifier.height(24.dp))
//
//                Button(
//                    onClick = { onBook() },
//                    modifier = Modifier.fillMaxWidth(),
//                    shape = RoundedCornerShape(8.dp),
//                    enabled = bookingState !is BookingState.Loading // ✅ Disable khi đang loading
//                ) {
//                    Text("Đặt ngay")
//                }
//            }
//
//            if (bookingState is BookingState.Loading) {
//                CircularProgressIndicator(
//                    modifier = Modifier.align(Alignment.Center)
//                )
//            }
//        }
//    }
//
//    // ✅ Xử lý Success/Error + reset state
//    LaunchedEffect(bookingState) {
//        when (bookingState) {
//            is BookingState.Success -> {
//                onBookingSuccess()
//                onResetState() // ✅ Reset state sau khi xử lý
//            }
//            is BookingState.Error -> {
//                onBookingError(bookingState.message)
//                onResetState() // ✅ Reset state sau khi xử lý
//            }
//            else -> Unit
//        }
//    }
//}
// app/src/main/java/com/mb_an/tour_booking/presentation/screens/bookings/BookingDetailScreen.kt

fun BookingDetailScreen(
    tour: TourModel,
    bookingState: BookingState,
    onBook: () -> Unit,
    onBookingSuccess: () -> Unit,
    onBookingError: (String) -> Unit,
    onResetState: () -> Unit
) {
    Scaffold(
        topBar = { TopAppBar(title = { Text("Chi tiết: ${tour.title}") }) }
    ) { padding ->
        Column(
            Modifier
                .padding(padding)
                .verticalScroll(rememberScrollState())
                .fillMaxSize()
        ) {
            AsyncImage(
                model           = tour.imageUrl,
                contentScale    = ContentScale.Crop,
                contentDescription = tour.title,
                modifier        = Modifier
                    .fillMaxWidth()
                    .height(220.dp)
            )
            Spacer(Modifier.height(16.dp))
            Column(Modifier.padding(16.dp)) {
                Text(tour.title, style = MaterialTheme.typography.titleLarge)
                Spacer(Modifier.height(8.dp))
                // Rating
                Row(verticalAlignment = Alignment.CenterVertically) {
                    repeat(5) { idx ->
                        val color = if (idx < tour.rating.toInt()) Color(0xFFFFD700) else Color.LightGray
                        Icon(Icons.Filled.Star, contentDescription = null, tint = color, modifier = Modifier.size(20.dp))
                    }
                    Spacer(Modifier.width(8.dp))
                    Text(String.format("%.1f", tour.rating), style = MaterialTheme.typography.bodyMedium)
                }
                Spacer(Modifier.height(8.dp))
                Text("📍 ${tour.location}", style = MaterialTheme.typography.bodyMedium)
                Spacer(Modifier.height(8.dp))
                Text("💵 Giá: ${tour.price}₫", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                Spacer(Modifier.height(16.dp))
                Text("Mô tả", style = MaterialTheme.typography.titleSmall)
                Spacer(Modifier.height(4.dp))
                Text(tour.description, style = MaterialTheme.typography.bodyMedium)
                Spacer(Modifier.height(24.dp))

                Button(
                    onClick = onBook,
                    enabled = bookingState !is BookingState.Loading,
                    modifier= Modifier.fillMaxWidth()
                ) {
                    Text("Đặt Tour")
                }
                if (bookingState is BookingState.Loading) {
                    Spacer(Modifier.height(8.dp))
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally))
                }
            }
        }
    }

    // Xử lý kết quả
    LaunchedEffect(bookingState) {
        when (bookingState) {
            is BookingState.Success -> {
                onBookingSuccess()
                onResetState()
            }
            is BookingState.Error -> {
                onBookingError(bookingState.message)
                onResetState()
            }
            else -> Unit
        }
    }
}
