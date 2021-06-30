package uz.texnopos.texnoposedufinance.ui.main.report.expense

import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.RecyclerView
import uz.texnopos.texnoposedufinance.R
import uz.texnopos.texnoposedufinance.core.BaseAdapter
import uz.texnopos.texnoposedufinance.core.extentions.inflate
import uz.texnopos.texnoposedufinance.core.extentions.onClick
import uz.texnopos.texnoposedufinance.core.extentions.visibility
import uz.texnopos.texnoposedufinance.data.AllReports
import uz.texnopos.texnoposedufinance.databinding.ItemReportExpBinding

class ReportExpAdapter: BaseAdapter<AllReports, ReportExpAdapter.ReportsViewHolder>(){
    private var onItemClick: (String) -> Unit = {}
    fun setOnItemClickListener(onItemClick: (String) -> Unit) {
        this.onItemClick = onItemClick
    }

    inner class ReportsViewHolder(private val binding: ItemReportExpBinding): RecyclerView.ViewHolder(binding.root){
        fun populateModel(model: AllReports){
            binding.apply {
                tvCategory.text = root.context.getString(R.string.reportCategory, model.category)
                val amount = textFormat(model.amount.toString())
                tvAmount.text = root.context.getString(R.string.amount, amount)
                tvTrans.text =  root.context.getString(R.string.trans, model.trans)
                rcvItemReport.addItemDecoration(DividerItemDecoration(root.context, DividerItemDecoration.VERTICAL))
                if(model.expenses.isNotEmpty()){
                    clItemReport.onClick {
                        val myExpAdapter = ExpenseTransAdapter()
                        rcvItemReport.adapter = myExpAdapter
                        myExpAdapter.models = model.expenses
                        if(rcvItemReport.visibility == View.GONE){
                            rcvItemReport.visibility(true)
                        }
                        else{
                            rcvItemReport.visibility(false)
                        }
                    }
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReportsViewHolder {
        val itemView = parent.inflate(R.layout.item_report_exp)
        val binding = ItemReportExpBinding.bind(itemView)
        return ReportsViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ReportsViewHolder, position: Int) {
        holder.populateModel(models[position])
    }
}
