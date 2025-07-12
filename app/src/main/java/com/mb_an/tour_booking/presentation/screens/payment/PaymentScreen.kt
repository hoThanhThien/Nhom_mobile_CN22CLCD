package com.mb_an.tour_booking.presentation.screens.payment



import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.mb_an.tour_booking.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PaymentScreen(
    @DrawableRes qrResId: Int,
    onPaid:            () -> Unit
) {
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Thanh toán", color = Color.Black,fontWeight = FontWeight.Bold,) },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor           = MaterialTheme.colorScheme.primary,
                    titleContentColor        = Color.White,
                    navigationIconContentColor = Color.Black
                )
            )
        },
        bottomBar = {
            Button(
                onClick = onPaid,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
                    .height(48.dp),
                shape = RoundedCornerShape(24.dp)
            ) {
                Text("Đã thanh toán")
            }
        }
    ) { inner ->
        Column(
            Modifier
                .padding(inner)
                .fillMaxSize()
                .padding(32.dp),
            horizontalAlignment   = Alignment.CenterHorizontally,
            verticalArrangement   = Arrangement.Center
        ) {
            Text("Quét mã QR của bạn để thanh toán", style = MaterialTheme.typography.titleMedium)
            Spacer(Modifier.height(24.dp))
            Image(
                painter = painterResource(qrResId),
                contentDescription = "QR Payment Code",
                modifier = Modifier
                    .size(240.dp)
            )
        }
    }
}
