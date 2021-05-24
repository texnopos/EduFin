package uz.texnopos.texnoposedufinance.ui.main.group

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import uz.texnopos.texnoposedufinance.R
import uz.texnopos.texnoposedufinance.core.BaseAdapter
import uz.texnopos.texnoposedufinance.core.extentions.inflate
import uz.texnopos.texnoposedufinance.core.extentions.onClick
import uz.texnopos.texnoposedufinance.data.model.Course
import uz.texnopos.texnoposedufinance.data.model.Group
import uz.texnopos.texnoposedufinance.databinding.ItemGroupBinding

class GroupAdapter: BaseAdapter<Group, GroupAdapter.GroupViewHolder>(){

    private var onItemClick: (String) -> Unit = {}
    fun setOnItemClickListener(onItemClick: (String) -> Unit) {
        this.onItemClick = onItemClick
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GroupViewHolder {
        val itemView = parent.inflate(R.layout.item_group)
        val binding = ItemGroupBinding.bind(itemView)
        return GroupViewHolder(binding)
    }

    override fun onBindViewHolder(holder: GroupViewHolder, position: Int) {
        holder.populateModel(models[position])
    }

    inner class GroupViewHolder(val binding: ItemGroupBinding): RecyclerView.ViewHolder(binding.root){
        fun populateModel(model: Group){
            binding.apply {
                val gsonPretty = GsonBuilder().setPrettyPrinting().create()
                val jsonString = gsonPretty.toJson(Group(model.id, model.courseId,
                model.courseName, model.name, model.time, model.startDate, model.teacher, model.days, model.created))
                name.text = model.name
                days.text = model.days
                time.text = model.time
                clGroup.onClick {
                    onItemClick.invoke(jsonString)
                }
            }
        }
    }
}