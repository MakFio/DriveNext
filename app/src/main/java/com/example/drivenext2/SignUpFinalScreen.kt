package com.example.drivenext2

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import java.text.SimpleDateFormat
import java.util.*

// Функция проверки возраста
fun isOver18(selectedDate: Date?): Boolean {
    if (selectedDate == null) return false
    val calendar = Calendar.getInstance()
    calendar.time = selectedDate
    return calendar.get(Calendar.YEAR) <= Calendar.getInstance().get(Calendar.YEAR) - 2
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SignUpFinalScreen(navController: NavController) {
    val context = LocalContext.current
    var profileImageUri by remember { mutableStateOf<Uri?>(null) }
    var driverLicenseNumber by remember { mutableStateOf("") }
    var driverLicenseUri by remember { mutableStateOf<Uri?>(null) }
    var passportUri by remember { mutableStateOf<Uri?>(null) }
    var issueDate by remember { mutableStateOf<Date?>(null) }
    var dateError by remember { mutableStateOf("") }
    var numberError by remember { mutableStateOf("") }

    val datePickerState = rememberDatePickerState()
    val openDialog = remember { mutableStateOf(false) }
    val errorFillFields = stringResource(R.string.error_fill_fields)
    val errorUnderage = stringResource(R.string.error_underage_2)
    val dateFormatterFinal = SimpleDateFormat("dd.MM.yyyy", Locale.getDefault())

    // Лаунчеры для загрузки
    val profileLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? -> profileImageUri = uri }

    val driverLicenseLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? -> driverLicenseUri = uri }

    val passportLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? -> passportUri = uri }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(24.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.TopCenter),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Круглая иконка фото профиля
            Box(
                modifier = Modifier
                    .size(120.dp)
                    .clip(CircleShape) // Теперь работает
                    .background(MaterialTheme.colorScheme.surfaceVariant)
                    .clickable { profileLauncher.launch("image/*") }
            ) {
                Image(
                    painter = rememberAsyncImagePainter(
                        ImageRequest.Builder(context)
                            .data(profileImageUri ?: R.drawable.ic_add_photo)
                            .crossfade(true)
                            .build()
                    ),
                    contentDescription = "Profile Photo",
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
                if (profileImageUri == null) {
                    Icon(
                        painter = painterResource(R.drawable.ic_add_photo),
                        contentDescription = "Add Photo",
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier
                            .size(48.dp)
                            .align(Alignment.Center)
                    )
                }
            }
            Text(
                text = stringResource(R.string.profile_photo_hint),
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.padding(top = 8.dp, bottom = 24.dp)
            )

            // Поле номера водительского удостоверения
            OutlinedTextField(
                value = driverLicenseNumber,
                onValueChange = {
                    driverLicenseNumber = it.take(10)
                    numberError = if (it.length != 10) "Номер должен содержать 10 символов" else ""
                },
                label = { Text(stringResource(R.string.driver_license_number)) },
                isError = numberError.isNotEmpty(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.fillMaxWidth()
            )
            if (numberError.isNotEmpty()) {
                Text(
                    text = numberError,
                    color = MaterialTheme.colorScheme.error,
                    modifier = Modifier.padding(start = 16.dp, bottom = 16.dp)
                )
            }

            // Дата выдачи
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Дата выдачи",
                    style = MaterialTheme.typography.bodyMedium
                )
                Spacer(modifier = Modifier.width(8.dp))
                Button(
                    onClick = { openDialog.value = true },
                    modifier = Modifier.height(48.dp)
                ) {
                    Text(
                        text = issueDate?.let { dateFormatterFinal.format(it) }
                            ?: stringResource(R.string.select_date)
                    )
                }
            }
            if (dateError.isNotEmpty()) {
                Text(
                    text = dateError,
                    color = MaterialTheme.colorScheme.error,
                    modifier = Modifier.padding(start = 16.dp, bottom = 16.dp)
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Иконки загрузки документов
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                // Водительское удостоверение
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Image(
                        painter = rememberAsyncImagePainter(
                            ImageRequest.Builder(context)
                                .data(driverLicenseUri ?: R.drawable.ic_upload)
                                .crossfade(true)
                                .build()
                        ),
                        contentDescription = "Driver License Preview",
                        modifier = Modifier
                            .size(64.dp)
                            .clip(CircleShape) // Круглые миниатюры для документов
                            .clickable { driverLicenseLauncher.launch("image/*") },
                        contentScale = ContentScale.Crop
                    )
                    Text(
                        text = "Водительское удостоверение",
                        style = MaterialTheme.typography.bodySmall
                    )
                }

                // Паспорт
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Image(
                        painter = rememberAsyncImagePainter(
                            ImageRequest.Builder(context)
                                .data(passportUri ?: R.drawable.ic_upload)
                                .crossfade(true)
                                .build()
                        ),
                        contentDescription = "Passport Preview",
                        modifier = Modifier
                            .size(64.dp)
                            .clip(CircleShape)
                            .clickable { passportLauncher.launch("image/*") },
                        contentScale = ContentScale.Crop
                    )
                    Text(text = "Паспорт", style = MaterialTheme.typography.bodySmall)
                }
            }
        }

        // Кнопка "Далее"
        Button(
            onClick = {
                dateError = if (issueDate == null) errorFillFields
                else if (!isOver18(issueDate)) errorUnderage
                else ""

                numberError = if (driverLicenseNumber.length != 10) "Номер должен содержать 10 символов"
                else ""

                if (profileImageUri != null &&
                    driverLicenseNumber.isNotBlank() &&
                    driverLicenseUri != null &&
                    passportUri != null &&
                    issueDate != null)
                {
                    navController.navigate("registration_success")
                }
            },
            enabled = profileImageUri != null &&
                    driverLicenseNumber.isNotBlank() &&
                    driverLicenseUri != null &&
                    passportUri != null &&
                    issueDate != null,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .height(56.dp)
                .padding(bottom = 24.dp),
            shape = MaterialTheme.shapes.medium
        ) {
            Text(stringResource(R.string.complete_registration))
        }

        // Диалог выбора даты
        if (openDialog.value) {
            DatePickerDialog(
                onDismissRequest = { openDialog.value = false },
                confirmButton = {
                    Button(onClick = {
                        issueDate = datePickerState.selectedDateMillis?.let { Date(it) }
                        openDialog.value = false
                        dateError = if (issueDate != null && !isOver18(issueDate)) errorUnderage else ""
                    }) {
                        Text("OK")
                    }
                },
                dismissButton = {
                    Button(onClick = { openDialog.value = false }) {
                        Text("Отмена")
                    }
                }
            ) {
                DatePicker(state = datePickerState)
            }
        }
    }
}