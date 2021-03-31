package uz.texnopos.texnoposedufinance.ui.main.course

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import uz.texnopos.texnoposedufinance.R
import uz.texnopos.texnoposedufinance.core.BaseAdapter
import uz.texnopos.texnoposedufinance.core.extentions.addVertDivider
import uz.texnopos.texnoposedufinance.core.extentions.inflate
import uz.texnopos.texnoposedufinance.core.extentions.onClick
import uz.texnopos.texnoposedufinance.core.extentions.visibility
import uz.texnopos.texnoposedufinance.data.model.Course
import uz.texnopos.texnoposedufinance.databinding.ItemCoursesBinding
import uz.texnopos.texnoposedufinance.ui.main.group.GroupAdapter

class CoursesAdapter : BaseAdapter<Course, CoursesAdapter.CoursesViewHolder>() {

    var onItemClick: (id: String) -> Unit = {}
    fun setOnItemClicked(onItemClick: (id: String) -> Unit) {
        this.onItemClick = onItemClick
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CoursesViewHolder {
        val itemView = parent.inflate(R.layout.item_courses)
        val binding = ItemCoursesBinding.bind(itemView)
        return CoursesViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CoursesViewHolder, position: Int) {
        holder.populateModel(models[position], position)
    }

    inner class CoursesViewHolder(private val binding: ItemCoursesBinding) :
        RecyclerView.ViewHolder(binding.root) {
        val adapter = GroupAdapter()
        fun populateModel(model: Course, position: Int) {
            binding.apply {
                tvCourseName.text = model.name
                tvGroupCount.text = root.context.getString(R.string.group_count)
                tvPupilsCount.text =
                    root.context.getString(R.string.participants_count, model.duration)
                rvGroups.adapter = adapter
                rvGroups.addVertDivider(root.context)
                setDrawable(position)

                rlLayout.onClick {
                    if (rvGroups.visibility == View.GONE) {
                        rvGroups.visibility(true)
                        onItemClick.invoke(model.id)
                    } else {
                        rvGroups.visibility(false)
                    }
                }
            }
        }

        private fun setDrawable(i: Int) {
            binding.apply {
                when (i % 3) {
                    0 -> rlLayout.setBackgroundResource(R.drawable.shape_teachers_1)
                    1 -> rlLayout.setBackgroundResource(R.drawable.shape_teachers_2)
                    2 -> rlLayout.setBackgroundResource(R.drawable.shape_teachers_3)
                }
            }

        }
    }
}

