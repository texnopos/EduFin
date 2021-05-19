package uz.texnopos.texnoposedufinance.ui.main.group.add

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import uz.texnopos.texnoposedufinance.core.extentions.onClick
import uz.texnopos.texnoposedufinance.databinding.DialogCalendarBinding

class CalendarDialog(context: Context) : Dialog(context) {
    lateinit var binding: DialogCalendarBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DialogCalendarBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}