package uz.texnopos.texnoposedufinance.ui.main.group.add

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import uz.texnopos.texnoposedufinance.core.extentions.onClick
import uz.texnopos.texnoposedufinance.databinding.DialogCalendarBinding

class CalendarDialog(context: Context):Dialog(context) {
    lateinit var binding: DialogCalendarBinding

    var getData: (time: String) -> Unit = {}
    fun getData(a: (time: String) -> Unit) {
        getData = a
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DialogCalendarBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.apply {
            cvCalendar.setOnDateChangeListener { _, year, month, dayOfMonth ->
                btnYes.onClick {
                    var y = "$year"
                    var m = "${month + 1}"
                    var d = "$dayOfMonth"
                    if (y.toInt() < 10) y = "0$y"
                    if (m.toInt() < 10) m = "0$m"
                    if (d.toInt() < 10) d = "0$d"
                    getData.invoke("$d.$m.$y")
                    dismiss()
                }
            }
            btnCancel.onClick {
                dismiss()
            }
            btnYes.onClick {
                dismiss()
            }
        }
    }
}