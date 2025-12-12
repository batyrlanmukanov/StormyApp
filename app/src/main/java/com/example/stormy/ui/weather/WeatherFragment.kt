package com.example.stormy.ui.weather


import android.widget.TextView
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.bumptech.glide.Glide
import com.example.stormy.databinding.FragmentWeatherBinding
import com.example.stormy.viewmodel.WeatherViewModel
import com.example.stormy.viewmodel.WeatherViewModelFactory

class WeatherFragment : Fragment() {

    private var _binding: FragmentWeatherBinding? = null
    private val binding get() = _binding!!

    private val viewModel: WeatherViewModel by viewModels {
        WeatherViewModelFactory(requireActivity().application)
    }

    // Твой API ключ
    private val apiKey = "674844c00c208fa9b921c3864883cd9b"

    // SharedPreferences (Offline Mode - 4 балла)
    private fun saveLastCity(city: String) {
        val sharedPref = requireActivity().getPreferences(android.content.Context.MODE_PRIVATE)
        with(sharedPref.edit()) {
            putString("last_city", city)
            apply()
        }
    }

    private fun getLastCity(): String {
        val sharedPref = requireActivity().getPreferences(android.content.Context.MODE_PRIVATE)
        return sharedPref.getString("last_city", "London") ?: "London"
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentWeatherBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val cityToLoad = getLastCity()

        setupSearch() // Настраиваем поиск
        observeViewModel()

        viewModel.loadWeather(cityToLoad, apiKey)
    }

    // Добавляем обработку ввода города
    private fun setupSearch() {
        // Если у тебя в XML есть EditText с id searchCity
        binding.searchCity?.setOnEditorActionListener { textView, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                val newCity = textView.text.toString().trim()
                if (newCity.isNotEmpty()) {
                    viewModel.loadWeather(newCity, apiKey)
                }
                true
            } else {
                false
            }
        }
    }

    private fun observeViewModel() {
        viewModel.weather.observe(viewLifecycleOwner) { weather ->
            if (weather != null) {
                // Данные из Room Database (Offline Mode - 7 баллов)
                binding.cityName.text = weather.city
                binding.temperature.text = "${weather.temperature}°C"
                binding.description.text = weather.description.replaceFirstChar { it.uppercase() }

                // Сохраняем последний удачный город в SharedPrefs
                saveLastCity(weather.city)

                // Загрузка иконки через Networking (Retrofit/Glide - 8 баллов)
                val iconUrl = "https://openweathermap.org/img/wn/${weather.iconCode}@4x.png"
                Glide.with(this)
                    .load(iconUrl)
                    .placeholder(android.R.drawable.progress_indeterminate_horizontal)
                    .error(android.R.drawable.stat_notify_error)
                    .into(binding.weatherIcon)

                binding.errorText.visibility = View.GONE
            } else {
                // Состояние, если база пуста
                binding.cityName.text = "Город"
                binding.temperature.text = "N/A"
                binding.description.text = "Загрузка..."
            }
        }

        viewModel.loading.observe(viewLifecycleOwner) { isLoading ->
            binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
        }

        viewModel.error.observe(viewLifecycleOwner) { error ->
            if (!error.isNullOrEmpty()) {
                binding.errorText.text = error
                binding.errorText.visibility = View.VISIBLE
            } else {
                binding.errorText.visibility = View.GONE
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}