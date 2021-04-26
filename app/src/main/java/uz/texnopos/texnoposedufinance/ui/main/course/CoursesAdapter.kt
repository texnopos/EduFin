package uz.texnopos.texnoposedufinance.ui.main.course

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.RecyclerView
import uz.texnopos.texnoposedufinance.R
import uz.texnopos.texnoposedufinance.core.BaseAdapter
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

    var setAddGroup: (id: String) -> Unit = {}
    fun setAddGroupClicked(addGroupId: (id: String) -> Unit) {
        this.setAddGroup = addGroupId
    }

    var onResponse: (List<Course>) -> Unit = {}
    fun onResponse(onResponse: (List<Course>) -> Unit) {
        this.onResponse = onResponse
    }

    var onFailure: (String?) -> Unit = {}
    fun onFailure(onFailure: (String?) -> Unit) {
        this.onFailure = onFailure
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CoursesViewHolder {
        val itemView = parent.inflate(R.layout.item_courses)
        val binding = ItemCoursesBinding.bind(itemView)
        return CoursesViewHolder(binding)

    }

    override fun onBindViewHolder(holder: CoursesViewHolder, position: Int) {
        holder.populateModel(models[position])
    }

    inner class CoursesViewHolder(private val binding: ItemCoursesBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun populateModel(model: Course) {
            binding.apply {
                rvGroups.addItemDecoration(
                    DividerItemDecoration(root.context, DividerItemDecoration.VERTICAL)
                )
                rvGroups.visibility(false)
                addGroup.visibility(false)
                cvGroups.visibility(false)
                line.visibility(false)
                tvCourseName.text = model.name
                val g = model.groups.size.toString()
                tvGroupCount.text = root.context.getString(R.string.group_count, g)
                tvPupilsCount.text =
                    root.context.getString(R.string.period, model.duration)
                rlLayout.setBackgroundResource(R.drawable.shape_teachers)

                rlLayout.onClick {
                    val groupAdapter = GroupAdapter()
                    rvGroups.adapter = groupAdapter
                    groupAdapter.models = model.groups
                    if (cvGroups.visibility == View.GONE && addGroup.visibility == View.GONE) {
                        addGroup.visibility(true)
                        line.visibility(true)
                        if (model.groups.isNotEmpty()) {
                            cvGroups.visibility(true)
                            rvGroups.visibility(true)
                        } else {
                            rvGroups.visibility(false)
                            cvGroups.visibility(false)
                        }
                        rlLayout.setBackgroundResource(R.drawable.shape_courses_1_open)
                        addGroup.setBackgroundResource(R.drawable.shape_courses_1_close)
                    } else {
                        cvGroups.visibility(false)
                        addGroup.visibility(false)
                        rvGroups.visibility(false)
                        line.visibility(false)
                        rlLayout.setBackgroundResource(R.drawable.shape_teachers)
                    }
                }
                addGroup.onClick {
                    setAddGroup.invoke(model.id)
                }
            }
        }
    }
}

