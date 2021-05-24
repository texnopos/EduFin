package uz.texnopos.texnoposedufinance.ui.main.report.income

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import uz.texnopos.texnoposedufinance.databinding.DialogPaymentBinding

class PaymentDialog(context: Context): Dialog(context) {
    lateinit var binding: DialogPaymentBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DialogPaymentBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}