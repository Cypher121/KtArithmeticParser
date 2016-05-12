fun main(args: Array<String>) {
    val expr = readString().split(' ').normalize().parse()
    val vars = readString().split(' ').filter(String::isNotBlank).associate {
        it.split('=')[0] to it.split('=')[1].toDouble()
    }

    println(expr(vars))
}

fun readString(): String = readLine().run {
    if (this == null || isBlank())
        readString()
    else
        this
}

fun List<String>.normalize() = flatMap { it.splitTokens() }.run {
    if (first() == "(" && last() == ")")
        subList(1, lastIndex)
    else
        this
}

fun String.splitTokens(): List<String> {
    if (isBlank()) return emptyList()

    val tokenIndex = indexOfAny(
            "()+-/*".toCharArray()
    )

    if (tokenIndex < 0) return listOf(this)

    return substring(0, tokenIndex).splitTokens() +
            this[tokenIndex].toString() +
            substring(tokenIndex + 1).splitTokens()
}