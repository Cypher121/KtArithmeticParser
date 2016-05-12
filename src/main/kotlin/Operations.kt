fun constant(value: Double) = { values: Map<String, Double> ->
    value
}

fun variable(name: String) = { values: Map<String, Double> ->
    values[name]!!
}

fun binary(
        op: (Double, Double) -> Double,
        first: (Map<String, Double>) -> Double,
        second: (Map<String, Double>) -> Double
) = { values: Map<String, Double> ->
    op(first(values), second(values))
}

fun add(
        first: (Map<String, Double>) -> Double,
        second: (Map<String, Double>) -> Double
) = binary(Double::plus, first, second)

fun subtract(
        first: (Map<String, Double>) -> Double,
        second: (Map<String, Double>) -> Double
) = binary(Double::minus, first, second)

fun multiply(
        first: (Map<String, Double>) -> Double,
        second: (Map<String, Double>) -> Double
) = binary(Double::times, first, second)

fun divide(
        first: (Map<String, Double>) -> Double,
        second: (Map<String, Double>) -> Double
) = binary(Double::div, first, second)

