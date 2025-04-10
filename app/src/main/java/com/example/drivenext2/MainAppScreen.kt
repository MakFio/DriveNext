package com.example.drivenext2

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import androidx.compose.runtime.LaunchedEffect

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainAppScreen(navController: NavController) {
    val cars = remember { generateSampleCars() }
    var searchQuery by remember { mutableStateOf("") }
    var isSearchActive by remember { mutableStateOf(false) }
    var isLoading by remember { mutableStateOf(false) }
    val filteredCars = remember(searchQuery) {
        if (searchQuery.isBlank()) {
            cars
        } else {
            cars.filter { it.name.contains(searchQuery, ignoreCase = true) }
        }
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            SearchBar(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                query = searchQuery,
                onQueryChange = { searchQuery = it },
                onSearch = {
                    if (searchQuery.isNotBlank()) {
                        isLoading = true // Показать экран загрузки
                    }
                },
                active = isSearchActive,
                onActiveChange = { isSearchActive = it },
                placeholder = { Text("Введите марку автомобиля") },
                leadingIcon = { Icon(painterResource(R.drawable.ic_search), contentDescription = null) }
            ) {

            }
        },
        bottomBar = {
            BottomAppBar(
                modifier = Modifier.fillMaxWidth(),
                containerColor = MaterialTheme.colorScheme.surfaceVariant
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(
                        onClick = { /* Уже на главном экране */ },
                        modifier = Modifier.weight(1f)
                    ) {
                        Icon(
                            painter = painterResource(R.drawable.ic_home),
                            contentDescription = "Главная",
                            modifier = Modifier.size(24.dp)
                        )
                    }
                    IconButton(
                        onClick = { navController.navigate("bookmarks") },
                        modifier = Modifier.weight(1f)
                    ) {
                        Icon(
                            painter = painterResource(R.drawable.ic_bookmark),
                            contentDescription = "Закладки",
                            modifier = Modifier.size(24.dp)
                        )
                    }
                    IconButton(
                        onClick = { navController.navigate("settings") },
                        modifier = Modifier.weight(1f)
                    ) {
                        Icon(
                            painter = painterResource(R.drawable.ic_setting),
                            contentDescription = "Настройки",
                            modifier = Modifier.size(24.dp)
                        )
                    }
                }
            }
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .background(MaterialTheme.colorScheme.background)
        ) {
            if (isLoading) {
                // Экран загрузки
                LoadingScreen()
                LaunchedEffect(Unit) {
                    kotlinx.coroutines.delay(3000) // Имитация загрузки
                    isLoading = false
                }
            } else {
                Column {
                    // Заголовок в зависимости от состояния
                    Text(
                        text = if (searchQuery.isNotBlank()) "Результаты поиска" else "Давайте найдем автомобиль",
                        style = MaterialTheme.typography.titleLarge,
                        modifier = Modifier.padding(16.dp)
                    )

                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(16.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        items(filteredCars) { car ->
                            CarCard(car = car, navController = navController)
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun LoadingScreen() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            CircularProgressIndicator(
                modifier = Modifier.size(64.dp),
                color = MaterialTheme.colorScheme.primary
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "Ищем подходящие автомобили...",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onBackground
            )
        }
    }
}

@Composable
private fun CarCard(car: CarItem, navController: NavController) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(200.dp)
            .clip(RoundedCornerShape(16.dp))
            .padding(horizontal = 16.dp),
        elevation = CardDefaults.cardElevation(8.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.surface)
                .padding(16.dp)
        ) {
            // Верхняя часть с текстом и изображением
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.Top
            ) {
                // Текстовая часть
                Column(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.Top
                ) {
                    Text(
                        text = car.name,
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                    )
                    Text(
                        text = "${car.pricePerDay} ₽/день",
                        style = MaterialTheme.typography.titleSmall.copy(
                            color = MaterialTheme.colorScheme.primary
                        ),
                        modifier = Modifier.padding(vertical = 8.dp)
                    )

                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        IconText(
                            icon = R.drawable.ic_transmission,
                            text = car.transmissionType
                        )
                        IconText(
                            icon = R.drawable.ic_fuel,
                            text = car.fuelType
                        )
                    }
                }

                // Изображение автомобиля
                Image(
                    painter = rememberAsyncImagePainter(
                        ImageRequest.Builder(LocalContext.current)
                            .data(car.imageUrl)
                            .crossfade(true)
                            .build()
                    ),
                    contentDescription = car.name,
                    modifier = Modifier
                        .size(120.dp)
                        .align(Alignment.Top), // Выровнено по верху
                    contentScale = ContentScale.Fit // Полное изображение
                )
            }

            // Нижняя часть с кнопками
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Button(
                    onClick = { /* Логика бронирования */ },
                    modifier = Modifier
                        .weight(1f)
                        .padding(end = 8.dp),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text("Забронировать")
                }
                TextButton(
                    onClick = { navController.navigate("car_details/${car.id}") },
                    modifier = Modifier
                        .weight(1f)
                        .padding(start = 8.dp)
                ) {
                    Text("Детали")
                }
            }
        }
    }
}

@Composable
private fun IconText(icon: Int, text: String) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Icon(
            painter = painterResource(icon),
            contentDescription = null,
            modifier = Modifier.size(24.dp),
            tint = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Text(
            text = text,
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.padding(start = 4.dp)
        )
    }
}

// карточки
private fun generateSampleCars(): List<CarItem> = listOf(
    CarItem(
        id = 1,
        name = "Toyota Camry 2024",
        pricePerDay = 3500,
        transmissionType = "Автомат",
        fuelType = "Бензин",
        imageUrl = R.drawable.car_toyota
    ),
    CarItem(
        id = 2,
        name = "Hyundai Elantra VI",
        pricePerDay = 3200,
        transmissionType = "Автомат",
        fuelType = "Гибрид",
        imageUrl = R.drawable.car_hyundai
    ),
    CarItem(
        id = 3,
        name = "Hyundai Elantra VI",
        pricePerDay = 3200,
        transmissionType = "Автомат",
        fuelType = "Гибрид",
        imageUrl = R.drawable.car_hyundai
    ),
    CarItem(
        id = 4,
        name = "Hyundai Elantra VI",
        pricePerDay = 3200,
        transmissionType = "Автомат",
        fuelType = "Гибрид",
        imageUrl = R.drawable.car_hyundai
    )
)

data class CarItem(
    val id: Int,
    val name: String,
    val pricePerDay: Int,
    val transmissionType: String,
    val fuelType: String,
    val imageUrl: Int
)