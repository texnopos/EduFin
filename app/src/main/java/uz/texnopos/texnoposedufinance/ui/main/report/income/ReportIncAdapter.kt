package uz.texnopos.texnoposedufinance.ui.main.report.income

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.RecyclerView
import uz.texnopos.texnoposedufinance.R
import uz.texnopos.texnoposedufinance.core.BaseAdapter
import uz.texnopos.texnoposedufinance.core.extentions.inflate
import uz.texnopos.texnoposedufinance.core.extentions.onClick
import uz.texnopos.texnoposedufinance.core.extentions.visibility
import uz.texnopos.texnoposedufinance.data.AllReports
import uz.texnopos.texnoposedufinance.databinding.ItemReportIncBinding

class ReportIncAdapter: BaseAdapter<AllReports, ReportIncAdapter.ReportsViewHolder>(){
    private var onItemClick: (String) -> Unit = {}
    fun setOnItemClickListener(onItemClick: (String) -> Unit) {
        this.onItemClick = onItemClick
    }

    inner class ReportsViewHolder(private val binding: ItemReportIncBinding): RecyclerView.ViewHolder(binding.root){
        fun populateModel(model: AllReports){
            binding.apply {
                tvCategory.text = root.context.getString(R.string.reportCategory, model.category)
                val amount = textFormat(model.amount.toString())
                tvAmount.text = root.context.getString(R.string.amount, amount)
                tvTrans.text =  root.context.getString(R.string.trans, model.trans)
                rcvItemReport.addItemDecoration(DividerItemDecoration(root.context, DividerItemDecoration.VERTICAL))
                clItemReport.onClick {
                    if(rcvItemReport.visibility == View.GONE){
                        rcvItemReport.visibility(true)
                    }
                    else{
                        rcvItemReport.visibility(false)
                    }
                    val myIncAdapter = IncomeTransAdapter()
                    rcvItemReport.adapter = myIncAdapter
                    myIncAdapter.models = model.incomes.sortedByDescending { it.date }
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReportsViewHolder {
        val itemView = parent.inflate(R.layout.item_report_inc)
        val binding = ItemReportIncBinding.bind(itemView)
        return ReportsViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ReportsViewHolder, position: Int) {
        holder.populateModel(models[position])
    }
}
