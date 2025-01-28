import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.ezgiyilmaz.wallpapermanager.R
import com.ezgiyilmaz.wallpapermanager.model.Wallpaper

class WallpapersAdapter(private val wallpapers: List<Wallpaper>) :
    RecyclerView.Adapter<WallpapersAdapter.WallpaperViewHolder>() {

    class WallpaperViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imageView: ImageView = itemView.findViewById(R.id.imageView)
        val titleView:TextView=itemView.findViewById(R.id.title)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WallpaperViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_wallpaper, parent, false)
        return WallpaperViewHolder(view)
    }

    override fun onBindViewHolder(holder: WallpaperViewHolder, position: Int) {
        val wallpaper = wallpapers[position]
        holder.titleView.text = wallpaper.title
        Glide.with(holder.itemView.context).load(wallpaper.url).into(holder.imageView)
    }

    override fun getItemCount(): Int = wallpapers.size
}
