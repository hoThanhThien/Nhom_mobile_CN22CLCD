
package com.mb_an.tour_booking.presentation.screens.home

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.Search
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.mb_an.tour_booking.data.models.TourModel
import com.mb_an.tour_booking.data.models.CategoryModel
import com.mb_an.tour_booking.presentation.viewmodel.HomeViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.HorizontalPagerIndicator
import com.google.accompanist.pager.rememberPagerState
import kotlinx.coroutines.delay




@OptIn(ExperimentalMaterial3Api::class, ExperimentalPagerApi::class)
@Composable
fun HomeScreen(
    bannerUrls: List<String>,
    categories: List<CategoryModel>,
    tours:      List<TourModel>,
    onCategoryClick: (CategoryModel) -> Unit,
    onTourClick:     (TourModel)   -> Unit,

) {
    // State tìm kiếm
    var query by remember { mutableStateOf("") }
    // Filtered list
    val filteredTours = remember(query, tours) {
        if (query.isBlank()) tours
        else tours.filter {
            it.title.contains(query.trim(), ignoreCase = true)
        }
    }
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {
        // 1. Search + Banner
        item {
            // Trạng thái cho truy vấn tìm kiếm
            OutlinedTextField(
                value = query,
                onValueChange = { query = it },           // ← cập nhật local state
                placeholder   = { Text("Tìm kiếm tour...") },
                leadingIcon   = { Icon(Icons.Outlined.Search, contentDescription = null) },
                modifier      = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape         = RoundedCornerShape(28.dp)
            )
            Spacer(Modifier.height(16.dp))
            if (bannerUrls.isNotEmpty()) {
                // 1. Tạo state cho pager
                val pagerState = rememberPagerState()

                // 2. (Tuỳ chọn) Auto-scroll
                LaunchedEffect(pagerState) {
                    while (true) {
                        delay(3000)
                        val next = (pagerState.currentPage + 1) % bannerUrls.size
                        pagerState.animateScrollToPage(next)
                    }
                }

                // 3. Vẽ carousel
                Card(
                    modifier  = Modifier
                        .fillMaxWidth()
                        .height(180.dp),
                    shape     = RoundedCornerShape(12.dp),
                    elevation = CardDefaults.cardElevation(6.dp)
                ) {
                    Box {
                        HorizontalPager(
                            count    = bannerUrls.size,
                            state    = pagerState,
                            modifier = Modifier.fillMaxSize()
                        ) { page ->
                            AsyncImage(
                                model             = bannerUrls[page],
                                contentDescription= null,
                                modifier          = Modifier.fillMaxSize(),
                                contentScale      = ContentScale.Crop
                            )
                        }
                        HorizontalPagerIndicator(
                            pagerState = pagerState,
                            modifier   = Modifier
                                .align(Alignment.BottomCenter)
                                .padding(8.dp)
                        )
                    }
                }
            } else {
                // fallback nếu không có bannerUrls
                AsyncImage(
                    model            = "https://sgweb.vn/wp-content/uploads/2022/12/75b1d5aa3eb003170055303a362d1a7e.jpg",
                    contentDescription= "Banner",
                    modifier         = Modifier
                        .fillMaxWidth()
                        .height(180.dp)
                        .clip(RoundedCornerShape(12.dp)),
                    contentScale     = ContentScale.Crop
                )
            }
        }


        // 2. Danh mục (Categories)
        item {
            SectionHeader("Danh mục")
            Spacer(Modifier.height(8.dp))
            LazyRow(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                items(categories) { cat ->
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.clickable { onCategoryClick(cat) }
                    ) {
                        AsyncImage(
                            model = cat.imageUrl,
                            contentDescription = cat.name,
                            modifier = Modifier
                                .size(64.dp)
                                .clip(CircleShape),
                            contentScale = ContentScale.Crop
                        )
                        Spacer(Modifier.height(4.dp))
                        Text(cat.name, style = MaterialTheme.typography.bodyMedium)
                    }
                }
            }
        }

        // 3. Yêu thích (Favorites)
        item {
            SectionHeader("Yêu thích")
            Spacer(Modifier.height(8.dp))
            LazyRow(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                items(filteredTours) { tour ->
                    FavoriteCard(tour, onTourClick)
                }
            }
        }

        // 4. Tất cả tour (All Tours)
        item { SectionHeader("Tất cả tour") }
        items(filteredTours) { tour ->
            TourListItem(tour, onTourClick)
        }
    }
}

// Header cho mỗi phần
@Composable
fun SectionHeader(title: String) {
    Row(
        Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(title, style = MaterialTheme.typography.titleMedium)
        Text("Xem tất cả", style = MaterialTheme.typography.bodySmall)
    }
}

// Card hiển thị tour yêu thích
@Composable
fun FavoriteCard(tour: TourModel, onClick: (TourModel)->Unit) = Card(
    modifier = Modifier
        .width(200.dp)
        .clickable { onClick(tour) },
    shape = RoundedCornerShape(12.dp),
    elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
) {
    Column {
        AsyncImage(
            model = tour.imageUrl,
            contentDescription = tour.title,
            modifier = Modifier
                .fillMaxWidth()
                .height(120.dp)
                .clip(RoundedCornerShape(topStart = 12.dp, topEnd = 12.dp)),
            contentScale = ContentScale.Crop
        )
        Spacer(Modifier.height(8.dp))
        Column(Modifier.padding(8.dp)) {
            Text(tour.title, style = MaterialTheme.typography.titleSmall)
            Spacer(Modifier.height(4.dp))
            Text("${tour.price}₫", fontWeight = FontWeight.Bold, color = Color.Red)
        }
    }
}

// Mục trong danh sách dọc
@Composable
fun TourListItem(tour: TourModel, onClick: (TourModel)->Unit) = Card(
    modifier = Modifier
        .fillMaxWidth()
        .clickable { onClick(tour) },
    shape = RoundedCornerShape(12.dp),
    elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
) {
    Row {
        AsyncImage(
            model = tour.imageUrl,
            contentDescription = tour.title,
            modifier = Modifier
                .size(100.dp)
                .clip(RoundedCornerShape(topStart = 12.dp, bottomStart = 12.dp)),
            contentScale = ContentScale.Crop
        )
        Spacer(Modifier.width(12.dp))
        Column(
            modifier = Modifier
                .padding(12.dp)
                .align(Alignment.CenterVertically)
        ) {
            Text(tour.title, style = MaterialTheme.typography.titleMedium)
            Spacer(Modifier.height(4.dp))
            Text(tour.location, style = MaterialTheme.typography.bodyMedium)
            Spacer(Modifier.height(4.dp))
            //price
            Text("${tour.price}₫", fontWeight = FontWeight.Bold, color = Color.Red)
            //rating
            Row(verticalAlignment = Alignment.CenterVertically) {
                repeat(5) { idx ->
                    val tint = if (idx < tour.rating.toInt()) Color(0xFFFFD700) else Color.LightGray
                    Icon(Icons.Filled.Star, contentDescription = null, tint = tint, modifier = Modifier.size(16.dp))
                }
                Spacer(Modifier.width(4.dp))
                Text(String.format("%.1f", tour.rating), style = MaterialTheme.typography.bodySmall)
            }
            Spacer(Modifier.height(4.dp))
        }
    }
}