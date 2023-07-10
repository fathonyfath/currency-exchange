package dev.fathony.currencyexchange.library.api

import com.github.michaelbull.result.*
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.json.JsonElement

class CurrencyExchangeApi {

    private val client = HttpClient {
        install(ContentNegotiation) {
            json()
        }
    }
    private val baseUrl = Url("https://cdn.jsdelivr.net/gh/fawazahmed0/currency-api@1/latest/")

    suspend fun getCurrencies(): Result<GetCurrencies, RequestException> {
        val currenciesEndpoint = URLBuilder(baseUrl)
            .appendPathSegments("/currencies.json")
            .build()

        return runCatching { client.get(currenciesEndpoint) }
            .processBody<Map<String, String>>()
            .map(GetCurrencies::parse)
            .processError()
    }

    suspend fun getRates(countryCode: String): Result<GetRates, RequestException> {
        val ratesEndpoint = URLBuilder(baseUrl)
            .appendPathSegments("/currencies/$countryCode.json")
            .build()

        return runCatching { client.get(ratesEndpoint) }
            .processBody<Map<String, JsonElement>>()
            .map(GetRates::parse)
            .processError()
    }

    suspend fun getRate(fromCountryCode: String, toCountryCode: String): Result<GetRate, RequestException> {
        val rateEndpoint = URLBuilder(baseUrl)
            .appendPathSegments("/currencies/$fromCountryCode/$toCountryCode.json")
            .build()

        return runCatching { client.get(rateEndpoint) }
            .processBody<Map<String, JsonElement>>()
            .map { GetRate.parse(fromCountryCode, it) }
            .processError()
    }

    private suspend inline fun <reified T> Result<HttpResponse, Throwable>.processBody(): Result<T, Throwable> {
        return this.processHttpResponse()
            .map { it.body<T>() }
    }

    private fun Result<HttpResponse, Throwable>.processHttpResponse(): Result<HttpResponse, Throwable> {
        return this.andThen { response ->
            return@andThen if (response.status.isSuccess()) {
                Ok(response)
            } else {
                Err(HttpException(response.status.value, response.status.description))
            }
        }
    }

    private fun <T> Result<T, Throwable>.processError(): Result<T, RequestException> {
        return this.mapError { throwable ->
            return@mapError when (throwable) {
                is HttpException -> throwable
                else -> WrappedRequestException(throwable)
            }
        }
    }
}
