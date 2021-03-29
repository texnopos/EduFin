package uz.texnopos.texnoposedufinance.di

import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.functions.FirebaseFunctions
import org.koin.android.ext.koin.androidApplication
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.dsl.module
import uz.texnopos.texnoposedufinance.R
import uz.texnopos.texnoposedufinance.data.firebase.AuthHelper
import uz.texnopos.texnoposedufinance.data.firebase.CourseHelper
import uz.texnopos.texnoposedufinance.data.firebase.TeacherHelper
import uz.texnopos.texnoposedufinance.data.firebase.GroupHelper
import uz.texnopos.texnoposedufinance.ui.auth.signin.SignInViewModel
import uz.texnopos.texnoposedufinance.ui.auth.signup.SignUpViewModel
import uz.texnopos.texnoposedufinance.ui.main.course.CoursesViewModel
import uz.texnopos.texnoposedufinance.ui.main.course.add.AddCoursesViewModel
import uz.texnopos.texnoposedufinance.ui.main.teacher.TeacherViewModel
import uz.texnopos.texnoposedufinance.ui.main.teacher.add.AddTeacherViewModel
import uz.texnopos.texnoposedufinance.ui.main.group.GroupViewModel
import uz.texnopos.texnoposedufinance.ui.main.group.add.AddGroupViewModel

val firebaseModule = module {
    single { FirebaseAuth.getInstance() }
    single { FirebaseFirestore.getInstance() }
    single {FirebaseFunctions.getInstance()}
    single {
        GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(androidApplication().applicationContext.getString(R.string.default_web_client_id))
            .requestEmail()
            .build()
    }
    single { GoogleSignIn.getClient(androidApplication().applicationContext, get()) }
}

val helperModule = module {
    single { AuthHelper(get()) }
    single { TeacherHelper(get(), get()) }
    single { CourseHelper(get(), get(), get()) }
    single { GroupHelper(get(), get()) }
}

val viewModelModule = module {
    viewModel { SignInViewModel(get()) }
    viewModel { SignUpViewModel(get()) }
    viewModel { TeacherViewModel(get()) }
    viewModel { AddTeacherViewModel(get()) }
    viewModel { CoursesViewModel(get()) }
    viewModel { AddCoursesViewModel(get(), get()) }
    viewModel { AddGroupViewModel(get(), get()) }
    viewModel { GroupViewModel(get()) }
}
val adapterModule = module {

}
