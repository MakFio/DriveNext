package com.example.drivenext2

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.util.*

class RegistrationViewModel : ViewModel() {

    // Экран 1: Регистрация (сохраняем только email и пароль)
    private val _email = MutableStateFlow("")
    val email: StateFlow<String> = _email
    fun updateEmail(email: String) = viewModelScope.launch { _email.emit(email) }

    private val _password = MutableStateFlow("")
    val password: StateFlow<String> = _password
    fun updatePassword(password: String) = viewModelScope.launch { _password.emit(password) }

    // Экран 2: Личные данные
    private val _firstName = MutableStateFlow("")
    val firstName: StateFlow<String> = _firstName
    fun updateFirstName(firstName: String) = viewModelScope.launch { _firstName.emit(firstName) }

    private val _lastName = MutableStateFlow("")
    val lastName: StateFlow<String> = _lastName
    fun updateLastName(lastName: String) = viewModelScope.launch { _lastName.emit(lastName) }

    private val _middleName = MutableStateFlow("")
    val middleName: StateFlow<String> = _middleName
    fun updateMiddleName(middleName: String) = viewModelScope.launch { _middleName.emit(middleName) }

    private val _birthDate = MutableStateFlow<Date?>(null)
    val birthDate: StateFlow<Date?> = _birthDate
    fun updateBirthDate(date: Date) = viewModelScope.launch { _birthDate.emit(date) }

    private val _gender = MutableStateFlow<Gender?>(null)
    val gender: StateFlow<Gender?> = _gender
    fun updateGender(gender: Gender) = viewModelScope.launch { _gender.emit(gender) }

    // Экран 3: Документы
    private val _profileImageUri = MutableStateFlow<Uri?>(null)
    val profileImageUri: StateFlow<Uri?> = _profileImageUri
    fun updateProfileImage(uri: Uri) = viewModelScope.launch { _profileImageUri.emit(uri) }

    private val _driverLicenseNumber = MutableStateFlow("")
    val driverLicenseNumber: StateFlow<String> = _driverLicenseNumber
    fun updateDriverLicenseNumber(number: String) = viewModelScope.launch { _driverLicenseNumber.emit(number) }

    private val _driverLicenseUri = MutableStateFlow<Uri?>(null)
    val driverLicenseUri: StateFlow<Uri?> = _driverLicenseUri
    fun updateDriverLicenseUri(uri: Uri) = viewModelScope.launch { _driverLicenseUri.emit(uri) }

    private val _passportUri = MutableStateFlow<Uri?>(null)
    val passportUri: StateFlow<Uri?> = _passportUri
    fun updatePassportUri(uri: Uri) = viewModelScope.launch { _passportUri.emit(uri) }

    private val _issueDate = MutableStateFlow<Date?>(null)
    val issueDate: StateFlow<Date?> = _issueDate
    fun updateIssueDate(date: Date) = viewModelScope.launch { _issueDate.emit(date) }

    // Проверка полноты данных
    fun isDataComplete(): Boolean {
        return email.value.isNotBlank() &&
                password.value.isNotBlank() &&
                firstName.value.isNotBlank() &&
                lastName.value.isNotBlank() &&
                birthDate.value != null &&
                gender.value != null &&
                profileImageUri.value != null &&
                driverLicenseNumber.value.isNotBlank() &&
                driverLicenseUri.value != null &&
                passportUri.value != null &&
                issueDate.value != null
    }
}