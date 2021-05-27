package uz.texnopos.texnoposedufinance.ui.main.category.expense

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import uz.texnopos.texnoposedufinance.R
import uz.texnopos.texnoposedufinance.core.BaseAdapter
import uz.texnopos.texnoposedufinance.core.extentions.inflate
import uz.texnopos.texnoposedufinance.data.model.ExpenseCategory
import uz.texnopos.texnoposedufinance.databinding.ItemCategoryBinding

class ExpenseCategoryAdapter: BaseAdapter<ExpenseCategory, ExpenseCategoryAdapter.ExpenseCategoryVH>(){
    inner class ExpenseCategoryVH(private val binding: ItemCategoryBinding): RecyclerView.ViewHolder(binding.root){
        fun populateModel(model: ExpenseCategory){
            binding.apply {
                tvCategoryName.text = model.name
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ExpenseCategoryVH {
        val itemView = parent.inflate(R.layout.item_category)
        val binding = ItemCategoryBinding.bind(itemView)
        return ExpenseCategoryVH(binding)
    }

    override fun onBindViewHolder(holder: ExpenseCategoryVH, position: Int) {
        holder.populateModel(models[position])
    }
}