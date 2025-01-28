package com.ezgiyilmaz.wallpapermanager.pages

import WallpapersAdapter
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import com.ezgiyilmaz.wallpapermanager.RetrofitService
import com.ezgiyilmaz.wallpapermanager.databinding.ActivityHomePageBinding
import com.ezgiyilmaz.wallpapermanager.model.Wallpaper
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HomePage : AppCompatActivity() {
    private lateinit var binding: ActivityHomePageBinding
    private lateinit var adapter: WallpapersAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomePageBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.recyclerView.layoutManager = GridLayoutManager(this, 3)

        RetrofitService.instance.getWallpapers("your_key", 18)
            .enqueue(object : Callback<List<Wallpaper>> {
                override fun onResponse(
                    call: Call<List<Wallpaper>>,
                    response: Response<List<Wallpaper>>
                ) {
                    if (response.isSuccessful) {
                        response.body()?.let {
                            adapter = WallpapersAdapter(it)
                            binding.recyclerView.adapter = adapter
                        }
                    }
                }
                override fun onFailure(call: Call<List<Wallpaper>>, t: Throwable) {
                    Log.e("MainActivity", "Error: ${t.message}")
                }
            })
    }
}