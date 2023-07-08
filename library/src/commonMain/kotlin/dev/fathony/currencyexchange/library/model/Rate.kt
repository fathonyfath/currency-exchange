package dev.fathony.currencyexchange.library.model

class Rate internal constructor(
    val from: Currency,
    val target: Currency,
    val rate: Double,
)
