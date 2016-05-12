fun List<String>.parse(
        exprQueue: List<String> = emptyList(),
        opStack: List<String> = emptyList()
): (Map<String, Double>) -> Double {
    if (isEmpty()) {
        if ("(" in opStack)
            throw IllegalArgumentException()

        return (exprQueue + opStack).constructExpression()
    }

    val token = first()
    val rest = subList(1, size)

    if (token in operations) {
        val lastPar = opStack.indexOfFirst {
            it !in operations || operations[it]!!.second < operations[token]!!.second
        }

        val ops = if (lastPar == -1)
            opStack
        else
            opStack.subList(0, lastPar)

        val remStack = if (lastPar == -1)
            emptyList()
        else
            opStack.subList(lastPar, opStack.size)

        return rest.parse(exprQueue + ops, token and remStack)
    }


    if (token == "(")
        return rest.parse(exprQueue, token and opStack)

    if (token == ")") {
        val lastPar = opStack.indexOf("(")

        val ops = opStack.subList(0, lastPar)
        val remStack = if (lastPar < opStack.lastIndex)
            opStack.subList(lastPar, opStack.size)
        else
            emptyList()

        return rest.parse(exprQueue + ops, remStack)
    }

    return rest.parse(exprQueue + token, opStack)
}

fun List<String>.constructExpression(
        constructed: List<(Map<String, Double>) -> Double> = emptyList()
): (Map<String, Double>) -> Double {
    if (isEmpty())
        if (constructed.size == 1)
            return constructed[0]
        else
            throw IllegalArgumentException()

    val token = first()
    val rest = subList(1, size)


    if (token in operations) {
        val gen = operations[token]!!.first
        return rest.constructExpression(gen(constructed[1], constructed[0]) and (constructed.subList(2, constructed.size)))
    }

    try {
        val num = token.toDouble()
        return rest.constructExpression(constant(num) and constructed)
    } catch (e: NumberFormatException) {

    }

    return rest.constructExpression(variable(token) and constructed)
}

infix fun<T> T.and(list: List<T>) = listOf(this) + list
