package com.slide.test.network.errors

import com.slide.test.core.errors.ApiError
import com.slide.test.core.errors.GoRestApiException
import okhttp3.Interceptor
import okhttp3.Interceptor.Chain
import okhttp3.Request
import okhttp3.Response
import okhttp3.ResponseBody
import okhttp3.ResponseBody.Companion.asResponseBody
import okio.Buffer
import org.json.JSONException
import org.json.JSONObject
import java.io.IOException


/**
 * Created by Stefan Halus on 22 May 2022
 */
class ErrorHandlingInterceptor : Interceptor {

    override fun intercept(chain: Chain): Response {
        val originalRequest: Request = chain.request()
        val response: Response = chain.proceed(originalRequest)

        val responseBody = copy(response.body, Long.MAX_VALUE)
        val rawJson = try {
            JSONObject(responseBody?.string())
        } catch (e: JSONException) {
            throw GoRestApiException(500, listOf(ApiError("JSON", e.message ?: "")))
        }

        return if (ERROR_CODES.contains(rawJson.getInt("code"))) {
            throw getException(rawJson)
        } else {
            response
        }
    }

    private fun getException(rawJson: JSONObject): Throwable {
        val code = rawJson.getLong("code")
        val errors = rawJson.getJSONArray("data")

        val apiErrors = (0 until  errors.length()).map { index ->
            val error = errors.getJSONObject(index)
            val field = error.getString("field")
            val message = error.getString("message")
            ApiError(field, message)
        }
        return GoRestApiException(code, apiErrors)
    }

    @Throws(IOException::class)
    private fun copy(body: ResponseBody?, limit: Long): ResponseBody? {
        if (body == null) return body
        val source = body.source()
        if (source.request(limit)) throw IOException("body too long!")
        val bufferedCopy: Buffer = source.buffer.clone()
        return bufferedCopy.asResponseBody(body.contentType(), body.contentLength())
    }

    companion object {
        val ERROR_CODES = 400..500
    }

//    200: OK. Everything worked as expected.
//    201: A resource was successfully created in response to a POST request. The Location header contains the URL pointing to the newly created resource.
//    204: The request was handled successfully and the response contains no body content (like a DELETE request).
}
