package uz.texnopos.texnoposedufinance.ui.main.student.select

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.GsonBuilder
import uz.texnopos.texnoposedufinance.R
import uz.texnopos.texnoposedufinance.core.BaseAdapter
import uz.texnopos.texnoposedufinance.core.extentions.inflate
import uz.texnopos.texnoposedufinance.core.extentions.onClick
import uz.texnopos.texnoposedufinance.data.model.Student
import uz.texnopos.texnoposedufinance.databinding.ItemSelectStudentBinding

class SelectStudentsAdapter : BaseAdapter<Student, SelectStudentsAdapter.SelectStudentsViewHolder>() {
    private var onItemClicked: (student: String) -> Unit = {}
    fun setOnItemClickListener(onGroupItemClicked: (student: String) -> Unit) {
        this.onItemClicked = onGroupItemClicked
    }
    inner class SelectStudentsViewHolder(private val binding: ItemSelectStudentBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun populateModel(model: Student) {
            binding.apply {
                tvStudentName.text = model.name
                tvInterestedCourse.text = model.course
                val gsonPretty = GsonBuilder().setPrettyPrinting().create()
                val jsonString = gsonPretty.toJson(Student(model.id, model.name, model.phone, model.course, model.passport, model.birthDate, model.createdDate, model.address))
                clSelectStudent.onClick {
                    onItemClicked.invoke(jsonString)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SelectStudentsViewHolder {
        val itemView = parent.inflate(R.layout.item_select_student)
        val binding = ItemSelectStudentBinding.bind(itemView)
        return SelectStudentsViewHolder(binding)
    }

    override fun onBindViewHolder(holder: SelectStudentsViewHolder, position: Int) {
        holder.populateModel(models[position])
    }
}