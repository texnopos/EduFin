package uz.texnopos.texnoposedufinance.ui.main.group.info

import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import uz.texnopos.texnoposedufinance.R
import uz.texnopos.texnoposedufinance.core.BaseAdapter
import uz.texnopos.texnoposedufinance.core.extentions.inflate
import uz.texnopos.texnoposedufinance.core.extentions.onClick
import uz.texnopos.texnoposedufinance.core.extentions.visibility
import uz.texnopos.texnoposedufinance.data.model.ParticipantResponse
import uz.texnopos.texnoposedufinance.databinding.ItemGroupInfoBinding


class GroupInfoAdapter : BaseAdapter<ParticipantResponse, GroupInfoAdapter.GroupInfoViewHolder>(){
    var periodCount = 0
    var coursePrice = 0
    private var onStudentItemClick: (participantId: String) -> Unit = { }
    fun setOnStudentItemClickListener(onStudentItemClick: (participantId: String) -> Unit) {
        this.onStudentItemClick = onStudentItemClick
    }
    inner class GroupInfoViewHolder(val binding: ItemGroupInfoBinding): RecyclerView.ViewHolder(binding.root){
        private var periodViewList = mutableListOf<ImageView>()
        init {
            binding.apply {
                periodViewList = mutableListOf(period1, period2, period3, period4, period5, period6, period7, period8, period9, period10, period11, period12)
            }
        }
        fun populateModel(model: ParticipantResponse){
            binding.apply {
                tvName.text = model.student.name
                setPeriodVisible(periodCount)
                var paymentAmount = model.payments.map { it.amount }.sum()
                var index = 0
                while (paymentAmount > 0) {
                    if (paymentAmount >= coursePrice) {
                        paymentAmount -= coursePrice
                        periodViewList[index].setImageResource(R.drawable.green_round)
                    } else if (paymentAmount > 0) {
                        paymentAmount = 0
                        periodViewList[index].setImageResource(R.drawable.yellow_round)
                    }
                    index++
                }
                clStudentItem.onClick {
                    onStudentItemClick.invoke(model.payments[0].participantId)
                }
            }

        }
        private fun setPeriodVisible(i: Int){
            binding.apply {
                repeat(i) {
                    periodViewList[it].visibility(true)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GroupInfoViewHolder {
        val itemView = parent.inflate(R.layout.item_group_info)
        val binding = ItemGroupInfoBinding.bind(itemView)
        return GroupInfoViewHolder(binding)
    }

    override fun onBindViewHolder(holder: GroupInfoViewHolder, position: Int) {
        holder.populateModel(models[position])
    }

}