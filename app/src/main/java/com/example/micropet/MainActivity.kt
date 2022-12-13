package com.example.micropet

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toolbar
import androidx.annotation.MenuRes
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.view.SupportMenuInflater
import androidx.appcompat.view.menu.MenuBuilder
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.onNavDestinationSelected
import androidx.navigation.ui.setupActionBarWithNavController
import com.example.micropet.databinding.ActivityMainBinding
import java.security.AccessController.getContext


class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var controller: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        init()
        enableToolBar()

    }

    private fun init(){
        controller = findNavController(R.id.fragmentContainerView)
    }

    private fun enableToolBar(){
        setSupportActionBar(binding.include.toolbar)
        val config = AppBarConfiguration(controller.graph)
        setupActionBarWithNavController(controller)
    }

    //подключает стрелку
    override fun onSupportNavigateUp(): Boolean {
        return super.onSupportNavigateUp() || controller.navigateUp()
    }

    //подключает меню
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        //т.к. id элемента "+" такой же как и название фрагмента то оно работает
        return item.onNavDestinationSelected(controller) || super.onOptionsItemSelected(item)
    }
}

