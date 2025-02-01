package com.ezgiyilmaz.wallpapermanager.pages

import WallpapersAdapter
import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.GridLayoutManager
import com.ezgiyilmaz.wallpapermanager.service.RetrofitService
import com.ezgiyilmaz.wallpapermanager.databinding.ActivityHomePageBinding
import com.ezgiyilmaz.wallpapermanager.model.Wallpaper
import com.google.android.material.snackbar.Snackbar
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HomePage : AppCompatActivity() {
    private lateinit var binding: ActivityHomePageBinding
    private lateinit var adapter: WallpapersAdapter
    private var selectedPicture: Uri? = null
    private var list = ArrayList<Wallpaper>()

    private lateinit var activityResultLauncher: ActivityResultLauncher<Intent>
    private lateinit var permissionLauncher: ActivityResultLauncher<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomePageBinding.inflate(layoutInflater)
        setContentView(binding.root)

        registerLauncher()
        binding.widgethButton.setOnClickListener{
            startActivity(Intent(this,WidgetsPage::class.java))
        }

        binding.recyclerView.layoutManager = GridLayoutManager(this, 3)

        RetrofitService.instance.getWallpapers("key",18)
            .enqueue(object : Callback<List<Wallpaper>> {
                override fun onResponse(
                    call: Call<List<Wallpaper>>,
                    response: Response<List<Wallpaper>>
                ) {
                    if (response.isSuccessful) {
                        response.body()?.let { wallpapers ->
                            val photoWallpapers = wallpapers.filter {
                                it.url!!.endsWith(".jpg") || it.url.endsWith(".png") || it.url.endsWith(".jpeg")
                            }

                            if (photoWallpapers.isNotEmpty()) {
                                adapter = WallpapersAdapter(photoWallpapers)
                                binding.recyclerView.adapter = adapter
                            } else {
                                Log.e("HomePage", "Gelen veriler fotoğraf değil.")
                            }
                        }
                    }

                }
                override fun onFailure(call: Call<List<Wallpaper>>, t: Throwable) {
                    Log.e("MainActivity", "Error: ${t.message}")
                }
            })
    }

    fun galleryOnClick(view:View){
        val requiredPermission = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            Manifest.permission.READ_MEDIA_IMAGES
        } else {
            Manifest.permission.READ_EXTERNAL_STORAGE
        }

        if (ContextCompat.checkSelfPermission(
                this,
                requiredPermission
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, requiredPermission)) {
                // İzin açıklaması göster
                Snackbar.make(
                    binding.root,
                    "Galeriye erişim izni gerekli",
                    Snackbar.LENGTH_INDEFINITE
                )
                    .setAction("İzin Ver") {
                        permissionLauncher.launch(requiredPermission)
                    }.show()
            } else {
                // İzin isteme işlemi başlat
                permissionLauncher.launch(requiredPermission)
            }
        } else {
            openGallery()
        }


    }

    private fun openGallery() {
        val intentToGallery = Intent(Intent.ACTION_OPEN_DOCUMENT)
        intentToGallery.addCategory(Intent.CATEGORY_OPENABLE)
        intentToGallery.type = "image/*"
        activityResultLauncher.launch(intentToGallery)

    }

    private fun registerLauncher() {
        activityResultLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                if (result.resultCode == RESULT_OK) {
                    val intentFromResult = result.data
                    intentFromResult?.data?.let { uri ->
                        // URI'yi kontrol et
                        Log.d("HomePage", "Seçilen resim URI: $uri")
                        selectedPicture = uri

                        val lis = Wallpaper(
                            url = selectedPicture.toString()
                        )
                        list.add(lis)

                            Log.d("HomePage", "Yeni resim listeye eklendi: ${lis.url}")
                            adapter = WallpapersAdapter(list)
                            binding.recyclerView.adapter = adapter


                        Log.d("HomePage", "RecyclerView adapter'ı güncellendi.")
                    } ?: run {
                        Log.e("HomePage", "Seçilen resmin URI'si alınamadı.")
                        Toast.makeText(this,"Seçilen resmin URI'si alınamadı",Toast.LENGTH_LONG).show()
                    }
                } else {
                    Log.e("HomePage", "Galeriden resim seçilemedi. ResultCode: ${result.resultCode}")
                    Toast.makeText(this,"Galeriden resim seçilemedi. ResultCode: ${result.resultCode}",Toast.LENGTH_LONG).show()

                }
            }

        permissionLauncher =
            registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
                if (isGranted) {
                    openGallery()
                } else {
                    Toast.makeText(this, "İzin verilmedi", Toast.LENGTH_LONG).show()
                }
            }
    }

}