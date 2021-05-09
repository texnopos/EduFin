package uz.texnopos.texnoposedufinance.ui.main.group.select

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import uz.texnopos.texnoposedufinance.R
import uz.texnopos.texnoposedufinance.core.BaseAdapter
import uz.texnopos.texnoposedufinance.core.extentions.inflate
import uz.texnopos.texnoposedufinance.data.model.Student
import uz.texnopos.texnoposedufinance.databinding.ItemStudentBinding

class SelectStudentsAdapter: BaseAdapter<Student, SelectStudentsAdapter.SelectStudentsViewHolder>(){
    inner class SelectStudentsViewHolder(private val binding: ItemStudentBinding): RecyclerView.ViewHolder(binding.root){
        fun populateModel(model: Student){
            binding.apply {
                tvStudentName.text = model.name
                tvInterestedCourse.text = model.interested
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SelectStudentsViewHolder {
        val itemView = parent.inflate(R.layout.item_student)
        val binding = ItemStudentBinding.bind(itemView)
        return SelectStudentsViewHolder(binding)
    }

    override fun onBindViewHolder(holder: SelectStudentsViewHolder, position: Int) {
        holder.populateModel(models[position])
    }
}