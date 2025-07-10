
//package com.mb_an.tour_booking.presentation.screens.booking
//
//import android.icu.text.NumberFormat
//import android.os.Build
//import androidx.annotation.RequiresApi
//import androidx.compose.foundation.layout.*
//import androidx.compose.foundation.rememberScrollState
//import androidx.compose.foundation.verticalScroll
//import androidx.compose.material.icons.Icons
//import androidx.compose.material.icons.filled.Add
//import androidx.compose.material.icons.filled.Remove
//import androidx.compose.material.icons.filled.Star
//import androidx.compose.material3.*
//import androidx.compose.runtime.Composable
//import androidx.compose.runtime.LaunchedEffect
//import androidx.compose.runtime.getValue
//import androidx.compose.runtime.mutableStateOf
//import androidx.compose.runtime.remember
//import androidx.compose.runtime.rememberCoroutineScope
//import androidx.compose.runtime.setValue
//import androidx.compose.ui.Alignment
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.graphics.Color
//import androidx.compose.ui.layout.ContentScale
//import androidx.compose.ui.platform.LocalContext
//import androidx.compose.ui.text.font.FontWeight
//import androidx.compose.ui.text.intl.Locale
//import androidx.compose.ui.text.style.TextAlign
//import androidx.compose.ui.unit.dp
//import coil.compose.AsyncImage
//import com.mb_an.tour_booking.data.models.TourModel
//import com.mb_an.tour_booking.presentation.viewmodel.BookingState
//import java.time.LocalDate
//import java.time.temporal.ChronoUnit
//
//@RequiresApi(Build.VERSION_CODES.O)
//@OptIn(ExperimentalMaterial3Api::class)
//@Composable
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
package com.mb_an.tour_booking.presentation.screens.booking
import com.mb_an.tour_booking.data.models.TourModel
import com.mb_an.tour_booking.presentation.viewmodel.BookingState

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import java.text.NumberFormat
import java.time.LocalDate
import java.time.temporal.ChronoUnit
import java.util.Locale as JavaLocale

//@RequiresApi(Build.VERSION_CODES.O)
//@OptIn(ExperimentalMaterial3Api::class)
//@Composable
//fun BookingDetailScreen(
//    tour: TourModel,
//    bookingState: BookingState,
//    onBook: (start: LocalDate, end: LocalDate, guests: Int) -> Unit,
//    onBookingSuccess: () -> Unit,
//    onBookingError: (String) -> Unit,
//    onResetState: () -> Unit
//) {
//    // state cho dialog và range-picker
//    val dateState = rememberDateRangePickerState()
//    var showDateDialog by remember { mutableStateOf(false) }
//
//    // state khách
//    var guests by remember { mutableStateOf(1) }
//
//    // tính đêm và tổng tiền
//    var startDate by remember { mutableStateOf<LocalDate?>(null) }
//    var endDate   by remember { mutableStateOf<LocalDate?>(null) }
//    val nights by derivedStateOf {
//        if (startDate != null && endDate != null && endDate!!.isAfter(startDate)) {
//            ChronoUnit.DAYS.between(startDate, endDate).toInt()
//        } else 0
//    }
//    val totalPrice by derivedStateOf { nights * tour.price * guests }
//
//    Scaffold(topBar = {
//        TopAppBar(title = { Text("Chi tiết: ${tour.title}") })
//    }) { padding ->
//        Column(
//            Modifier
//                .padding(padding)
//                .fillMaxSize()
//                .verticalScroll(rememberScrollState())
//                .padding(16.dp)
//        ) {
//            AsyncImage(
//                model = tour.imageUrl,
//                contentDescription = null,
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .height(200.dp)
//            )
//            Spacer(Modifier.height(16.dp))
//
//            Text("Chọn ngày", style = MaterialTheme.typography.titleMedium)
//            OutlinedButton(
//                onClick = { showDateDialog = true },
//                modifier = Modifier.fillMaxWidth()
//            ) {
//                if (startDate != null && endDate != null) {
//                    Text("$startDate – $endDate")
//                } else {
//                    Text("Chọn ngày bắt đầu – kết thúc")
//                }
//            }
//
//            Spacer(Modifier.height(16.dp))
//            Text("Số khách", style = MaterialTheme.typography.titleMedium)
//            Row(verticalAlignment = Alignment.CenterVertically) {
//                IconButton(onClick = { if (guests > 1) guests-- }) {
//                    Icon(Icons.Default.Remove, contentDescription = "Giảm")
//                }
//                Text("$guests", Modifier.width(40.dp), textAlign = TextAlign.Center)
//                IconButton(onClick = { guests++ }) {
//                    Icon(Icons.Default.Add, contentDescription = "Tăng")
//                }
//            }
//
//            Spacer(Modifier.height(16.dp))
//            if (nights > 0) {
//                Text("Số đêm: $nights", style = MaterialTheme.typography.bodyLarge)
//                Text("Tổng tiền:", style = MaterialTheme.typography.bodyLarge)
//                Text(
//                    NumberFormat
//                        .getCurrencyInstance(JavaLocale("vi","VN"))
//                        .format(totalPrice),
//                    style = MaterialTheme.typography.headlineSmall,
//                    color = MaterialTheme.colorScheme.primary
//                )
//            }
//
//            Spacer(Modifier.height(24.dp))
//            Button(
//                onClick = {
//                    if (nights > 0 && startDate != null && endDate != null) {
//                        onBook(startDate!!, endDate!!, guests)
//                    } else {
//                        // TODO: show Snackbar/Toast
//                    }
//                },
//                modifier = Modifier.fillMaxWidth(),
//                enabled = bookingState !is BookingState.Loading
//            ) {
//                Text("Đặt Tour")
//            }
//            if (bookingState is BookingState.Loading) {
//                Spacer(Modifier.height(8.dp))
//                LinearProgressIndicator(Modifier.fillMaxWidth())
//            }
//        }
//    }
//
//    // Hiện dialog chọn range
//    if (showDateDialog) {
//        DatePickerDialog(
//            onDismissRequest = { showDateDialog = false },
//            confirmButton = {
//                TextButton(onClick = {
//                    // chuyển millis -> LocalDate
//                    dateState.selectedStartDateMillis?.let { s ->
//                        dateState.selectedEndDateMillis?.let { e ->
//                            startDate = LocalDate.ofEpochDay(s / 86_400_000)
//                            endDate   = LocalDate.ofEpochDay(e / 86_400_000)
//                        }
//                    }
//                    showDateDialog = false
//                }) {
//                    Text("Xác nhận")
//                }
//            },
//            dismissButton = {
//                TextButton(onClick = { showDateDialog = false }) {
//                    Text("Hủy")
//                }
//            }
//        ) {
//            DateRangePicker(state = dateState)
//        }
//    }
//
//    // Xử lý kết quả
//    LaunchedEffect(bookingState) {
//        when (bookingState) {
//            is BookingState.Success -> {
//                onResetState()
//                onBookingSuccess()
//            }
//            is BookingState.Error -> {
//                onResetState()
//                onBookingError(bookingState.message)
//            }
//            else -> {}
//        }
//    }
//}
@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BookingDetailScreen(
    tour: TourModel,
    bookingState: BookingState,
    onBook: (LocalDate, LocalDate, Int) -> Unit,
    onBookingSuccess: () -> Unit,
    onBookingError: (String) -> Unit,
    onResetState: () -> Unit
) {
    val dateState = rememberDateRangePickerState()
    var showDateDialog by remember { mutableStateOf(false) }
    var startDate by remember { mutableStateOf<LocalDate?>(null) }
    var endDate by remember { mutableStateOf<LocalDate?>(null) }
    var guests by remember { mutableStateOf(1) }

    val nights by derivedStateOf {
        if (startDate != null && endDate != null && endDate!!.isAfter(startDate))
            ChronoUnit.DAYS.between(startDate, endDate).toInt()
        else 0
    }
    val totalPrice by derivedStateOf { nights * tour.price * guests }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Chi tiết Tour") },
                navigationIcon = {
                    IconButton(onClick = onResetState) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        },
        bottomBar = {
            Column(Modifier.padding(16.dp)) {
                Button(
                    onClick = {
                        if (nights > 0 && startDate != null && endDate != null)
                            onBook(startDate!!, endDate!!, guests)
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    shape = RoundedCornerShape(28.dp)
                ) {
                    if (bookingState is BookingState.Loading)
                        CircularProgressIndicator(color = Color.White, modifier = Modifier.size(24.dp))
                    else
                        Text("Đặt Tour", style = MaterialTheme.typography.titleMedium)
                }
            }
        }
    ) { innerPadding ->
        Column(
            Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // 1. Ảnh + overlay Title + Price
            Card(
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(220.dp),
                elevation = CardDefaults.cardElevation(8.dp)
            ) {
                Box {
                    AsyncImage(
                        model = tour.imageUrl,
                        contentDescription = tour.title,
                        modifier = Modifier.fillMaxSize()
                    )
                    // gradient overlay
                    Box(
                        Modifier
                            .fillMaxSize()
                            .background(
                                Brush.verticalGradient(
                                    0f to Color.Transparent,
                                    1f to Color(0xAA000000)
                                )
                            )
                    )
                    Column(
                        Modifier
                            .align(Alignment.BottomStart)
                            .padding(16.dp)
                    ) {
                        Text(
                            tour.title,
                            style = MaterialTheme.typography.headlineSmall,
                            color = Color.White
                        )
                        Text(
                            NumberFormat
                                .getCurrencyInstance(JavaLocale("vi","VN"))
                                .format(tour.price),
                            style = MaterialTheme.typography.titleMedium,
                            color = Color.White
                        )
                    }
                }
            }

            // 2. Date picker
            Card(
                shape = RoundedCornerShape(12.dp),
                elevation = CardDefaults.cardElevation(4.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { showDateDialog = true }
            ) {
                Row(
                    Modifier
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Icon(Icons.Default.DateRange, contentDescription = null)
                    Column {
                        Text("Chọn ngày", style = MaterialTheme.typography.labelLarge)
                        Text(
                            if (startDate != null && endDate != null)
                                "$startDate → $endDate"
                            else "Bắt đầu — Kết thúc",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }

            // 3. Guest selector
            Card(
                shape = RoundedCornerShape(12.dp),
                elevation = CardDefaults.cardElevation(4.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    Modifier
                        .padding(16.dp)
                        .fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text("Số khách", style = MaterialTheme.typography.labelLarge)
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        IconButton(
                            onClick = { if (guests > 1) guests-- }
                        ) {
                            Icon(Icons.Default.Remove, contentDescription = "Giảm")
                        }
                        Text(
                            "$guests",
                            style = MaterialTheme.typography.titleMedium,
                            modifier = Modifier.width(32.dp),
                            textAlign = TextAlign.Center
                        )
                        IconButton(onClick = { guests++ }) {
                            Icon(Icons.Default.Add, contentDescription = "Tăng")
                        }
                    }
                }
            }

            // 4. Summary
            if (nights > 0) {
                Card(
                    shape = RoundedCornerShape(12.dp),
                    elevation = CardDefaults.cardElevation(4.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(Modifier.padding(16.dp)) {
                        Text("Số đêm: $nights", style = MaterialTheme.typography.bodyLarge)
                        Spacer(Modifier.height(8.dp))
                        Row(
                            Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text("Tổng tiền", style = MaterialTheme.typography.titleMedium)
                            Text(
                                NumberFormat
                                    .getCurrencyInstance(JavaLocale("vi","VN"))
                                    .format(totalPrice),
                                style = MaterialTheme.typography.headlineSmall,
                                color = MaterialTheme.colorScheme.primary
                            )
                        }
                    }
                }
            }
        }
    }

    // DateRangePicker Dialog
    if (showDateDialog) {
        DatePickerDialog(
            onDismissRequest = { showDateDialog = false },
            confirmButton = {
                TextButton(onClick = {
                    dateState.selectedStartDateMillis?.let { s ->
                        dateState.selectedEndDateMillis?.let { e ->
                            startDate = LocalDate.ofEpochDay(s / 86_400_000)
                            endDate = LocalDate.ofEpochDay(e / 86_400_000)
                        }
                    }
                    showDateDialog = false
                }) { Text("Xác nhận") }
            },
            dismissButton = {
                TextButton(onClick = { showDateDialog = false }) { Text("Hủy") }
            }
        ) {
            DateRangePicker(state = dateState)
        }
    }

    // Handle bookingState effect...
    LaunchedEffect(bookingState) {
        when (bookingState) {
            is BookingState.Success -> {
                onResetState()
                onBookingSuccess()
            }
            is BookingState.Error -> {
                onResetState()
                onBookingError(bookingState.message)
            }
            else -> Unit
        }
    }
}
