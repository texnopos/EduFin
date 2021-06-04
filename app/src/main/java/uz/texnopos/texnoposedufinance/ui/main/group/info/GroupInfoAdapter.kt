package uz.texnopos.texnoposedufinance.ui.main.group.info

import android.annotation.SuppressLint
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import uz.texnopos.texnoposedufinance.R
import uz.texnopos.texnoposedufinance.core.BaseAdapter
import uz.texnopos.texnoposedufinance.core.extentions.inflate
import uz.texnopos.texnoposedufinance.core.extentions.onClick
import uz.texnopos.texnoposedufinance.core.extentions.visibility
import uz.texnopos.texnoposedufinance.data.model.response.ParticipantResponse
import uz.texnopos.texnoposedufinance.databinding.ItemGroupInfoBinding

class GroupInfoAdapter : BaseAdapter<ParticipantResponse, GroupInfoAdapter.GroupInfoViewHolder>() {
    var periodCount = 0
    var coursePrice = 0
    private var onStudentItemClick: (participantId: String) -> Unit = { }
    fun setOnStudentItemClickListener(onStudentItemClick: (participantId: String) -> Unit) {
        this.onStudentItemClick = onStudentItemClick
    }
    private var callStudent: (number1: String, number2: String) -> Unit = {number1, number2 ->  }
    fun callStudentClicked(callStudent: (number1: String, number2: String) -> Unit) {
        this.callStudent = callStudent
    }
    inner class GroupInfoViewHolder(val binding: ItemGroupInfoBinding) :
        RecyclerView.ViewHolder(binding.root) {
        private var periodViewList = mutableListOf<ImageView>()
        init {
            binding.apply {
                periodViewList = mutableListOf(
                    period1,
                    period2,
                    period3,
                    period4,
                    period5,
                    period6,
                    period7,
                    period8,
                    period9,
                    period10,
                    period11,
                    period12
                )
            }
        }

        @SuppressLint("ResourceAsColor")
        fun populateModel(model: ParticipantResponse) {
            binding.apply {
                model.apply {
                    tvName.text = student.name
                    tvAddress.text = student.address
                    tvContactNum.text = participant.contract.toString()
                    tvPassport.text = student.passport
                    setPeriodVisible(periodCount)
                    var paymentAmount = payments.map { it.amount }.sum()
                    val remainder = paymentAmount - coursePrice * periodCount
                    when {
                        remainder > 0 -> {
                            tvRemainder.setTextColor(R.color.green)
                            tvRemainder.visibility(true)
                        }
                        remainder == 0 -> tvRemainder.visibility(false)
                        else -> {
                            tvRemainder.setTextColor(R.color.red)
                            tvRemainder.visibility(true)
                        }
                    }
                    tvRemainder.text = remainder.toString()
                    periodViewList.forEach {
                        it.setImageResource(R.drawable.red_round)
                    }
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
                }
                btnCall.onClick {
                    model.student.apply {
                        callStudent.invoke(phone[0], phone[1])
                    }

                }
                clStudentItem.onClick {
                    onStudentItemClick.invoke(model.participant.id)
                }
            }
        }

        private fun setPeriodVisible(i: Int) {
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