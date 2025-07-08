@Composable
fun BookingDetailScreen(
    tour: TourModel,
    onBook: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Chi tiết Tour") })
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .padding(16.dp)
        ) {
            // Hiển thị ảnh đại diện (sau này dùng Coil để load online)
            Image(
                painter = painterResource(id = R.drawable.placeholder),
                contentDescription = null,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
            )

            Text(tour.title, style = MaterialTheme.typography.h6)
            Spacer(modifier = Modifier.height(8.dp))
            Text("Thời gian: ${tour.date}")
            Text("Địa điểm: ${tour.location}")
            Text("Giá: ${tour.price}₫")
            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = {
                    // TODO: Gọi Firebase để đặt lịch
                    onBook()
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Đặt ngay")
            }
        }
    }
}
