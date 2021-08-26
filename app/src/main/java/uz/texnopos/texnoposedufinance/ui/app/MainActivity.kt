package uz.texnopos.texnoposedufinance.ui.app

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.LiveData
import androidx.navigation.NavController
import androidx.navigation.findNavController
import org.koin.android.viewmodel.ext.android.viewModel
import uz.texnopos.texnoposedufinance.R
import uz.texnopos.texnoposedufinance.core.Resource
import uz.texnopos.texnoposedufinance.data.model.Course
import uz.texnopos.texnoposedufinance.data.model.response.ReportResponse
import uz.texnopos.texnoposedufinance.data.model.response.SalaryResponse
import uz.texnopos.texnoposedufinance.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var navController: NavController
    private lateinit var binding: ActivityMainBinding
    private val viewModel: AppViewModel by viewModel()

    val report: LiveData<Resource<ReportResponse>>
        get() = viewModel.report

    val course: LiveData<Resource<List<Course>>>
        get() = viewModel.courseList

    val salary: LiveData<Resource<SalaryResponse>>
        get() = viewModel.salary

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        navController = findNavController(R.id.nav_host)
        /*val sdf = SimpleDateFormat("dd.MM.yyyy")
        val calendar = Calendar.getInstance()
        val toString = sdf.format(calendar.time).toString()
        val day = toString.substring(0, 2)
        val toLong = calendar.timeInMillis
        val fromLong = toLong - day.toInt() * 3600 * 1000 * 24*/
    }

    fun getReport(from: Long, to: Long) {
        viewModel.getReports(from, to)
    }

    fun getAllCourse(){
        viewModel.getAllCourses()
    }

    fun getSalary(from: Long, to: Long){
        viewModel.getSalary(from, to)
    }
}

