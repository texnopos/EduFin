package uz.texnopos.texnoposedufinance.ui.main.teacher

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import uz.texnopos.texnoposedufinance.R
import uz.texnopos.texnoposedufinance.core.BaseAdapter
import uz.texnopos.texnoposedufinance.core.extentions.inflate
import uz.texnopos.texnoposedufinance.core.extentions.onClick
import uz.texnopos.texnoposedufinance.data.model.Teacher
import uz.texnopos.texnoposedufinance.databinding.ItemTeachersBinding

class TeacherAdapter :
    BaseAdapter<Teacher, TeacherAdapter.TeacherViewHolder>() {
    var onItemClick: (id: String) -> Unit = {}
    fun setOnItemClicked(onItemClick: (id: String) -> Unit){
        this.onItemClick = onItemClick
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TeacherViewHolder {
        val itemView = parent.inflate(R.layout.item_teachers)
        val binding = ItemTeachersBinding.bind(itemView)
        return TeacherViewHolder(binding)
    }

    override fun onBindViewHolder(holder: TeacherViewHolder, position: Int) {
        holder.populateModel(models[position], position)
    }

    inner class TeacherViewHolder(private val binding: ItemTeachersBinding) : RecyclerView.ViewHolder(binding.root) {
        fun populateModel(model: Teacher, position: Int) {
            binding.apply {
                tvTeacherName.text = model.name
                tvUsername.text = model.username
                clItemTeacher.setBackgroundResource(R.drawable.shape_teachers_1)

                clItemTeacher.onClick {
                    onItemClick.invoke(model.id)
                }

            }
        }

//        private fun setDrawable(i: Int) {
//            binding.apply {
//                when (i % 3) {
//                    0 -> clItemTeacher.setBackgroundResource(R.drawable.shape_teachers_1)
//                    1 -> clItemTeacher.setBackgroundResource(R.drawable.shape_teachers_2)
//                    2 -> clItemTeacher.setBackgroundResource(R.drawable.shape_teachers_3)
//                }
//            }
//        }
    }
}