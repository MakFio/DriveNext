package com.example.drivenext2

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import java.text.SimpleDateFormat
import java.util.*
import java.util.regex.Pattern
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Text

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SignUpSecondScreen(navController: NavController) {
    // Строки из ресурсов
    val errorFillName = stringResource(id = R.string.error_fill_name)
    val errorInvalidName = stringResource(id = R.string.error_invalid_name)
    val errorUnderage = stringResource(id = R.string.error_underage)
    val signUpNextText = stringResource(id = R.string.sign_up_next)
    val selectDateText = stringResource(id = R.string.select_date)

    var firstName by remember { mutableStateOf("") }
    var lastName by remember { mutableStateOf("") }
    var middleName by remember { mutableStateOf("") }
    var selectedDate by remember { mutableStateOf<Date?>(null) }
    var selectedGender by remember { mutableStateOf<Gender?>(null) }
    var nameError by remember { mutableStateOf("") }
    var dateError by remember { mutableStateOf("") }

    val datePickerState = rememberDatePickerState()
    val openDialog = remember { mutableStateOf(false) }

    // Функция для валидации ФИО
    fun validateName(name: String): Boolean {
        return Pattern.matches("^[a-zA-Zа-яА-ЯёЁ]+$", name) && name.isNotEmpty()
    }

    // Функция для проверки возраста
    fun isOver18(selectedDate: Date?): Boolean {
        if (selectedDate == null) return false
        val calendar = Calendar.getInstance()
        calendar.time = selectedDate
        val currentYear = Calendar.getInstance().get(Calendar.YEAR)
        val birthYear = calendar.get(Calendar.YEAR)
        return (currentYear - birthYear) >= 18
    }

    // Автоматическое форматирование имени
    fun formatName(name: String): String {
        return name.replace(Regex("[^a-zA-Zа-яА-ЯёЁ]"), "")
            .lowercase()
            .replaceFirstChar { it.uppercase() }
    }

    // Обработчик ввода ФИО
    fun handleNameInput(name: String, onNameChange: (String) -> Unit, errorSetter: (String) -> Unit) {
        val formattedName = formatName(name)
        onNameChange(formattedName)
        errorSetter(
            when {
                formattedName.isEmpty() -> errorFillName
                !validateName(formattedName) -> errorInvalidName
                else -> ""
            }
        )
    }

    // Сброс ошибки при открытии диалога
    if (openDialog.value) {
        DatePickerDialog(
            onDismissRequest = {
                openDialog.value = false
                dateError = "" // Сбрасываем ошибку при закрытии диалога
            },
            confirmButton = {
                Button(onClick = {
                    val newDate = datePickerState.selectedDateMillis?.let { Date(it) }
                    selectedDate = newDate
                    openDialog.value = false
                    dateError = if (newDate != null && !isOver18(newDate)) errorUnderage else ""
                }) {
                    Text("OK")
                }
            },
            dismissButton = {
                Button(onClick = {
                    openDialog.value = false
                    dateError = "" // Сбрасываем ошибку при отмене
                }) {
                    Text("Отмена")
                }
            }
        ) {
            DatePicker(state = datePickerState)
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // Фамилия
        OutlinedTextField(
            value = lastName,
            onValueChange = {
                handleNameInput(it, { lastName = it }, { nameError = it })
            },
            label = { Text(stringResource(id = R.string.last_name)) },
            isError = nameError.isNotEmpty(),
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))

        // Имя
        OutlinedTextField(
            value = firstName,
            onValueChange = {
                handleNameInput(it, { firstName = it }, { nameError = it })
            },
            label = { Text(stringResource(id = R.string.first_name)) },
            isError = nameError.isNotEmpty(),
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))

        // Отчество
        OutlinedTextField(
            value = middleName,
            onValueChange = {
                handleNameInput(it, { middleName = it }, { /* Без ошибок для отчества */ })
            },
            label = { Text(stringResource(id = R.string.middle_name)) },
            isError = nameError.isNotEmpty(),
            modifier = Modifier.fillMaxWidth()
        )
        if (nameError.isNotEmpty()) {
            Text(
                text = nameError,
                color = MaterialTheme.colorScheme.error,
                modifier = Modifier.padding(start = 16.dp, bottom = 8.dp)
            )
        }
        if (dateError.isNotEmpty()) {
            Text(
                text = dateError,
                color = MaterialTheme.colorScheme.error,
                modifier = Modifier.padding(start = 16.dp, bottom = 8.dp)
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Дата рождения (кнопка всегда активна)
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = stringResource(id = R.string.date_of_birth),
                style = MaterialTheme.typography.bodyMedium
            )
            Spacer(modifier = Modifier.width(8.dp))
            Button(
                onClick = {
                    openDialog.value = true
                    dateError = "" // Сбрасываем ошибку при повторном открытии
                },
                modifier = Modifier.height(48.dp)
            ) {
                Text(
                    text = selectedDate?.let { dateFormatter.format(it) } ?: selectDateText
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Выбор пола
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = stringResource(id = R.string.gender),
                style = MaterialTheme.typography.bodyMedium
            )
            Spacer(modifier = Modifier.width(8.dp))
            GenderRadioButton(
                gender = Gender.Male,
                selectedGender = selectedGender,
                onGenderSelected = { selectedGender = it },
                modifier = Modifier.weight(1f)
            )
            GenderRadioButton(
                gender = Gender.Female,
                selectedGender = selectedGender,
                onGenderSelected = { selectedGender = it },
                modifier = Modifier.weight(1f)
            )
        }

        Spacer(modifier = Modifier.weight(1f))

        // Кнопка "Далее"
        Button(
            onClick = {
                // Проверка при отправке
                nameError = when {
                    lastName.isBlank() -> errorFillName
                    !validateName(lastName) -> errorInvalidName
                    firstName.isBlank() -> errorFillName
                    !validateName(firstName) -> errorInvalidName
                    else -> ""
                }

                dateError = when {
                    selectedDate == null -> errorFillName
                    !isOver18(selectedDate) -> errorUnderage
                    else -> ""
                }

                if (nameError.isEmpty() && dateError.isEmpty() && selectedGender != null) {
                    navController.navigate("sign_up_final")
                }
            },
            enabled = validateName(firstName) && validateName(lastName) && selectedDate != null && isOver18(selectedDate) && selectedGender != null,
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
                .padding(bottom = 16.dp)
        ) {
            Text(signUpNextText)
        }
    }
}

@Composable
private fun GenderRadioButton(
    gender: Gender,
    selectedGender: Gender?,
    onGenderSelected: (Gender) -> Unit,
    modifier: Modifier = Modifier
) {
    val genderText = stringResource(
        id = if (gender == Gender.Male) R.string.male else R.string.female
    )

    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        RadioButton(
            selected = selectedGender == gender,
            onClick = { onGenderSelected(gender) },
            colors = RadioButtonDefaults.colors(selectedColor = MaterialTheme.colorScheme.primary)
        )
        Text(
            text = genderText,
            modifier = Modifier.padding(start = 8.dp)
        )
    }
}

enum class Gender {
    Male, Female
}

val dateFormatter = SimpleDateFormat("dd.MM.yyyy", Locale.getDefault())