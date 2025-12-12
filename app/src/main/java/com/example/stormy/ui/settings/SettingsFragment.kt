package com.example.stormy.ui.settings

import android.os.Bundle
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.Fragment
import android.view.*
import android.widget.Switch
import com.example.stormy.R

class SettingsFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_settings, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val themeSwitch = view.findViewById<androidx.appcompat.widget.SwitchCompat>(R.id.switch_theme) ?:
        view.findViewById<android.widget.Switch>(R.id.switch_theme)

        // Устанавливаем положение свитча в зависимости от текущей темы
        val isNightMode = AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES
        themeSwitch?.isChecked = isNightMode

        themeSwitch?.setOnCheckedChangeListener { _, checked ->
            if (checked) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            }
        }
    }
}
