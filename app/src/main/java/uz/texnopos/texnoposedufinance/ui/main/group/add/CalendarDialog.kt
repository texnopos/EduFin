package uz.texnopos.texnoposedufinance.ui.main.group.add

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import uz.texnopos.texnoposedufinance.core.extentions.onClick
import uz.texnopos.texnoposedufinance.databinding.DialogCalendarBinding

class CalendarDialog(context: Context):
        Dialog(context){
    lateinit var binding: DialogCalendarBinding

    var getData:(time:String)->Unit={}
    fun getData(a:(time:String)->Unit){
        getData=a
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DialogCalendarBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.apply {
            calendar.setOnDateChangeListener{ _, year, month, dayOfMonth ->
                btnYes.onClick {
                    getData.invoke("$dayOfMonth.${month+1}.$year")
                    dismiss()
                }
            }
            btnCancel.onClick {
                dismiss()
            }
        }
    }
}