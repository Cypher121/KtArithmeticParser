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

fun List<String>.normalize(): List<String> = flatMap(String::splitTokens)

fun String.splitTokens(): List<String> {
    if (isBlank()) return emptyList()

    val tokenIndex = indexOfAny(
        "()+-/*".toCharArray()
    )

    if (tokenIndex < 0) return listOf(this)

    return take(tokenIndex).splitTokens() +
           this[tokenIndex].toString() +
           drop(tokenIndex + 1).splitTokens()
}
