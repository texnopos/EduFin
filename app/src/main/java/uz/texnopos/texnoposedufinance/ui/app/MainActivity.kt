package uz.texnopos.texnoposedufinance.ui.app

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.LiveData
import androidx.navigation.NavController
import androidx.navigation.findNavController
import org.koin.android.viewmodel.ext.android.viewModel
import uz.texnopos.texnoposedufinance.R
import uz.texnopos.texnoposedufinance.core.Resource
import uz.texnopos.texnoposedufinance.data.model.response.ReportResponse
import uz.texnopos.texnoposedufinance.databinding.ActivityMainBinding
import java.text.SimpleDateFormat
import java.util.*

class MainActivity : AppCompatActivity() {

    private lateinit var navController: NavController
    private lateinit var binding: ActivityMainBinding
    private val viewModel: MainActivityViewModel by viewModel()

    val report: LiveData<Resource<ReportResponse>>
    get() = viewModel.report

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        navController = findNavController(R.id.nav_host)
    }

    @SuppressLint("SimpleDateFormat")
    fun getReport() {
        val sdf = SimpleDateFormat("dd.MM.yyyy")
        val calendar = Calendar.getInstance()
        val toString = sdf.format(calendar.time).toString()
        val day = toString.substring(0, 2)
        val toLong = calendar.timeInMillis
        val fromLong = toLong - day.toInt() * 3600 * 1000 * 24
        viewModel.getReports(fromLong, toLong)
    }
}

