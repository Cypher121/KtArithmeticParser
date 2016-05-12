fun constant(value: Double) = { values: Map<String, Double> ->
    value
}

fun variable(name: String) = { values: Map<String, Double> ->
    values[name]!!
}

//for fuck's sake, just add typealias already!
fun binary(
        op: (Double, Double) -> Double
) = { first: (Map<String, Double>) -> Double, second: (Map<String, Double>) -> Double ->
    { values: Map<String, Double> ->
        op(first(values), second(values))
    }
}

val operations = mapOf(
        "+" to (binary(Double::plus) to 0),
        "-" to (binary(Double::minus) to 0),
        "/" to (binary(Double::div) to 1),
        "*" to (binary(Double::times) to 1)
)