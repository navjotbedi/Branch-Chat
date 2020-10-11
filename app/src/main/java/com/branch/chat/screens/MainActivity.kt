package com.branch.chat.screens

import android.content.Intent
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.branch.chat.R
import com.branch.chat.core.AppDatabase
import com.branch.chat.utils.PreferenceManager
import com.branch.chat.utils.URL_CODE
import com.branch.chat.utils.URL_DEVELOPER
import com.branch.chat.utils.Utils
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity(R.layout.activity_main) {

    @Inject
    lateinit var utils: Utils

    @Inject
    lateinit var preferenceManager: PreferenceManager

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_action, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.logoutMenu -> {
                GlobalScope.launch(Dispatchers.IO)
                {
                    AppDatabase.getInstance(this@MainActivity).clearAllTables()
                    preferenceManager.authToken = null
                    startActivity(Intent(this@MainActivity, LoginActivity::class.java))
                    finish()
                }
                true
            }
            R.id.developerMenu -> {
                utils.openUrl(URL_DEVELOPER)
                true
            }
            R.id.viewCodeMenu -> {
                utils.openUrl(URL_CODE)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}