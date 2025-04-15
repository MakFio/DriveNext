package com.example.drivenext2

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class SettingsViewModel : ViewModel() {
    // Тип темы
    enum class ThemeMode {
        SYSTEM, DARK, LIGHT
    }

    // Состояние темы
    private val _currentTheme = MutableStateFlow(ThemeMode.SYSTEM)
    val currentTheme: StateFlow<ThemeMode> = _currentTheme

    // Установка темы
    fun setTheme(mode: ThemeMode) = viewModelScope.launch {
        _currentTheme.emit(mode)
    }

    // Аватар
    private val _profileImageUri = MutableStateFlow<Uri?>(null)
    val profileImageUri: StateFlow<Uri?> = _profileImageUri

    fun updateProfileImage(uri: Uri?) = viewModelScope.launch {
        _profileImageUri.emit(uri)
    }

    fun clearProfileImage() = viewModelScope.launch {
        _profileImageUri.emit(null)
    }
    // Фильтр сортировки
    enum class SortMode {
        NONE, ASCENDING, DESCENDING
    }

    private val _sortMode = MutableStateFlow(SortMode.NONE)
    val sortMode: StateFlow<SortMode> = _sortMode

    fun setSortMode(mode: SortMode) = viewModelScope.launch {
        _sortMode.emit(mode)
    }
}