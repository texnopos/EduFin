package uz.texnopos.texnoposedufinance.ui.main.salary

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import uz.texnopos.texnoposedufinance.databinding.DialogSalaryBinding

class SalaryDialog (context: Context): Dialog(context) {
    lateinit var binding: DialogSalaryBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DialogSalaryBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}