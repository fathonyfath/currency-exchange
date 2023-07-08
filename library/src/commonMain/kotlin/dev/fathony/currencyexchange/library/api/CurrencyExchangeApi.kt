package dev.fathony.currencyexchange.library.api

import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*

class CurrencyExchangeApi {

    private val client = HttpClient {
        install(ContentNegotiation) {
            json()
        }
    }
    private val baseUrl = Url("https://cdn.jsdelivr.net/gh/fawazahmed0/currency-api@1/latest/")

    suspend fun getCurrencies(): Map<String, String> {
        val currenciesEndpoint = URLBuilder(baseUrl)
            .appendPathSegments("/currencies.json")
            .build()

        val result = client.get(currenciesEndpoint)
        return result.body<Map<String, String>>()
    }

    suspend fun getRates() {

    }

    suspend fun getRate() {

    }
}
