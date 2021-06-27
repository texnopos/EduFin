package uz.texnopos.texnoposedufinance.data.retrofit

import com.google.firebase.auth.FirebaseAuth
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import uz.texnopos.texnoposedufinance.data.model.*
import uz.texnopos.texnoposedufinance.data.model.response.*

class NetworkHelper(auth: FirebaseAuth, private val apiInterface: ApiInterface) {
    private val orgId = auth.currentUser!!.uid
    fun getAllCourses(onSuccess: (List<Course>) -> Unit, onFailure: (msg: String) -> Unit) {
        val call: Call<List<Course>> = apiInterface.getAllCourses(orgId)
        call.enqueue(object : Callback<List<Course>> {
            override fun onFailure(call: Call<List<Course>>, t: Throwable) {
                onFailure.invoke(t.localizedMessage!!)
            }

            override fun onResponse(call: Call<List<Course>>, response: Response<List<Course>>) {
                response.body().let {
                    onSuccess.invoke(it!!)
                }
                if (response.body()!!.isEmpty()) {
                    onSuccess.invoke(listOf())
                }
            }
        })
    }

    fun getGroupParticipants(
        id: String,
        onSuccess: (List<ParticipantResponse>) -> Unit,
        onFailure: (msg: String) -> Unit
    ) {
        val call: Call<List<ParticipantResponse>> = apiInterface.getGroupParticipants(orgId, id)
        call.enqueue(object : Callback<List<ParticipantResponse>> {
            override fun onFailure(call: Call<List<ParticipantResponse>>, t: Throwable) {
                onFailure.invoke(t.localizedMessage!!)
            }

            override fun onResponse(
                call: Call<List<ParticipantResponse>>,
                response: Response<List<ParticipantResponse>>
            ) {
                response.body().let {
                    onSuccess.invoke(it!!)
                }
                if (response.body().isNullOrEmpty()) {
                    onSuccess.invoke(listOf())
                }
            }
        })

    }

    fun selectExistingStudentToGroup(
        groupId: String,
        onSuccess: (List<Student>) -> Unit,
        onFailure: (msg: String) -> Unit
    ) {
        val call: Call<List<Student>> = apiInterface.selectExistingStudentToGroup(orgId!!, groupId)
        call.enqueue(object : Callback<List<Student>> {
            override fun onFailure(call: Call<List<Student>>, t: Throwable) {
                onFailure.invoke(t.localizedMessage!!)
            }

            override fun onResponse(call: Call<List<Student>>, response: Response<List<Student>>) {
                response.body().let {
                    onSuccess.invoke(it!!)
                }
                if (response.body()!!.isEmpty()) {
                    onSuccess.invoke(listOf())
                }
            }
        })
    }

    fun createParticipantIfStudentNotExists(
        data: CreateParticipantRequest,
        onSuccess: (status: String) -> Unit,
        onFailure: (msg: String) -> Unit
    ) {
        val call: Call<PostResponse> = apiInterface.createParticipantIfStudentNotExists(data)
        call.enqueue(object : Callback<PostResponse> {
            override fun onFailure(call: Call<PostResponse>, t: Throwable) {
                onFailure.invoke(t.localizedMessage!!)
            }

            override fun onResponse(call: Call<PostResponse>, response: Response<PostResponse>) {
                if (response.isSuccessful) {
                    response.body()?.let {
                        onSuccess.invoke(it.status)
                    } ?: onFailure("Непредвиденная ошибка")
                }
            }
        })
    }
    fun createParticipantWithNewStudent(
        data: CreateParticipantRequest,
        onSuccess: (String) -> Unit,
        onFailure: (msg: String) -> Unit
    ) {
        val call: Call<PostResponse> = apiInterface.createParticipantWithNewStudent(data)
        call.enqueue(object : Callback<PostResponse> {
            override fun onFailure(call: Call<PostResponse>, t: Throwable) {
                onFailure.invoke(t.localizedMessage!!)
            }

            override fun onResponse(call: Call<PostResponse>, response: Response<PostResponse>) {
                if (response.isSuccessful) {
                    response.body()?.let {
                        onSuccess.invoke(it.status)
                    } ?: onFailure("Непредвиденная ошибка")
                } else {
                    onFailure(response.errorBody().toString())
                }
            }
        })
    }
    fun coursePayment(
        data: CoursePayments,
        onSuccess: (String) -> Unit,
        onFailure: (msg: String) -> Unit
    ) {
        val call: Call<PostResponse> = apiInterface.coursePayment(data)
        call.enqueue(object : Callback<PostResponse> {
            override fun onFailure(call: Call<PostResponse>, t: Throwable) {
                onFailure.invoke(t.localizedMessage!!)
            }

            override fun onResponse(call: Call<PostResponse>, response: Response<PostResponse>) {
                if (response.isSuccessful) {
                    response.body()?.let {
                        onSuccess.invoke(it.status)
                    } ?: onFailure("Непредвиденная ошибка")
                }
            }

        })
    }

    fun createParticipantWithStudentId(
        data: SendParticipantDataRequest,
        onSuccess: (String) -> Unit,
        onFailure: (msg: String) -> Unit
    ) {
        val call: Call<PostResponse> = apiInterface.createParticipantWithStudentId(data)
        call.enqueue(object : Callback<PostResponse> {
            override fun onFailure(call: Call<PostResponse>, t: Throwable) {
                onFailure.invoke(t.localizedMessage!!)
            }

            override fun onResponse(call: Call<PostResponse>, response: Response<PostResponse>) {
                if (response.isSuccessful) {
                    response.body()?.let {
                        onSuccess.invoke(it.status)
                    } ?: onFailure("Непредвиденная ошибка")
                } else {
                    onFailure(response.errorBody().toString())
                }
            }
        })
    }

    fun checkContract(
        data: ContractRequest,
        onSuccess: (String) -> Unit,
        onFailure: (msg: String) -> Unit
    ) {
        val call: Call<PostResponse> = apiInterface.checkContract(data)
        call.enqueue(object : Callback<PostResponse> {
            override fun onFailure(call: Call<PostResponse>, t: Throwable) {
                onFailure.invoke(t.localizedMessage!!)
            }

            override fun onResponse(call: Call<PostResponse>, response: Response<PostResponse>) {
                if (response.isSuccessful) {
                    response.body()?.let {
                        onSuccess.invoke(it.status)
                    } ?: onFailure("Непредвиденная ошибка")
                } else {
                    onFailure(response.errorBody().toString())
                }
            }
        })
    }

    fun getReports(
        fromDate: Long,
        toDate: Long,
        onSuccess: (ReportResponse) -> Unit,
        onFailure: (msg: String) -> Unit
    ) {
        val call: Call<ReportResponse> = apiInterface.getReports(orgId, fromDate, toDate)
        call.enqueue(object : Callback<ReportResponse> {
            override fun onFailure(call: Call<ReportResponse>, t: Throwable) {
                onFailure.invoke(t.localizedMessage!!)
            }

            override fun onResponse(
                call: Call<ReportResponse>,
                response: Response<ReportResponse>
            ) {
                response.body()?.let {
                    onSuccess.invoke(it)
                }?: onFailure.invoke("Непредвиденная ошибка")
            }
        })
    }

    fun addExpense(data: ExpenseRequest,
                   onSuccess: (ExpenseRequest) -> Unit,
                   onFailure: (msg: String) -> Unit){
        val call: Call<ExpenseRequest> = apiInterface.addExpense(data)
        call.enqueue(object: Callback<ExpenseRequest>{
            override fun onFailure(call: Call<ExpenseRequest>, t: Throwable) {
                onFailure.invoke(t.localizedMessage!!)
            }

            override fun onResponse(
                call: Call<ExpenseRequest>,
                response: Response<ExpenseRequest>
            ) {
                response.body().let {
                    onSuccess.invoke(it!!)
                }
            }
        })
    }

    fun addIncome(data: IncomeRequest,
                   onSuccess: (IncomeRequest) -> Unit,
                   onFailure: (msg: String) -> Unit){
        val call: Call<IncomeRequest> = apiInterface.addIncome(data)
        call.enqueue(object: Callback<IncomeRequest>{
            override fun onFailure(call: Call<IncomeRequest>, t: Throwable) {
                onFailure.invoke(t.localizedMessage!!)
            }
            override fun onResponse(
                call: Call<IncomeRequest>,
                response: Response<IncomeRequest>
            ) {
                response.body().let {
                    onSuccess.invoke(it!!)
                }
            }
        })
    }

    fun getSalary(
        fromDate: Long,
        toDate: Long,
        onSuccess: (SalaryResponse) -> Unit,
        onFailure: (msg: String) -> Unit
    ) {
        val call: Call<SalaryResponse> = apiInterface.getSalary(orgId, fromDate, toDate)
        call.enqueue(object : Callback<SalaryResponse> {
            override fun onFailure(call: Call<SalaryResponse>, t: Throwable) {
                onFailure.invoke(t.localizedMessage!!)
            }

            override fun onResponse(
                call: Call<SalaryResponse>,
                response: Response<SalaryResponse>
            ) {
                response.body().let {
                    onSuccess.invoke(it!!)
                }
            }
        })
    }
}