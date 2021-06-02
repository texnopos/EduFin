package uz.texnopos.texnoposedufinance.ui.main.category.income

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import uz.texnopos.texnoposedufinance.R
import uz.texnopos.texnoposedufinance.core.BaseAdapter
import uz.texnopos.texnoposedufinance.core.extentions.inflate
import uz.texnopos.texnoposedufinance.data.model.IncomeCategory
import uz.texnopos.texnoposedufinance.databinding.ItemCategoryBinding

class IncomeCategoryAdapter: BaseAdapter<IncomeCategory, IncomeCategoryAdapter.IncomeCategoryVH>(){
    inner class IncomeCategoryVH(private val binding: ItemCategoryBinding): RecyclerView.ViewHolder(binding.root){
        fun populateModel(model: IncomeCategory){
            binding.apply {
                tvCategoryName.text = model.name
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): IncomeCategoryVH {
        val itemView = parent.inflate(R.layout.item_category)
        val binding = ItemCategoryBinding.bind(itemView)
        return IncomeCategoryVH(binding)
    }

    override fun onBindViewHolder(holder: IncomeCategoryVH, position: Int) {
        holder.populateModel(models[position])
    }
}