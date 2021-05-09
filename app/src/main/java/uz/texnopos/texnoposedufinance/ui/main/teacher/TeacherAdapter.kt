package uz.texnopos.texnoposedufinance.ui.main.teacher

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import uz.texnopos.texnoposedufinance.R
import uz.texnopos.texnoposedufinance.core.BaseAdapter
import uz.texnopos.texnoposedufinance.core.extentions.inflate
import uz.texnopos.texnoposedufinance.core.extentions.onClick
import uz.texnopos.texnoposedufinance.data.model.Teacher
import uz.texnopos.texnoposedufinance.databinding.ItemTeacherBinding


class TeacherAdapter :
    BaseAdapter<Teacher, TeacherAdapter.TeacherViewHolder>() {
    var onItemClick: (id: String) -> Unit = {}
    fun setOnItemClicked(onItemClick: (id: String) -> Unit) {
        this.onItemClick = onItemClick
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TeacherViewHolder {
        val itemView = parent.inflate(R.layout.item_teacher)
        val binding =ItemTeacherBinding.bind(itemView)
        return TeacherViewHolder(binding)
    }

    override fun onBindViewHolder(holder: TeacherViewHolder, position: Int) {
        holder.populateModel(models[position])
    }

    inner class TeacherViewHolder(private val binding: ItemTeacherBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun populateModel(model: Teacher) {
            binding.apply {
                tvTeacherName.text = model.name
                tvUsername.text = model.username
                clItemTeacher.setBackgroundResource(R.drawable.shape_teachers)

                clItemTeacher.onClick {
                    onItemClick.invoke(model.id)
                }

            }
        }
    }
}
