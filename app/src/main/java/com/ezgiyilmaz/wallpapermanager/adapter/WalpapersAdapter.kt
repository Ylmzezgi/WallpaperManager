import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.ezgiyilmaz.wallpapermanager.R
import com.ezgiyilmaz.wallpapermanager.model.Wallpaper
import com.ezgiyilmaz.wallpapermanager.pages.DetailsPage

class WallpapersAdapter(private val wallpapers: List<Wallpaper>) :
    RecyclerView.Adapter<WallpapersAdapter.WallpaperViewHolder>() {

    class WallpaperViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imageView: ImageView = itemView.findViewById(R.id.imageView)
        fun bind(walpaper:Wallpaper){
            Log.d("TAG", "bind: "+walpaper)
            if (!walpaper.url.isNullOrEmpty()) {
                Glide.with(itemView.context)
                    .load(walpaper.url)
                    .into(imageView)
            } else {
                imageView.setImageResource(R.drawable.baseline_home_24)
            }

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WallpaperViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.item_wallpaper, parent, false)
        return WallpaperViewHolder(view)
    }

    override fun onBindViewHolder(holder: WallpaperViewHolder, position: Int) {

        val wallpaper = wallpapers[position]
        Log.d("TAG", "onBindViewHolder: girdi")
        if(wallpaper!=null){
            Log.d("TAG", "onBindViewHolder: "+wallpaper)
            holder.bind(wallpaper)
            holder.itemView.setOnClickListener {
                val context = holder.itemView.context
                val intent = Intent(context, DetailsPage::class.java)
                if (wallpaper is Wallpaper) {
                    val wallpaperId = wallpaper.url
                    Log.e("on", "onBindViewHolder: "+ wallpaperId)
                    intent.putExtra("wallpaperId",wallpaperId)
                    intent.putExtra("name",wallpaper.title)
                    println(wallpaperId)
                }
                context.startActivity(intent)
            }
        }
    }

    override fun getItemCount(): Int = wallpapers.size

}
