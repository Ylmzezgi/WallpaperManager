package com.ezgiyilmaz.wallpapermanager.adapters

import android.appwidget.AppWidgetProviderInfo
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.ezgiyilmaz.wallpapermanager.R

class WidgetAdapter(
    private val widgets: List<AppWidgetProviderInfo>,
    private val onWidgetClick: (AppWidgetProviderInfo) -> Unit
) : RecyclerView.Adapter<WidgetAdapter.WidgetViewHolder>() {

    class WidgetViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val widgetName: TextView = itemView.findViewById(R.id.widgetName)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WidgetViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.widget_layout, parent, false)
        return WidgetViewHolder(view)
    }

    override fun onBindViewHolder(holder: WidgetViewHolder, position: Int) {
        val widget = widgets[position]
        holder.widgetName.text = widget.label
        holder.itemView.setOnClickListener { onWidgetClick(widget) }
    }

    override fun getItemCount() = widgets.size
}
