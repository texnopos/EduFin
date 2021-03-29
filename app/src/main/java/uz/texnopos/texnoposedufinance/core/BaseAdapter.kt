package uz.texnopos.texnoposedufinance.core

import androidx.recyclerview.widget.RecyclerView

abstract class BaseAdapter<T, VH : RecyclerView.ViewHolder> : RecyclerView.Adapter<VH>() {

    var models: List<T> = listOf()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    fun update() {
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int = models.size
}