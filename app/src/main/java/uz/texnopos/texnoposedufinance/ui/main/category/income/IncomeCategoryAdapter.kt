
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
    fun onAdded(data: IncomeCategory) {
        val list = models.toMutableList()
        list.add(data)
        models = list
        notifyItemInserted(list.lastIndex)
    }
    fun onModified(data: IncomeCategory) {
        val prev = models.find { it.id == data.id }!!
        val index = models.indexOf(prev)
        val list = models.toMutableList()
        list[index] = data
        models = list
        notifyItemChanged(index)
    }
    fun onRemoved(data: IncomeCategory) {
        val index = models.indexOf(data)
        val list = models.toMutableList()
        list.removeAt(index)
        models = list
        notifyItemRemoved(index)
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