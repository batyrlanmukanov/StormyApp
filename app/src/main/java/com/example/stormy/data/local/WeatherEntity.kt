package com.example.stormy.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "weather_cache")
data class WeatherEntity(
    @PrimaryKey val city: String, // Город будет первичным ключом
    val temperature: Double,
    val description: String,
    val iconCode: String, // Для иконки погоды
    val humidity: Int,
    val feelsLike: Double,
    val timestamp: Long // Время сохранения данных
)