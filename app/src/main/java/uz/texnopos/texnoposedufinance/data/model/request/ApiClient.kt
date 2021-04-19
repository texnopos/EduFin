package uz.texnopos.texnoposedufinance.data.model.request

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ApiClient {
    companion object{
        private lateinit var retrofit: Retrofit
        private lateinit var gson: Gson

        fun getClient(): Retrofit{
            if(!::gson.isInitialized){
                gson = GsonBuilder().setLenient().create()
            }
            if(!::retrofit.isInitialized){
                retrofit = Retrofit.Builder()
                    .baseUrl("https://us-central1-texnopos-finance.cloudfunctions.net/")
                    .addConverterFactory(GsonConverterFactory.create()).build()
            }
            return retrofit
        }
    }
}