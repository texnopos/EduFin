package uz.texnopos.texnoposedufinance.ui.main.student.add.select_existing_student

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.GsonBuilder
import uz.texnopos.texnoposedufinance.R
import uz.texnopos.texnoposedufinance.core.BaseAdapter
import uz.texnopos.texnoposedufinance.core.extentions.inflate
import uz.texnopos.texnoposedufinance.core.extentions.onClick
import uz.texnopos.texnoposedufinance.data.model.Course
import uz.texnopos.texnoposedufinance.data.model.Student
import uz.texnopos.texnoposedufinance.databinding.ItemSelectExistingStudentBinding

class SelectExistingStudentAdapter: BaseAdapter<Student, SelectExistingStudentAdapter.SelectExistingStudentVH>() {

    private var onItemClicked: (student: String) -> Unit = {}
    fun setOnItemClickListener(onGroupItemClicked: (student: String) -> Unit) {
        this.onItemClicked = onGroupItemClicked
    }
    inner class SelectExistingStudentVH(val binding: ItemSelectExistingStudentBinding): RecyclerView.ViewHolder(binding.root){
        fun populateModel(model: Student){
            binding.apply {
                tvName.text = model.name
                tvAddress.text = model.address
                tvCourseName.text = model.course
                tvPassport.text = model.passport
                val gsonPretty = GsonBuilder().setPrettyPrinting().create()
                val jsonString = gsonPretty.toJson(Student(model.id, model.name, model.phone, model.course, model.passport, model.birthDate, model.address))
                clStudentItem.onClick {
                    onItemClicked.invoke(jsonString)
                }
            }
        }
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SelectExistingStudentVH {
        val itemView = parent.inflate(R.layout.item_select_existing_student)
        val binding = ItemSelectExistingStudentBinding.bind(itemView)
        return SelectExistingStudentVH(binding)
    }

    override fun onBindViewHolder(holder: SelectExistingStudentVH, position: Int) {
        holder.populateModel(models[position])
    }
}