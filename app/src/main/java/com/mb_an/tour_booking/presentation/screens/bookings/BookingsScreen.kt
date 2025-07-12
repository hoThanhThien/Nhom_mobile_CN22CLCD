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
    bookings:    List<BookingModel>,
    onCancel:    (BookingModel) -> Unit,
    onViewDetail:(BookingModel) -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Danh sách Đã đặt") })
        }
    ) { paddingValues ->
        if (bookings.isEmpty()) {
            // khi chưa có booking nào
            Box(
                Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                contentAlignment = Alignment.Center
            ) {
                Text("Bạn chưa có đơn đặt nào", color = Color.Gray)
            }
        } else {
           
