package com.devmasterteam.tasks.service.repository

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.telecom.ConnectionService
import com.devmasterteam.tasks.R
import com.devmasterteam.tasks.service.exception.NoInternetException
import com.devmasterteam.tasks.service.model.PersonModel
import com.devmasterteam.tasks.service.repository.remote.PersonService
import com.devmasterteam.tasks.service.repository.remote.RetrofitClient
import retrofit2.Response

class PersonRepositoty(val context: Context) {

    private val remote = RetrofitClient.getService(PersonService::class.java)

    suspend fun login(email: String, password: String): Response<PersonModel> {

        return saveAPICall { remote.login(email, password) }
    }

    suspend fun create(
        name: String,
        email: String,
        password: String,
        receiveNews: String
    ): Response<PersonModel> {

        return saveAPICall { remote.create(name, email, password, receiveNews) }
    }

    suspend fun<T> saveAPICall(apiCall: suspend () -> Response<T>): Response<T>{
        if (!isConnectionAvailable()){
            throw NoInternetException(context.getString(R.string.error_internet_connection))
        }

        return apiCall()
    }

    fun isConnectionAvailable(): Boolean { // Removi o 'Avalible' e padronizei para 'Available'
        var result = false // Inicializa result como false
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        // Para Android 10 (API 29) e superior
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            val capabilities =
                connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork) ?: return false

            // Se capabilities for nulo, result permanece false (já inicializado)
            if (capabilities != null) {
                result = when { // O 'when' agora atribui diretamente a 'result'
                    capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
                    capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
                    capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
                    else -> false // Se nenhum dos transportes conhecidos estiver ativo
                }
            }
        } else {
            // Para versões anteriores ao Android 10 (API 29)
            @Suppress("DEPRECATION")
            val networkInfo = connectivityManager.activeNetworkInfo
            @Suppress("DEPRECATION")
            result = (networkInfo != null && networkInfo.isConnected) // Atribui diretamente a 'result'
        }

        return result // Retorna o valor final de 'result'
    }
}

//activity viewmodel repositorio
//, receivenews: String