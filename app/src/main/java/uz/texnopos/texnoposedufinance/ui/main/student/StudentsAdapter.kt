package uz.texnopos.texnoposedufinance.ui.main.student

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import uz.texnopos.texnoposedufinance.R
import uz.texnopos.texnoposedufinance.core.BaseAdapter
import uz.texnopos.texnoposedufinance.core.extentions.inflate
import uz.texnopos.texnoposedufinance.data.model.Student
import uz.texnopos.texnoposedufinance.databinding.ItemGroupInfoBinding

class StudentsAdapter : BaseAdapter<Student, StudentsAdapter.GroupInfoViewHolder>() {

    var onItemClick: (id: String) -> Unit = {}
    fun setOnItemClicked(onItemClick: (id: String) -> Unit) {
        this.onItemClick = onItemClick
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GroupInfoViewHolder {
        val itemView = parent.inflate(R.layout.item_group_info)
        val binding = ItemGroupInfoBinding.bind(itemView)
        return GroupInfoViewHolder(binding)
    }

    override fun onBindViewHolder(holder: GroupInfoViewHolder, position: Int) {
        holder.populateModel(models[position])
    }

    inner class GroupInfoViewHolder(val binding: ItemGroupInfoBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun populateModel(model: Student) {
            binding.apply {
                tvName.text = model.name
            }
        }
    }
}