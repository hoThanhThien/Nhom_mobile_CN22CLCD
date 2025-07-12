
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
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.HorizontalPagerIndicator
import com.google.accompanist.pager.rememberPagerState
import kotlinx.coroutines.delay
import android.widget.Toast
import androidx.compose.ui.platform.LocalContext
import java.text.NumberFormat
import java.time.LocalDate
import java.time.temporal.ChronoUnit

import java.util.Locale as JavaLocale

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class, ExperimentalPagerApi::class)
@Composable
fun BookingDetailScreen(
    tour: TourModel,
    bookingState: BookingState,
    onBook: (LocalDate, LocalDate, Int) -> Unit,
    onBookingSuccess: () -> Unit,
    onBookingError: (String) -> Unit,
    onResetState: () -> Unit
) {
    val context = LocalContext.current
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
                title = {
                    Text(
                        text = "Chi tiết Tour",
                        style = TextStyle(
                            fontSize   = 30.sp,
                            fontWeight = FontWeight.Bold,
                            fontFamily = FontFamily.Serif  // đổi font family
                        )
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onResetState) {
                        Icon(
                            imageVector   = Icons.Filled.ArrowBack,
                            contentDescription = "Quay lại"
                        )
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    // Màu nền của AppBar
                    containerColor                = Color.White,
                    // Màu chữ tiêu đề
                    titleContentColor             = Color.Black,
                    // Màu icon điều hướng
                    navigationIconContentColor    = Color.Black,
                    // Màu khi content scroll (nếu dùng scrollBehavior)
                    scrolledContainerColor        = Color.Blue
                )
            )
        },
        bottomBar = {
            Column(Modifier.padding(16.dp)) {
                Button(
                    onClick = {
                        if (nights > 0 && startDate != null && endDate != null){
                            onBook(startDate!!, endDate!!, guests)
                    }else {
                            Toast
                                .makeText(
                                    context,
                                    "Vui lòng chọn ngày bắt đầu và kết thúc",
                                    Toast.LENGTH_SHORT
                                )
                                .show()

                }},
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    shape = RoundedCornerShape(28.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.Black,
                    contentColor   = Color.LightGray
                )
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

            // 1. Pager state
            val pagerState = rememberPagerState()
            LaunchedEffect(pagerState) {
                // Vòng lặp vô tận
                while (true) {
                    delay(3000)  // chờ 3s
                    // Next page, quay vòng về 0 khi tới cuối
                    val nextPage = (pagerState.currentPage + 1) % tour.images.size
                    pagerState.animateScrollToPage(nextPage)
                }
            }

            Card(
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp),
                elevation = CardDefaults.cardElevation(8.dp)
            ) {
                Box {
                    HorizontalPager(
                        count = tour.images.size,
                        state     = pagerState,
                        modifier  = Modifier.fillMaxSize()
                    ) { page ->
                        AsyncImage(
                            model             = tour.images[page],
                            contentDescription= null,
                            modifier          = Modifier.fillMaxSize().height(240.dp).clip(RoundedCornerShape(16.dp)),
                                    contentScale = ContentScale.Crop
                        )
                    }

                    // Dots indicator ở dưới
                    HorizontalPagerIndicator(
                        pagerState = pagerState,
                        modifier   = Modifier
                            .align(Alignment.BottomCenter)
                            .padding(8.dp)
                    )
                }
            }
            // Thông tin tour: title, rating, mô tả, location
            Column(
                Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                // 1. Tiêu đề
                Text(
                    text = tour.title,
                    style = MaterialTheme.typography.headlineSmall
                )

                // 2. Rating
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.Star,
                        contentDescription = "Rating",
                        tint = Color(0xFFFFD700)
                    )
                    Spacer(Modifier.width(4.dp))
                    Text(
                        text = "${tour.rating} / 5",
                        style = MaterialTheme.typography.bodyMedium
                    )
                    Spacer(Modifier.width(8.dp))
                    Text(
                        text = "(${tour.reviewCount} đánh giá)",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                // 3. Mô tả chi tiết
                Text(
                    text = tour.description,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                // 4. Địa điểm
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.LocationOn,
                        contentDescription = "Location"
                    )
                    Spacer(Modifier.width(4.dp))
                    Text(
                        text = tour.location,
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }
            Spacer(Modifier.height(16.dp))
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
    Spacer(Modifier.height(16.dp))

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
