tailrec fun List<String>.parse(
    exprQueue: List<String> = emptyList(),
    opStack: List<String> = emptyList(),
    lastType: TokenType = TokenType.NOTHING
): Expression {
    if (isEmpty()) {
        if ("(" in opStack)
            throw IllegalArgumentException("Mismatched opening parenthesis")

        return (exprQueue + opStack).constructExpression()
    }

    val token = first()
    val rest = drop(1)

    if (token in binaryOperations) {
        if (lastType != TokenType.EXPRESSION) {
            if (token in binaryToUnary) {
                val realToken = binaryToUnary[token]!!
                val lastPar = opStack.indexOfFirst {
                    it.priority < token.priority
                }

                val (ops, remStack) = if (lastPar < 0)
                    opStack to emptyList()
                else
                    opStack.take(lastPar) to opStack.drop(lastPar)

                return rest.parse(exprQueue + ops, realToken and remStack, TokenType.OPERATOR)
            } else {
                throw IllegalArgumentException("Binary operator $token is missing first argument")
            }
        }

        val lastPar = opStack.indexOfFirst {
            it.priority < token.priority
        }

        val (ops, remStack) = if (lastPar < 0)
            opStack to emptyList()
        else
            opStack.take(lastPar) to opStack.drop(lastPar)

        return rest.parse(exprQueue + ops, token and remStack, TokenType.OPERATOR)
    }

    if (token == "(")
        return rest.parse(exprQueue, token and opStack, TokenType.PARENTHESIS)

    if (token == ")") {
        val lastPar = opStack.indexOf("(")

        if (lastPar < 0) {
            throw IllegalArgumentException("Mismatched closing parenthesis")
        }

        val ops = opStack.take(lastPar)
        val remStack = if (lastPar < opStack.lastIndex)
            opStack.drop(lastPar + 1)
        else
            emptyList()

        return rest.parse(exprQueue + ops, remStack, TokenType.EXPRESSION)
    }

    return rest.parse(exprQueue + token, opStack, TokenType.EXPRESSION)
}

tailrec fun List<String>.constructExpression(
    constructed: List<Expression> = emptyList()
): Expression {
    if (isEmpty())
        if (constructed.size == 1)
            return constructed[0]
        else
            throw IllegalArgumentException()

    val token = first()
    val rest = drop(1)


    if (token in binaryOperations && constructed.size > 1) {
        val gen = binaryOperations[token]!!.first
        return rest.constructExpression(gen(constructed[1], constructed[0]) and (constructed.drop(2)))
    } else {
        if (token in unaryOperations && constructed.isNotEmpty()) {
            val gen = unaryOperations[token]!!.first
            return rest.constructExpression(gen(constructed[0]) and (constructed.drop(1)))
        }
    }

    return rest.constructExpression(token.toTerminal() and constructed)
}

infix fun <T> T.and(list: List<T>) = listOf(this) + list

fun String.toTerminal() = try {
    constant(toDouble())
} catch (e: NumberFormatException) {
    variable(this)
}

enum class TokenType { PARENTHESIS, EXPRESSION, OPERATOR, NOTHING }