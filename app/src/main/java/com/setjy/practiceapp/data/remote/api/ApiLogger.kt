package com.setjy.practiceapp.data.remote.api


import android.util.Log
import com.google.gson.GsonBuilder
import com.google.gson.JsonParser
import com.google.gson.JsonSyntaxException
import okhttp3.logging.HttpLoggingInterceptor
import javax.inject.Inject

class ApiLogger @Inject constructor(private val gsonBuilder: GsonBuilder) :
    HttpLoggingInterceptor.Logger {
    override fun log(message: String) {
        val logName = "ApiLogger"
        if (message.startsWith("{") || message.startsWith("[")) {
            try {
                val jsonParser = JsonParser()
                val prettyPrintJson = gsonBuilder.serializeNulls().setPrettyPrinting()
                    .create().toJson(jsonParser.parse(message))
                Log.d(logName, "\n$prettyPrintJson")
            } catch (m: JsonSyntaxException) {
                Log.d(logName, message)
            }
        } else {
            Log.d(logName, message)
            return
        }
    }
}