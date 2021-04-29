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
import uz.texnopos.texnoposedufinance.data.firebase.AuthHelper
import uz.texnopos.texnoposedufinance.data.firebase.CourseHelper
import uz.texnopos.texnoposedufinance.data.firebase.TeacherHelper
import uz.texnopos.texnoposedufinance.data.firebase.GroupHelper
import uz.texnopos.texnoposedufinance.data.model.request.NetworkHelper
import uz.texnopos.texnoposedufinance.data.retrofit.ApiInterface
import uz.texnopos.texnoposedufinance.ui.auth.signin.SignInViewModel
import uz.texnopos.texnoposedufinance.ui.auth.signup.SignUpViewModel
import uz.texnopos.texnoposedufinance.ui.main.course.CourseViewModel
import uz.texnopos.texnoposedufinance.ui.main.course.add.AddCourseViewModel
import uz.texnopos.texnoposedufinance.ui.main.teacher.TeacherViewModel
import uz.texnopos.texnoposedufinance.ui.main.teacher.add.AddTeacherViewModel
import uz.texnopos.texnoposedufinance.ui.main.group.GroupViewModel
import uz.texnopos.texnoposedufinance.ui.main.group.add.AddGroupViewModel
import java.util.concurrent.TimeUnit

private const val baseUrl: String = "https://us-central1-texnopos-finance.cloudfunctions.net/"
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
            .connectTimeout(10, TimeUnit.SECONDS)
            .readTimeout(10, TimeUnit.SECONDS)
            .writeTimeout(10, TimeUnit.SECONDS)
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
    single { GroupHelper(get(), get(), get()) }
    single { NetworkHelper(get()) }
}

val viewModelModule = module {
    viewModel { SignInViewModel(get()) }
    viewModel { SignUpViewModel(get()) }
    viewModel { TeacherViewModel(get()) }
    viewModel { AddTeacherViewModel(get()) }
    viewModel { CourseViewModel(get()) }
    viewModel { AddCourseViewModel(get(), get()) }
    viewModel { AddGroupViewModel(get(), get()) }
    viewModel { GroupViewModel(get()) }
}
val adapterModule = module {

}
