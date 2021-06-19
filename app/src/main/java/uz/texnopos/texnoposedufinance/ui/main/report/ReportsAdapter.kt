package uz.texnopos.texnoposedufinance.ui.main.report

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.anychart.chart.common.dataentry.DataEntry
import uz.texnopos.texnoposedufinance.R
import uz.texnopos.texnoposedufinance.core.BaseAdapter
import uz.texnopos.texnoposedufinance.core.extentions.inflate
import uz.texnopos.texnoposedufinance.data.AllReports
import uz.texnopos.texnoposedufinance.data.model.Report
import uz.texnopos.texnoposedufinance.databinding.ItemAboutBinding
import uz.texnopos.texnoposedufinance.databinding.ItemReportBinding
import uz.texnopos.texnoposedufinance.databinding.ItemStudentBinding

class ReportsAdapter: BaseAdapter<AllReports, ReportsAdapter.ReportsViewHolder>(){
    inner class ReportsViewHolder(private val binding: ItemReportBinding): RecyclerView.ViewHolder(binding.root){
        fun populateModel(model: AllReports){
            binding.apply {
                tvCategory.text = root.context.getString(R.string.reportCategory, model.category)
                tvAmount.text = root.context.getString(R.string.amount, model.amount)
                tvTrans.text =  root.context.getString(R.string.trans, model.trans)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReportsViewHolder {
        val itemView = parent.inflate(R.layout.item_report)
        val binding = ItemReportBinding.bind(itemView)
        return ReportsViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ReportsViewHolder, position: Int) {
        holder.populateModel(models[position])
    }
}
