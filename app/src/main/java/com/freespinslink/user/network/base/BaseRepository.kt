package com.freespinslink.user.network.base

import android.content.Context
import com.freespinslink.user.R
import com.freespinslink.user.utils.CommonConfig
import com.google.gson.Gson
import okhttp3.RequestBody
import okio.Buffer
import org.json.JSONObject
import retrofit2.Response
import java.io.IOException
import java.net.UnknownHostException
import java.net.UnknownServiceException


open class BaseRepository {

    suspend fun <T : Any> safeApiCall(call: suspend () -> Response<T>): ResponseModel<T> {

        return when (val result: Result<T> = safeApiResult(call)) {
            is Result.Success ->
                ResponseModel(result.data, null)
            is Result.Error -> {
                ResponseModel(null, result.errorResponse)
            }
        }
    }

    private suspend fun <T : Any> safeApiResult(call: suspend () -> Response<T>): Result<T> {
        try {
            val response = call.invoke()
            if (response.isSuccessful) {
                return Result.Success(response.body()!!)
            }

            return Result.Error(parseError(response))
        } catch (ex: Exception) {
            ex.printStackTrace()
            return Result.Error(parseError(ex))
        }
    }

    /**
     * Parse Error Response from Server Side Response
     */
    private fun parseError(response: Response<*>): ErrorResponse {
        val errorBody = response.errorBody()?.string()
        var errorResponse: ErrorResponse
        try {
            errorResponse = Gson().fromJson(
                JSONObject(errorBody).toString(),
                ErrorResponse::class.java
            )
        } catch (ex: Exception) {
            errorResponse = parseError(response.code())
            ex.printStackTrace()
        }
        return errorResponse
    }

    /**
     * Generate Local Error Response from any exception
     */
    private fun parseError(exception: Exception): ErrorResponse {
        val errorResponse = ErrorResponse()
        if (exception is UnknownHostException || exception is UnknownServiceException) {
            errorResponse.message = getAppContext().getString(R.string.internet_lost)
//        } else if (exception is SSLPeerUnverifiedException) {
//            errorResponse.message = getAppContext().getString(R.string.error_message_ssl_pining)
//            CommonConfig.onSslException()
        } else {
            errorResponse.message = ""
        }
        return errorResponse
    }

    /**
     * Generate Local Error Response from http error code
     */
    private fun parseError(code: Int): ErrorResponse {
        val errorResponse = ErrorResponse()
        errorResponse.code = code
        when (code) {
            in 500..510 -> {
                errorResponse.message = getAppContext().getString(R.string.error_message_502)
            }
            in 401..403 -> {
                errorResponse.message = getAppContext().getString(R.string.error_message_403)
            }
            else -> {
                errorResponse.message = getAppContext().getString(R.string.internet_lost)
            }
        }
        return errorResponse
    }


    private fun getAppContext(): Context {
        return CommonConfig.getAppContext()
    }


    private fun bodyToString(request: RequestBody?): String {
        try {
            val buffer = Buffer()
            if (request != null)
                request.writeTo(buffer)
            else
                return ""
            return buffer.readUtf8()
        } catch (e: IOException) {
            return "Error Response"
        }
    }
}