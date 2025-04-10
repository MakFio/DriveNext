package com.example.drivenext2

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.clickable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.draw.clip

@Composable
fun OnboardingScreen(onFinish: () -> Unit) {
    var currentPage by remember { mutableStateOf(0) }
    var skipOnboarding by remember { mutableStateOf(false) }

    if (skipOnboarding) {
        onFinish() // Если пользователь нажал "Пропустить", завершаем онбординг
    } else {
        when (currentPage) {
            0 -> OnboardingScreen1(
                onNext = { currentPage++ },
                onSkip = { skipOnboarding = true },
                currentPage = currentPage
            )
            1 -> OnboardingScreen2(
                onNext = { currentPage++ },
                onSkip = { skipOnboarding = true },
                currentPage = currentPage
            )
            2 -> OnboardingScreen3(
                onStart = { onFinish() },
                currentPage = currentPage
            )
        }
    }
}

@Composable
private fun OnboardingScreen1(onNext: () -> Unit, onSkip: () -> Unit, currentPage: Int) {
    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Image(
                painter = painterResource(id = R.drawable.onboarding_image_1),
                contentDescription = "Onboarding Image 1",
                modifier = Modifier.size(300.dp)
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = stringResource(id = R.string.onboarding_title_1),
                style = MaterialTheme.typography.titleLarge.copy(
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary,
                    textAlign = TextAlign.Center
                )
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = stringResource(id = R.string.onboarding_subtitle_1),
                style = MaterialTheme.typography.bodyMedium.copy(
                    color = MaterialTheme.colorScheme.onSurface,
                    textAlign = TextAlign.Center
                )
            )
            Spacer(modifier = Modifier.height(32.dp)) // Отступ между текстом и кнопкой
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                PageIndicator(currentPage = currentPage, totalPages = 3)
                Button(
                    onClick = onNext,
                    modifier = Modifier
                        .height(56.dp)
                        .width(120.dp) // Фиксированная ширина кнопки
                ) {
                    Text(
                        text = stringResource(id = R.string.next),
                        style = MaterialTheme.typography.labelLarge.copy(
                            color = MaterialTheme.colorScheme.onPrimary
                        )
                    )
                }
            }
        }
        // Кнопка "Пропустить" в правом верхнем углу
        Text(
            text = stringResource(id = R.string.skip),
            style = MaterialTheme.typography.labelLarge.copy(
                color = MaterialTheme.colorScheme.primary
            ),
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(top = 16.dp, end = 16.dp)
                .clickable { onSkip() }
        )
    }
}

@Composable
private fun OnboardingScreen2(onNext: () -> Unit, onSkip: () -> Unit, currentPage: Int) {
    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Image(
                painter = painterResource(id = R.drawable.onboarding_image_2),
                contentDescription = "Onboarding Image 2",
                modifier = Modifier.size(300.dp)
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = stringResource(id = R.string.onboarding_title_2),
                style = MaterialTheme.typography.titleLarge.copy(
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary,
                    textAlign = TextAlign.Center
                )
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = stringResource(id = R.string.onboarding_subtitle_2),
                style = MaterialTheme.typography.bodyMedium.copy(
                    color = MaterialTheme.colorScheme.onSurface,
                    textAlign = TextAlign.Center
                )
            )
            Spacer(modifier = Modifier.height(32.dp)) // Отступ между текстом и кнопкой
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                PageIndicator(currentPage = currentPage, totalPages = 3)
                Button(
                    onClick = onNext,
                    modifier = Modifier
                        .height(56.dp)
                        .width(120.dp) // Фиксированная ширина кнопки
                ) {
                    Text(
                        text = stringResource(id = R.string.next),
                        style = MaterialTheme.typography.labelLarge.copy(
                            color = MaterialTheme.colorScheme.onPrimary
                        )
                    )
                }
            }
        }
        // Кнопка "Пропустить" в правом верхнем углу
        Text(
            text = stringResource(id = R.string.skip),
            style = MaterialTheme.typography.labelLarge.copy(
                color = MaterialTheme.colorScheme.primary
            ),
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(top = 16.dp, end = 16.dp)
                .clickable { onSkip() }
        )
    }
}

@Composable
private fun OnboardingScreen3(onStart: () -> Unit, currentPage: Int) {
    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Image(
                painter = painterResource(id = R.drawable.onboarding_image_3),
                contentDescription = "Onboarding Image 3",
                modifier = Modifier.size(300.dp)
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = stringResource(id = R.string.onboarding_title_3),
                style = MaterialTheme.typography.titleLarge.copy(
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary,
                    textAlign = TextAlign.Center
                )
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = stringResource(id = R.string.onboarding_subtitle_3),
                style = MaterialTheme.typography.bodyMedium.copy(
                    color = MaterialTheme.colorScheme.onSurface,
                    textAlign = TextAlign.Center
                )
            )
            Spacer(modifier = Modifier.height(32.dp)) // Отступ между текстом и кнопкой
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                PageIndicator(currentPage = currentPage, totalPages = 3)
                Button(
                    onClick = onStart,
                    modifier = Modifier
                        .height(56.dp)
                        .width(120.dp) // Фиксированная ширина кнопки
                ) {
                    Text(
                        text = stringResource(id = R.string.start),
                        style = MaterialTheme.typography.labelLarge.copy(
                            color = MaterialTheme.colorScheme.onPrimary
                        )
                    )
                }
            }
        }
        // Кнопка "Пропустить" в правом верхнем углу
        Text(
            text = stringResource(id = R.string.skip),
            style = MaterialTheme.typography.labelLarge.copy(
                color = MaterialTheme.colorScheme.primary
            ),
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(top = 16.dp, end = 16.dp)
                .clickable { onStart() }
        )
    }
}

@Composable
private fun PageIndicator(currentPage: Int, totalPages: Int) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        repeat(totalPages) { index ->
            Box(
                modifier = Modifier
                    .size(width = 24.dp, height = 8.dp) // Овальная форма
                    .clip(RoundedCornerShape(4.dp)) // Закругление углов
                    .background(
                        if (index == currentPage) MaterialTheme.colorScheme.primary
                        else Color.Gray.copy(alpha = 0.5f)
                    )
            )
        }
    }
}