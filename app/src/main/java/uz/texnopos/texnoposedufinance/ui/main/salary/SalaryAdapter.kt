package uz.texnopos.texnoposedufinance.ui.main.salary

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import uz.texnopos.texnoposedufinance.R
import uz.texnopos.texnoposedufinance.core.BaseAdapter
import uz.texnopos.texnoposedufinance.core.extentions.inflate
import uz.texnopos.texnoposedufinance.core.extentions.onClick
import uz.texnopos.texnoposedufinance.data.model.Teacher
import uz.texnopos.texnoposedufinance.databinding.ItemTeacherBinding

class SalaryAdapter: BaseAdapter<Teacher, SalaryAdapter.SalaryViewHolder>() {
    inner class SalaryViewHolder(private val binding: ItemTeacherBinding): RecyclerView.ViewHolder(binding.root){
        fun populateModel(model: Teacher){
            binding.apply {
                clItemTeacher.onClick {
                    onItemClick.invoke(model.id)
                }
            }
        }
    }
    private var onItemClick: (String) -> Unit = {}
    fun setOnItemClickListener(onItemClick: (String) -> Unit) {
        this.onItemClick = onItemClick
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SalaryViewHolder {
        val itemView = parent.inflate(R.layout.item_teacher)
        val binding = ItemTeacherBinding.bind(itemView)
        return SalaryViewHolder(binding)
    }

    override fun onBindViewHolder(holder: SalaryViewHolder, position: Int) {
        holder.populateModel(models[position])
    }
}
