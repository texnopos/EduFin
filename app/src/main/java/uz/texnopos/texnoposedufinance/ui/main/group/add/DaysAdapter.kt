package uz.texnopos.texnoposedufinance.ui.main.group.add

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import uz.texnopos.texnoposedufinance.R
import uz.texnopos.texnoposedufinance.core.BaseAdapter
import uz.texnopos.texnoposedufinance.core.extentions.inflate
import uz.texnopos.texnoposedufinance.core.extentions.onClick
import uz.texnopos.texnoposedufinance.data.model.Day
import uz.texnopos.texnoposedufinance.databinding.ItemDaysBinding
import uz.texnopos.texnoposedufinance.databinding.ItemMonthBinding

class DaysAdapter: BaseAdapter<Day, DaysAdapter.DaysViewHolder>(){

    /*var onItemClick :(id: Int) -> Unit = {}
    fun setOnItemClicked(onItemClick: (id: Int) -> Unit){
        this.onItemClick = onItemClick
    }*/

    var selectedItem = -1
        set(value) {
            if (field != -1) {
                notifyItemChanged(field)
            }
            field = value
            notifyItemChanged(value)
        }

    inner class DaysViewHolder(val binding: ItemDaysBinding, private val adapter: DaysAdapter): RecyclerView.ViewHolder(binding.root){
        fun populateModel(day: Day, position: Int){
            binding.apply {
                days.text = day.day
                if(day.selected) selected.visibility = View.VISIBLE
                else selected.visibility = View.GONE

                clDays.onClick {
                    if (adapter.selectedItem != -1) {
                        adapter.selectedItem = -1
                        selected.visibility = View.GONE
                        day.selected = false
                        //adapter.models[adapter.selectedItem].selected = false

                    }
                    else{
                        adapter.selectedItem = position
                        selected.visibility = View.VISIBLE
                        day.selected = true
                    }
                    var s = ""
                    when(position){
                        0 -> s+= "1"
                        1 -> s+= "2"
                        2 -> s+= "3"
                        3 -> s+= "4"
                        4 -> s+= "5"
                        5 -> s+= "6"
                        6 -> s+= "7"
                    }
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DaysViewHolder {
        val itemView = parent.inflate(R.layout.item_days)
        val binding = ItemDaysBinding.bind(itemView)
        return DaysViewHolder(binding, this)
    }

    override fun onBindViewHolder(holder: DaysViewHolder, position: Int) {
        holder.populateModel(models[position], position)
    }
}