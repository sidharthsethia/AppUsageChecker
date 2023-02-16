package com.android.cyberdivetest.ui.views.activities

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.android.cyberdivetest.services.AppUsageCheckService

/**
 * Created by Sidharth Sethia on 16/02/23.
 */
class KillForegroundActivity : AppCompatActivity() {

    companion object {
        fun newIntent(context: Context) = Intent(context, KillForegroundActivity::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val intent = AppUsageCheckService.newLeaveAppIntent(this)
        ContextCompat.startForegroundService(this, intent)
        val homeIntent = Intent(Intent.ACTION_MAIN)
        homeIntent.addCategory(Intent.CATEGORY_HOME)
        startActivity(homeIntent)
        finish()
    }
}