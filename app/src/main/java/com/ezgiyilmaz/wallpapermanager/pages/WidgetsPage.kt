package com.ezgiyilmaz.wallpapermanager.pages

import android.appwidget.AppWidgetHost
import android.appwidget.AppWidgetManager
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.ezgiyilmaz.wallpapermanager.R
import com.ezgiyilmaz.wallpapermanager.databinding.ActivityWidgetsPageBinding

class WidgetsPage : AppCompatActivity() {
    private lateinit var binding: ActivityWidgetsPageBinding
    private lateinit var appWidgetManager: AppWidgetManager
    private lateinit var appWidgetHost: AppWidgetHost
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityWidgetsPageBinding.inflate(layoutInflater)
        setContentView(binding.root)
        enableEdgeToEdge()
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        appWidgetManager = AppWidgetManager.getInstance(this) // widget nesnesi
        appWidgetHost = AppWidgetHost(this, 1) //widgetları barındırır


        binding.widgetButton.setOnClickListener{
            pickWidget()
        }
    }
    companion object {
        const val PIN_WIDGET_REQUEST_CODE = 1001
    }


    private fun pickWidget() {
        val appWidgetId = appWidgetHost.allocateAppWidgetId() // widgetlar için id oluşturur
        val pickIntent = Intent(AppWidgetManager.ACTION_APPWIDGET_PICK) //kullanıcıya widget seçimi yapabilmesi için widget seçme ekranını açar.
        pickIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId)
        startActivityForResult(pickIntent, PIN_WIDGET_REQUEST_CODE)
    }
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PIN_WIDGET_REQUEST_CODE && resultCode == RESULT_OK) {
            val appWidgetId = data?.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, -1) ?: return
            requestPinWidget(appWidgetId)
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun requestPinWidget(appWidgetId: Int) {
        val appWidgetProviderInfo = appWidgetManager.getAppWidgetInfo(appWidgetId)

        if (appWidgetProviderInfo != null) {
            val success = appWidgetManager.requestPinAppWidget(appWidgetProviderInfo.provider, null, null)
            if (success) {
                Toast.makeText(this, "Widget Ana Ekrana Eklendi!", Toast.LENGTH_SHORT).show()
            } else {
                Log.e("WidgetError", "Widget eklenemedi, success değeri false.")
                Toast.makeText(this, "Widget eklenemedi!", Toast.LENGTH_SHORT).show()
            }
        } else {
            Log.e("WidgetError", "Widget bilgisi alınamadı. appWidgetProviderInfo null.")
            Toast.makeText(this, "Widget bilgisi alınamadı!", Toast.LENGTH_SHORT).show()
        }
    }

}