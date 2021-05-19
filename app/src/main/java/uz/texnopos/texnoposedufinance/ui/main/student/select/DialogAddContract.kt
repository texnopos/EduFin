package uz.texnopos.texnoposedufinance.ui.main.student.select

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import uz.texnopos.texnoposedufinance.databinding.AddContractDialogBinding
import uz.texnopos.texnoposedufinance.databinding.DialogCalendarBinding

class DialogAddContract (context: Context) : Dialog(context) {
    lateinit var binding: AddContractDialogBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = AddContractDialogBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}