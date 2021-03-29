package uz.texnopos.texnoposedufinance.ui.main.group

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import uz.texnopos.texnoposedufinance.R
import uz.texnopos.texnoposedufinance.core.BaseAdapter
import uz.texnopos.texnoposedufinance.core.extentions.inflate
import uz.texnopos.texnoposedufinance.data.model.Group
import uz.texnopos.texnoposedufinance.databinding.ItemGroupBinding

class GroupAdapter: BaseAdapter<Group, GroupAdapter.GroupViewHolder>(){
    inner class GroupViewHolder(val binding: ItemGroupBinding): RecyclerView.ViewHolder(binding.root){
        fun populateModel(model: Group){
            binding.name.text = model.id
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GroupViewHolder {
        val itemView = parent.inflate(R.layout.item_group)
        val binding = ItemGroupBinding.bind(itemView)
        return GroupViewHolder(binding)
    }

    override fun onBindViewHolder(holder: GroupViewHolder, position: Int) {
        holder.populateModel(models[position])
    }
}