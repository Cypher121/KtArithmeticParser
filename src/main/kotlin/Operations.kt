typealias LiteralValues = Map<String, Double>

typealias Expression = (LiteralValues) -> Double

fun constant(value: Double): Expression = { value }

fun variable(name: String): Expression = {
    it[name] ?: throw IllegalArgumentException("Value of variable $name is not specified")
}

fun binary(
    op: (Double, Double) -> Double
) = { first: Expression, second: Expression ->
    { values: LiteralValues ->
        op(first(values), second(values))
    }
}

fun unary(op: (Double) -> Double) = { arg: Expression ->
    { values: LiteralValues ->
        op(arg(values))
    }
}

val binaryOperations = mapOf(
    "+" to (binary(Double::plus) to 0),
    "-" to (binary(Double::minus) to 0),
    "/" to (binary(Double::div) to 2),
    "*" to (binary(Double::times) to 2)
)

val binaryToUnary = mapOf(
    "+" to "$",
    "-" to "#"
)

val unaryOperations = mapOf(
    "$" to (unary(Double::unaryPlus) to 1),
    "#" to (unary(Double::unaryMinus) to 1)
)

data class Operation(val op: (Expression, Expression) -> (LiteralValues) -> Double)

val String.priority: Int
    get() = if (this in binaryOperations)
        binaryOperations[this]!!.second
    else
        if (this in unaryOperations)
            unaryOperations[this]!!.second
        else
            -1