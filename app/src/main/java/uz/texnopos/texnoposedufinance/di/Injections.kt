package uz.texnopos.texnoposedufinance.di

import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.functions.FirebaseFunctions
import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.android.ext.koin.androidApplication
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import uz.texnopos.texnoposedufinance.R
import uz.texnopos.texnoposedufinance.data.firebase.*
import uz.texnopos.texnoposedufinance.data.retrofit.NetworkHelper
import uz.texnopos.texnoposedufinance.data.retrofit.ApiInterface
import uz.texnopos.texnoposedufinance.ui.app.AppViewModel
import uz.texnopos.texnoposedufinance.ui.auth.signin.SignInViewModel
import uz.texnopos.texnoposedufinance.ui.auth.signup.SignUpViewModel
import uz.texnopos.texnoposedufinance.ui.main.category.CategoryViewModel
import uz.texnopos.texnoposedufinance.ui.main.course.CourseAdapter
import uz.texnopos.texnoposedufinance.ui.main.course.CourseViewModel
import uz.texnopos.texnoposedufinance.ui.main.group.GroupAdapter
import uz.texnopos.texnoposedufinance.ui.main.teacher.TeacherViewModel
import uz.texnopos.texnoposedufinance.ui.main.group.info.GroupInfoViewModel
import uz.texnopos.texnoposedufinance.ui.main.group.add.AddGroupViewModel
import uz.texnopos.texnoposedufinance.ui.main.group.info.GroupInfoAdapter
import uz.texnopos.texnoposedufinance.ui.main.info.InfoViewModel
import uz.texnopos.texnoposedufinance.ui.main.report.ReportViewModel
import uz.texnopos.texnoposedufinance.ui.main.student.StudentAdapter
import uz.texnopos.texnoposedufinance.ui.main.student.StudentViewModel
import uz.texnopos.texnoposedufinance.ui.main.student.add.select_existing_student.SelectExistingStudentAdapter
import uz.texnopos.texnoposedufinance.ui.main.student.select.SelectStudentsAdapter
import uz.texnopos.texnoposedufinance.ui.main.teacher.TeacherAdapter
import java.util.concurrent.TimeUnit

private const val productionUrl = "https://us-central1-texnopos-finance.cloudfunctions.net/api/"
private const val localUrl = "192.168.1.116:5001/texnopos-finance/us-central1/api/"
private const val baseUrl: String = productionUrl
val firebaseModule = module {
    single { FirebaseAuth.getInstance() }
    single { FirebaseFirestore.getInstance() }
    single { FirebaseFunctions.getInstance() }
    single {
        GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(androidApplication().applicationContext.getString(R.string.default_web_client_id))
            .requestEmail()
            .build()
    }
    single { GoogleSignIn.getClient(androidApplication().applicationContext, get()) }
}

val networkModule = module {
    single {
        GsonBuilder().setLenient().create()
    }
    single {
        val loggingInterceptor = HttpLoggingInterceptor()
        loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
        OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .connectTimeout(100, TimeUnit.SECONDS)
            .readTimeout(100, TimeUnit.SECONDS)
            .writeTimeout(100, TimeUnit.SECONDS)
            .retryOnConnectionFailure(false)
            .build()

    }
    single {
        Retrofit
            .Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create(get()))
            .client(get())
            .build()
    }
    single { get<Retrofit>().create(ApiInterface::class.java) }
}

val helperModule = module {
    single { AuthHelper(get(), get()) }
    single { TeacherHelper(get(), get()) }
    single { CourseHelper(get(), get()) }
    single { GroupHelper(get(), get()) }
    single { NetworkHelper(get(), get()) }
    single { StudentHelper(get(), get()) }
    single { InfoHelper(get(), get()) }
    single { CategoryHelper(get(), get()) }
    single { ReportHelper(get(), get()) }
}

val viewModelModule = module {
    viewModel { SignInViewModel(get()) }
    viewModel { SignUpViewModel(get()) }
    viewModel { TeacherViewModel(get()) }
    viewModel { CourseViewModel(get()) }
    viewModel { AddGroupViewModel(get()) }
    viewModel { GroupInfoViewModel(get()) }
    viewModel { StudentViewModel(get(), get()) }
    viewModel { InfoViewModel(get()) }
    viewModel { CategoryViewModel(get()) }
    viewModel { ReportViewModel(get(), get()) }
    viewModel { AppViewModel(get()) }
}
val adapterModule = module {
    single { CourseAdapter() }
    single { StudentAdapter() }
    single { TeacherAdapter() }
    single { GroupInfoAdapter() }
    single { GroupAdapter() }
    single { SelectStudentsAdapter() }
    single { SelectExistingStudentAdapter() }
}
