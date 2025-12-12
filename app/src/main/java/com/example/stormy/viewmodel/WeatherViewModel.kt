package com.example.stormy.viewmodel

import android.app.Application // Добавим импорт Application
import androidx.lifecycle.AndroidViewModel // Изменим ViewModel на AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.stormy.data.local.AppDatabase // Добавим импорт AppDatabase
import com.example.stormy.data.local.WeatherEntity
import com.example.stormy.data.repository.WeatherRepository
import kotlinx.coroutines.launch
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.LiveData
import com.example.stormy.data.network.WeatherResponse
import kotlinx.coroutines.flow.collect // Добавим импорт collect

// Изменим на AndroidViewModel, чтобы получить доступ к Application Context
class WeatherViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: WeatherRepository

    // Инициализация репозитория с Dao
    init {
        val weatherDao = AppDatabase.getDatabase(application).weatherDao()
        repository = WeatherRepository(weatherDao)
    }

    private val _weather = MutableLiveData<WeatherEntity?>() // Теперь храним WeatherEntity
    val weather: LiveData<WeatherEntity?> get() = _weather

    private val _loading = MutableLiveData<Boolean>()
    val loading: LiveData<Boolean> get() = _loading

    private val _error = MutableLiveData<String?>()
    val error: LiveData<String?> get() = _error

    fun loadWeather(city: String, apiKey: String) {
        viewModelScope.launch {
            _loading.value = true
            _error.value = null

            try {
                // 1. Сначала пытаемся обновить данные из сети
                repository.fetchAndCacheWeather(city, apiKey)
            } catch (e: Exception) {
                _error.value = "Нет интернета. Показываю старые данные."
            }

            // 2. В ЛЮБОМ СЛУЧАЕ подписываемся на базу данных
            // Как только в базе появится запись (от сетевого запроса), UI обновится сам
            repository.getCachedWeather(city).collect { cachedWeather ->
                if (cachedWeather != null) {
                    _weather.value = cachedWeather
                    _error.value = null // Если данные из базы пришли, убираем текст ошибки
                }
                _loading.value = false
            }
        }
    }
}