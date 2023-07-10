package dev.fathony.currencyexchange.library.api

class WrappedRequestException(val underlying: Throwable) : RequestException()
