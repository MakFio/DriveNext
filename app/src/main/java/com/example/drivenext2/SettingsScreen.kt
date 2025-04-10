package com.example.drivenext2

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.compose.ui.graphics.graphicsLayer
import androidx.lifecycle.viewmodel.compose.viewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(navController: NavController, viewModel: SettingsViewModel = viewModel()) {
    var showThemeDialog by remember { mutableStateOf(false) }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = { Text("Настройки") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant // Цвет фона панели
                )
            )
        },
        bottomBar = {
            BottomAppBar(
                modifier = Modifier.fillMaxWidth(),
                containerColor = MaterialTheme.colorScheme.surfaceVariant // Цвет фона нижней панели
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(onClick = { navController.navigate("main_app") }) {
                        Icon(
                            painterResource(R.drawable.ic_home),
                            contentDescription = "Главная",
                            modifier = Modifier.size(24.dp),
                            tint = MaterialTheme.colorScheme.onSurface // Цвет иконки
                        )
                    }
                    IconButton(onClick = { navController.navigate("bookmarks") }) {
                        Icon(
                            painterResource(R.drawable.ic_bookmark),
                            contentDescription = "Закладки",
                            modifier = Modifier.size(24.dp),
                            tint = MaterialTheme.colorScheme.onSurface // Цвет иконки
                        )
                    }
                    IconButton(onClick = { /* Уже на экране настроек */ }) {
                        Icon(
                            painterResource(R.drawable.ic_setting),
                            contentDescription = "Настройки",
                            modifier = Modifier.size(24.dp),
                            tint = MaterialTheme.colorScheme.primary // Активная иконка
                        )
                    }
                }
            }
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .background(MaterialTheme.colorScheme.background), // Фон списка
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            item {
                ProfileSettingItem(
                    initials = "ИИ",
                    email = "admin@mail.ru",
                    onClick = { navController.navigate("profile") }
                )
            }

            item {
                Column {
                    Spacer(modifier = Modifier.height(16.dp))
                    Divider(
                        modifier = Modifier
                            .fillMaxWidth()
                            .graphicsLayer(alpha = 0.12f),
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.12f) // Цвет разделителя
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                }
            }

            items(
                listOf(
                    SettingItem(R.drawable.ic_booking, "Мои бронирования"),
                    SettingItem(R.drawable.ic_theme, "Тема"),
                    SettingItem(R.drawable.ic_notification, "Уведомления"),
                    SettingItem(R.drawable.ic_cash, "Подключить свой автомобиль"),
                    SettingItem(R.drawable.ic_help, "Помощь"),
                    SettingItem(R.drawable.ic_mail, "Пригласить друга")
                )
            ) { item ->
                if (item.title == "Тема") {
                    SettingListItem(
                        iconRes = item.icon,
                        title = item.title,
                        onClick = { showThemeDialog = true }
                    )
                } else {
                    SettingListItem(
                        iconRes = item.icon,
                        title = item.title,
                        onClick = { /* Логика клика */ }
                    )
                }
            }
        }
    }

    // Диалог выбора темы
    if (showThemeDialog) {
        val currentTheme by viewModel.currentTheme.collectAsState()

        AlertDialog(
            onDismissRequest = { showThemeDialog = false },
            title = { Text("Выбор темы") },
            text = {
                Column {
                    ThemeOptionItem(
                        text = "Системная",
                        selected = currentTheme == SettingsViewModel.ThemeMode.SYSTEM,
                        onClick = { viewModel.setTheme(SettingsViewModel.ThemeMode.SYSTEM) }
                    )
                    ThemeOptionItem(
                        text = "Тёмная",
                        selected = currentTheme == SettingsViewModel.ThemeMode.DARK,
                        onClick = { viewModel.setTheme(SettingsViewModel.ThemeMode.DARK) }
                    )
                    ThemeOptionItem(
                        text = "Светлая",
                        selected = currentTheme == SettingsViewModel.ThemeMode.LIGHT,
                        onClick = { viewModel.setTheme(SettingsViewModel.ThemeMode.LIGHT) }
                    )
                }
            },
            confirmButton = {
                TextButton(onClick = { showThemeDialog = false }) {
                    Text("Готово", color = MaterialTheme.colorScheme.primary) // Цвет текста кнопки
                }
            }
        )
    }
}

@Composable
private fun ThemeOptionItem(
    text: String,
    selected: Boolean,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        RadioButton(
            selected = selected,
            onClick = onClick
        )
        Text(
            text = text,
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.padding(start = 16.dp)
        )
    }
}

@Composable
private fun ProfileSettingItem(
    initials: String,
    email: String,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Аватар
        Box(
            modifier = Modifier
                .size(56.dp)
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.primaryContainer)
                .clickable { /* Заглушка для аватара */ }
        ) {
            Text(
                text = initials,
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                ),
                modifier = Modifier.align(Alignment.Center)
            )
        }

        Spacer(modifier = Modifier.width(16.dp))

        // Текст
        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = "Иван Иванов",
                style = MaterialTheme.typography.titleMedium
            )
            Text(
                text = email,
                style = MaterialTheme.typography.bodyMedium.copy(
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            )
        }

        // Стрелка
        Icon(
            painter = painterResource(R.drawable.ic_next),
            contentDescription = "Перейти",
            modifier = Modifier
                .size(24.dp)
                .padding(end = 8.dp)
        )
    }
}

@Composable
private fun SettingListItem(
    iconRes: Int,
    title: String,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Иконка
        Icon(
            painter = painterResource(iconRes),
            contentDescription = title,
            modifier = Modifier.size(24.dp)
        )

        Spacer(modifier = Modifier.width(16.dp))

        // Текст
        Text(
            text = title,
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.weight(1f)
        )

        // Стрелка
        Icon(
            painter = painterResource(R.drawable.ic_next),
            contentDescription = "Перейти",
            modifier = Modifier
                .size(24.dp)
                .padding(end = 8.dp)
        )
    }
}

data class SettingItem(
    val icon: Int,
    val title: String
)