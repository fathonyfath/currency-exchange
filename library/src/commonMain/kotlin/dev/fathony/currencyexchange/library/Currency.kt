package dev.fathony.currencyexchange.library

class Currency internal constructor(
    val code: String,
    val name: String
) : Comparable<Currency> {

    override fun compareTo(other: Currency): Int {
        return this.code.compareTo(other.code)
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || this::class != other::class) return false

        other as Currency

        if (code != other.code) return false
        return name == other.name
    }

    override fun hashCode(): Int {
        var result = code.hashCode()
        result = 31 * result + name.hashCode()
        return result
    }
}