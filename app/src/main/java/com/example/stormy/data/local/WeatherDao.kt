package com.example.stormy.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface WeatherDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertWeather(weather: WeatherEntity)

    @Query("SELECT * FROM weather_cache WHERE city = :city")
    fun getWeather(city: String): Flow<WeatherEntity?>

    @Query("DELETE FROM weather_cache WHERE city = :city")
    suspend fun deleteWeather(city: String)
}