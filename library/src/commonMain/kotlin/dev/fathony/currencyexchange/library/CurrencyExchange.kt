package dev.fathony.currencyexchange.library

abstract class CurrencyExchange {

    abstract suspend fun getCurrencies(): Set<Currency>
    abstract suspend fun getRates(base: String): List<Rate>
    abstract suspend fun getRate(from: String, to: String): Rate?
}