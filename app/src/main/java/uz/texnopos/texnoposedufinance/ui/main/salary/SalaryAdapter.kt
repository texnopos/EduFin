package uz.texnopos.texnoposedufinance.ui.main.salary

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import uz.texnopos.texnoposedufinance.R
import uz.texnopos.texnoposedufinance.core.BaseAdapter
import uz.texnopos.texnoposedufinance.core.extentions.inflate
import uz.texnopos.texnoposedufinance.core.extentions.onClick
import uz.texnopos.texnoposedufinance.data.model.Teacher
import uz.texnopos.texnoposedufinance.data.model.response.EmployeeResponse
import uz.texnopos.texnoposedufinance.data.model.response.ExpenseRequest
import uz.texnopos.texnoposedufinance.data.model.response.SalaryResponse
import uz.texnopos.texnoposedufinance.databinding.ItemSalaryBinding
import uz.texnopos.texnoposedufinance.databinding.ItemTeacherBinding

class SalaryAdapter: BaseAdapter<EmployeeResponse, SalaryAdapter.SalaryViewHolder>() {
    inner class SalaryViewHolder(private val binding: ItemSalaryBinding): RecyclerView.ViewHolder(binding.root){
        fun populateModel(model: EmployeeResponse){
            binding.apply {
                val eSalary = mutableListOf<ExpenseRequest>()
                var sum = 0
                clItemTeacher.onClick {
                    onItemClick.invoke(model.id)
                }
                tvEmployeeName.text = model.name
                model.mySalary.forEach { e ->
                    eSalary.add(e)
                }
                eSalary.forEach {m ->
                    sum += m.amount
                }
                val amount = textFormat(sum.toString())
                tvSalary.text = root.context.getString(R.string.amount, amount)
            }
        }
    }
    private var onItemClick: (String) -> Unit = {}
    fun setOnItemClickListener(onItemClick: (String) -> Unit) {
        this.onItemClick = onItemClick
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SalaryViewHolder {
        val itemView = parent.inflate(R.layout.item_salary)
        val binding = ItemSalaryBinding.bind(itemView)
        return SalaryViewHolder(binding)
    }

    override fun onBindViewHolder(holder: SalaryViewHolder, position: Int) {
        holder.populateModel(models[position])
    }
}
