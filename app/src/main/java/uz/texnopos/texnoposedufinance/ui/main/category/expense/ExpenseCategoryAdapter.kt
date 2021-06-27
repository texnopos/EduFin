package uz.texnopos.texnoposedufinance.ui.main.category.expense

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import uz.texnopos.texnoposedufinance.R
import uz.texnopos.texnoposedufinance.core.BaseAdapter
import uz.texnopos.texnoposedufinance.core.extentions.inflate
import uz.texnopos.texnoposedufinance.core.extentions.onClick
import uz.texnopos.texnoposedufinance.data.model.ExpenseCategory
import uz.texnopos.texnoposedufinance.databinding.ItemCategoryBinding

class ExpenseCategoryAdapter :
    BaseAdapter<ExpenseCategory, ExpenseCategoryAdapter.ExpenseCategoryVH>() {
    private var onItemClicked: (name: String) -> Unit = {}
    fun setOnItemClickListener(onGroupItemClicked: (name: String) -> Unit) {
        this.onItemClicked = onGroupItemClicked
    }

    fun onModified(data: ExpenseCategory) {
        val prev = models.find { it.id == data.id }!!
        val index = models.indexOf(prev)
        val list = models.toMutableList()
        list[index] = data
        models = list
        notifyItemChanged(index)
    }

    inner class ExpenseCategoryVH(private val binding: ItemCategoryBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun populateModel(model: ExpenseCategory) {
            binding.apply {
                tvCategoryName.text = model.name
                llCategory.onClick {
                    onItemClicked.invoke(model.name)
                }
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