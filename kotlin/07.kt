import kotlin.math.absoluteValue

class Day7 : Day<List<Int>, List<Int>>("07") {
    override fun dataStar1(lines: List<String>): List<Int> = lines[0].split(",").map(String::toInt)

    override fun dataStar2(lines: List<String>): List<Int> = dataStar1(lines)

    override fun star1(data: List<Int>): Number = data.minOf { it }.rangeTo(data.maxOf { it })
        .minOfOrNull { pos -> data.sumOf { (it - pos).absoluteValue } } ?: 0

    // Sum 0 to n: n * (n+1) / 2
    override fun star2(data: List<Int>): Number = data.minOf { it }.rangeTo(data.maxOf { it })
        .minOf { pos -> data.map { crab -> (crab - pos).absoluteValue }.sumOf { diff -> diff * (diff + 1) / 2 } }

    // Sum 0 to n: (0..n).sum()
    override val additionalStar2Solutions: List<(List<Int>) -> Number>
        get() = listOf { data ->
            data.minOf { it }.rangeTo(data.maxOf { it })
                .minOf { pos -> data.sumOf { (0..(it - pos).absoluteValue).sum() } }
        }
}

fun main() {
    Day7()()
}