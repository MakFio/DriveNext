package com.example.drivenext2

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.drivenext2.R
import java.util.regex.Pattern

@Composable
fun SignUpScreen(navController: NavController) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var showPassword by remember { mutableStateOf(false) }
    var isAgreed by remember { mutableStateOf(false) }

    // Состояния ошибок
    var emailError by remember { mutableStateOf("") }
    var passwordError by remember { mutableStateOf("") }
    var confirmPasswordError by remember { mutableStateOf("") }

    // Функция валидации email
    fun validateEmail(email: String): Boolean {
        val emailPattern = Pattern.compile(
            "[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}"
        )
        return emailPattern.matcher(email).matches() && email.length >= 6
    }

    // Функция валидации пароля
    fun validatePassword(password: String): Boolean {
        return password.length >= 8 &&
                password.any { it.isDigit() } &&
                password.any { it.isUpperCase() } &&
                password.any { it.isLowerCase() }
    }

    // Основной контент
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // Верхняя панель с заголовком и кнопкой назад
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 32.dp), // Отступ для основного контента
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Кнопка назад
            IconButton(
                onClick = { navController.popBackStack() },
                modifier = Modifier.size(48.dp)
            ) {
                Icon(
                    painter = painterResource(R.drawable.ic_arrow_back), // Ваша иконка стрелки
                    contentDescription = "Back",
                    tint = Color.Black
                )
            }

            // Заголовок
            Text(
                text = "Создать аккаунт",
                style = MaterialTheme.typography.titleLarge.copy(
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                ),
                modifier = Modifier.padding(start = 16.dp)
            )
        }

        // Основные поля (центрированы)
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 80.dp), // Смещение под верхнюю панель
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // Поле для электронной почты
            OutlinedTextField(
                value = email,
                onValueChange = {
                    email = it
                    emailError = if (it.isBlank()) "Обязательное поле"
                    else if (!validateEmail(it)) "Неверный формат email"
                    else ""
                },
                label = { Text(stringResource(id = R.string.email)) },
                isError = emailError.isNotEmpty(),
                modifier = Modifier.fillMaxWidth()
            )
            if (emailError.isNotEmpty()) {
                Text(
                    text = emailError,
                    color = MaterialTheme.colorScheme.error,
                    modifier = Modifier.padding(start = 16.dp, bottom = 8.dp)
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Поле для пароля
            OutlinedTextField(
                value = password,
                onValueChange = {
                    password = it
                    passwordError = if (it.isBlank()) "Обязательное поле"
                    else if (!validatePassword(it)) "Пароль должен быть не менее 8 символов, содержать цифры и буквы разного регистра"
                    else ""
                },
                label = { Text(stringResource(id = R.string.password)) },
                visualTransformation = if (showPassword) VisualTransformation.None else PasswordVisualTransformation(),
                trailingIcon = {
                    IconButton(onClick = { showPassword = !showPassword }) {
                        Icon(
                            painter = painterResource(
                                id = if (showPassword) R.drawable.ic_visibility else R.drawable.ic_visibility_off
                            ),
                            contentDescription = "Toggle Password Visibility"
                        )
                    }
                },
                isError = passwordError.isNotEmpty(),
                modifier = Modifier.fillMaxWidth()
            )
            if (passwordError.isNotEmpty()) {
                Text(
                    text = passwordError,
                    color = MaterialTheme.colorScheme.error,
                    modifier = Modifier.padding(start = 16.dp, bottom = 8.dp)
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Поле для подтверждения пароля
            OutlinedTextField(
                value = confirmPassword,
                onValueChange = {
                    confirmPassword = it
                    confirmPasswordError = if (it != password) "Пароли не совпадают" else ""
                },
                label = { Text(stringResource(id = R.string.confirm_password)) },
                visualTransformation = if (showPassword) VisualTransformation.None else PasswordVisualTransformation(),
                isError = confirmPasswordError.isNotEmpty(),
                modifier = Modifier.fillMaxWidth()
            )
            if (confirmPasswordError.isNotEmpty()) {
                Text(
                    text = confirmPasswordError,
                    color = MaterialTheme.colorScheme.error,
                    modifier = Modifier.padding(start = 16.dp, bottom = 8.dp)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Чекбокс согласия
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(bottom = 16.dp)
            ) {
                Checkbox(
                    checked = isAgreed,
                    onCheckedChange = { isAgreed = it },
                    colors = CheckboxDefaults.colors(checkedColor = MaterialTheme.colorScheme.primary)
                )
                Text(
                    text = stringResource(id = R.string.agree_terms),
                    style = MaterialTheme.typography.bodySmall
                )
            }

            // Кнопка "Далее"
            Button(
                onClick = {
                    emailError = if (!validateEmail(email)) "Неверный формат email" else ""
                    passwordError = if (!validatePassword(password)) "Пароль должен быть не менее 8 символов, содержать цифры и буквы разного регистра" else ""
                    confirmPasswordError = if (confirmPassword != password) "Пароли не совпадают" else ""

                    if (validateEmail(email) && validatePassword(password) && confirmPassword == password && isAgreed) {
                        navController.navigate("sign_up_second")
                    }
                },
                enabled = validateEmail(email) && validatePassword(password) && confirmPassword == password && isAgreed,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
            ) {
                Text(stringResource(id = R.string.next))
            }
        }
    }
}