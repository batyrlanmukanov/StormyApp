package com.example.stormy.data.repository

import com.example.stormy.data.local.WeatherDao
import com.example.stormy.data.local.WeatherEntity
import com.example.stormy.data.network.RetrofitInstance
import com.example.stormy.data.network.WeatherResponse
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map // Добавим импорт map

class WeatherRepository(private val weatherDao: WeatherDao) {

    // Функция для получения погоды из сети и сохранения в Room
    suspend fun fetchAndCacheWeather(city: String, apiKey: String) {
        try {
            val networkWeather = RetrofitInstance.api.getWeather(city, apiKey)
            val weatherEntity = WeatherEntity(
                city = networkWeather.name,
                temperature = networkWeather.main.temp,
                description = networkWeather.weather.first().description,
                iconCode = networkWeather.weather.first().icon,
                humidity = networkWeather.main.humidity,
                feelsLike = networkWeather.main.feelsLike,
                timestamp = System.currentTimeMillis()
            )
            weatherDao.insertWeather(weatherEntity)
        } catch (e: Exception) {
            // Ошибка при получении данных из сети, но мы не хотим выбрасывать исключение
            // чтобы ViewModel мог показать старые данные из Room
            e.printStackTrace()
            throw e // Перебрасываем ошибку, чтобы ViewModel мог её обработать (например, показать сообщение пользователю)
        }
    }

    // Функция для получения погоды из локальной базы данных
    fun getCachedWeather(city: String): Flow<WeatherEntity?> {
        return weatherDao.getWeather(city)
    }
}