@Composable
fun HomeScreen(
    tours: List<TourModel>,  // List được lấy từ Firestore qua ViewModel
    onTourClick: (TourModel) -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Chào mừng bạn!") })
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
        ) {
            items(tours) { tour ->
                Card(
                    modifier = Modifier
                        .padding(8.dp)
                        .fillMaxWidth()
                        .clickable { onTourClick(tour) },
                    elevation = 4.dp
                ) {
                    Row(
                        modifier = Modifier.padding(16.dp)
                    ) {
                        // TODO: Dùng Coil để hiển thị ảnh từ URL
                        Image(
                            painter = painterResource(id = R.drawable.placeholder),
                            contentDescription = null,
                            modifier = Modifier
                                .size(64.dp)
                                .clip(CircleShape)
                        )

                        Spacer(modifier = Modifier.width(16.dp))

                        Column {
                            Text(tour.title, fontWeight = FontWeight.Bold)
                            Text("Giá: ${tour.price}₫")
                        }
                    }
                }
            }
        }
    }
}
