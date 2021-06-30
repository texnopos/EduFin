package uz.texnopos.texnoposedufinance.ui.main.report.expense

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import uz.texnopos.texnoposedufinance.R
import uz.texnopos.texnoposedufinance.core.BaseAdapter
import uz.texnopos.texnoposedufinance.core.extentions.inflate
import uz.texnopos.texnoposedufinance.data.model.Report
import uz.texnopos.texnoposedufinance.databinding.ItemTransBinding

class ExpenseTransAdapter  : BaseAdapter<Report, ExpenseTransAdapter.ExpenseViewHolder>(){
    inner class ExpenseViewHolder(private val binding: ItemTransBinding): RecyclerView.ViewHolder(binding.root){
        fun populateModel(model: Report){
            binding.apply {
                tvCategory.text = root.context.getString(R.string.reportCategory, model.note)
                val amount = textFormat(model.amount.toString())
                tvAmount.text = root.context.getString(R.string.amount, amount)
                tvTime.text = convertLongString(model.date)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ExpenseViewHolder {
        val itemView = parent.inflate(R.layout.item_trans)
        val binding = ItemTransBinding.bind(itemView)
        return ExpenseViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ExpenseViewHolder, position: Int) {
        holder.populateModel(models[position])
    }
}
