class Day8 : Day<List<Day8.Input>, List<Day8.Input>>("08") {
    data class Input(val segments: List<String>, val outputs: List<String>)

    enum class SegmentNumber(val segments: String, val int: Int) {
        ZERO("abcefg", 0),
        ONE("cf", 1),
        TWO("acdeg", 2),
        THREE("acdfg", 3),
        FOUR("bcdf", 4),
        FIVE("abdfg", 5),
        SIX("abdefg", 6),
        SEVEN("acf", 7),
        EIGHT("abcdefg", 8),
        NINE("abcdfg", 9);
    }

    val uniqueNumberLengths = setOf(
        SegmentNumber.ONE.segments.length,
        SegmentNumber.FOUR.segments.length,
        SegmentNumber.SEVEN.segments.length,
        SegmentNumber.EIGHT.segments.length
    )

    override fun dataStar1(lines: List<String>): List<Input> =
        lines.map { line -> line.split("|").run { Input(get(0).strings(), get(1).strings()) } }

    override fun dataStar2(lines: List<String>): List<Input> = dataStar1(lines)

    override fun star1(data: List<Input>): Number {
        return data.flatMap { it.outputs }.count { it.length in uniqueNumberLengths }
    }

    override fun star2(data: List<Input>): Number {
        var sum = 0

        for (input in data) {
            val inputCounts = input.segments.flatMap { it.toList() }.groupingBy { it }.eachCount()

            // a: 7 (unique) without 1 (unique)
            val a = (input.segments.find { it.length == 3 }!!.toSet() - input.segments.find { it.length == 2 }!!
                .toSet()).single()
            // b: Only segment that is lit 6 times
            val b = inputCounts.filter { (_, v) -> v == 6 }.map { it.key }.single()
            // c: The second segment besides a that is lit in 8 out of 10 digits
            val c = inputCounts.filter { (k, v) -> v == 8 && k != a }.map { it.key }.single()
            // e: Only segment that is lit 4 times
            val e = inputCounts.filter { (_, v) -> v == 4 }.map { it.key }.single()
            // f: Only segment that is lit 9 times
            val f = inputCounts.filter { (_, v) -> v == 9 }.map { it.key }.single()
            // d: 4 (unique) without b, c and f
            val d = (input.segments.find { it.length == 4 }!!.toSet() - setOf(b, c, f)).single()
            // g: remaining segment
            val g = ("abcdefg".toSet() - setOf(a, b, c, d, e, f)).single()

            val mapping = mapOf(
                a to 'a',
                b to 'b',
                c to 'c',
                d to 'd',
                e to 'e',
                f to 'f',
                g to 'g'
            )

            val fixedOutput = input.outputs.map { wrongNumberSegments: String ->
                SegmentNumber.values().find { segmentNumber ->
                    val fixed = wrongNumberSegments.map { letter -> mapping[letter] }.sortedBy { it }.joinToString("")
                    segmentNumber.segments == fixed
                }!!.int
            }.joinToString("").toInt()

            sum += fixedOutput
        }

        return sum
    }

}

fun main() {
    Day8()()
}