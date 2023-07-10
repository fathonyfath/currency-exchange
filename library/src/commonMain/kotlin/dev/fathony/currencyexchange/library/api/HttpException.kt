package dev.fathony.currencyexchange.library.api

class HttpException(val code: Int, override val message: String) : RequestException()
