package com.example.stormy

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupActionBarWithNavController
import com.example.stormy.R

class MainActivity : AppCompatActivity() {

    private val navHostId = R.id.nav_host_fragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // toolbar
        val toolbar = findViewById<Toolbar>(R.id.main_toolbar)
        setSupportActionBar(toolbar)

        // безопасно получить NavController через NavHostFragment
        val navHostFragment =
            supportFragmentManager.findFragmentById(navHostId) as? NavHostFragment

        val navController = navHostFragment?.navController
            ?: throw IllegalStateException("NavHostFragment with id $navHostId not found. Check activity_main.xml")

        // подключаем toolbar к NavController
        setupActionBarWithNavController(navController)

        // (не нужно вручную replace стартового фрагмента — NavHostFragment это сделает по nav_graph)
    }
    override fun onCreateOptionsMenu(menu: android.view.Menu?): Boolean {
        // Убедись, что файл в res/menu называется именно main_menu.xml
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: android.view.MenuItem): Boolean {
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as? NavHostFragment
        val navController = navHostFragment?.navController

        return when (item.itemId) {
            R.id.action_settings -> {
                // ПРОВЕРКА: переходим только если мы сейчас НЕ в настройках
                if (navController?.currentDestination?.id != R.id.settingsFragment) {
                    navController?.navigate(R.id.action_weatherFragment_to_settingsFragment)
                }
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        val navHostFragment =
            supportFragmentManager.findFragmentById(navHostId) as? NavHostFragment
        val navController = navHostFragment?.navController
        return navController?.navigateUp() ?: super.onSupportNavigateUp()
    }
}