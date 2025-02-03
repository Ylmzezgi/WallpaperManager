package com.ezgiyilmaz.wallpapermanager.pages

import WallpapersAdapter
import android.app.WallpaperManager
import android.graphics.drawable.BitmapDrawable
import android.os.Bundle
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.bumptech.glide.Glide
import com.ezgiyilmaz.wallpapermanager.R
import com.ezgiyilmaz.wallpapermanager.databinding.ActivityDetailsPageBinding
import com.ezgiyilmaz.wallpapermanager.databinding.ActivityHomePageBinding
import java.io.IOException

class DetailsPage : AppCompatActivity() {
    private lateinit var binding: ActivityDetailsPageBinding
    private lateinit var adapter: WallpapersAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailsPageBinding.inflate(layoutInflater)
        setContentView(binding.root)
        enableEdgeToEdge()
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val walpaperıd = intent.getStringExtra("wallpaperId")
        val title = intent.getStringExtra("name")


        Glide.with(this)
            .load(walpaperıd)
            .into(binding.detailImage)


        binding.detailImage.setOnClickListener {
            showAlertDialog()
        }

    }

    private fun showAlertDialog() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Ne yapmak istiyorsunuz?")
        builder.setMessage("Bir seçenek seçin")

        builder.setPositiveButton("Ana Ekran Fotoğrafı Yap") { dialog, which ->
            setWallpaper(binding.detailImage,false)
        }
        builder.setNeutralButton("Kilit Ekranı Fotoğrafı Yap") { dialog, which ->
            setWallpaper(binding.detailImage,true)
        }

        builder.setNegativeButton("İptal") { dialog, which ->
            dialog.dismiss()
        }

        val dialog = builder.create()
        dialog.show()
    }

    private fun setWallpaper(imageView: ImageView, isLockScreen: Boolean = false) {
        try {
            // Görseli ImageView'den Bitmap olarak al
            val bitmap = (imageView.drawable as BitmapDrawable).bitmap

            // WallpaperManager instance'ını al
            val wallpaperManager = WallpaperManager.getInstance(applicationContext)

            // Ana ekran duvar kağıdını ayarla
            if (isLockScreen) {
                wallpaperManager.setBitmap(bitmap, null, true) // Kilit ekranı için
            } else {
                wallpaperManager.setBitmap(bitmap) // Ana ekran için
            }

            val message = if (isLockScreen) {
                "Kilit ekranı duvar kağıdı başarıyla ayarlandı!"
            } else {
                "Duvar kağıdı başarıyla ayarlandı!"
            }
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show()

        } catch (e: IOException) {
            Toast.makeText(this, "Duvar kağıdı ayarlanırken hata oluştu!", Toast.LENGTH_SHORT).show()
            e.printStackTrace()
        }
    }
    }