package uz.texnopos.texnoposedufinance.ui.main.student

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import uz.texnopos.texnoposedufinance.R
import uz.texnopos.texnoposedufinance.core.BaseAdapter
import uz.texnopos.texnoposedufinance.core.extentions.inflate
import uz.texnopos.texnoposedufinance.data.model.Student
import uz.texnopos.texnoposedufinance.databinding.ItemGroupInfoBinding
import uz.texnopos.texnoposedufinance.databinding.ItemStudentBinding

class StudentsAdapter : BaseAdapter<Student, StudentsAdapter.StudentViewHolder>() {

    var onItemClick: (id: String) -> Unit = {}
    fun setOnItemClicked(onItemClick: (id: String) -> Unit) {
        this.onItemClick = onItemClick
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StudentViewHolder {
        val itemView = parent.inflate(R.layout.item_student)
        val binding = ItemStudentBinding.bind(itemView)
        return StudentViewHolder(binding)
    }

    override fun onBindViewHolder(holder: StudentViewHolder, position: Int) {
        holder.populateModel(models[position])
    }

    inner class StudentViewHolder(val binding: ItemStudentBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun populateModel(model: Student) {
            binding.apply {
                tvStudentName.text = model.name
                tvInterestedCourse.text = model.interested
            }
        }
    }
}