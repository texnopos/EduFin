package uz.texnopos.texnoposedufinance.core

import androidx.recyclerview.widget.RecyclerView

abstract class BaseAdapter<T, VH : RecyclerView.ViewHolder> : RecyclerView.Adapter<VH>() {

    var models: List<T> = listOf()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    fun onAdded(data: T) {
        val list = models.toMutableList()
        list.add(data)
        models = list
        notifyItemInserted(list.lastIndex)
    }
    fun onRemoved(data: T) {
        val index = models.indexOf(data)
        val list = models.toMutableList()
        list.removeAt(index)
        models = list
        notifyItemRemoved(index)
    }
    fun update() {
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int = models.size
}