class Day3 : Day<List<Day3.BitString>>("03") {
    // Classes
    data class BitString(val str: String) : CharSequence by str {
        val int: Int = str.toInt(2)
    }

    data class Counter(val zeros: Int = 0, val ones: Int = 0) {
        /**
         * Least frequent symbol, equal not defined
         */
        val epsilonRate: Char
            get() = if (zeros > ones) '1' else '0'

        /**
         * Most frequent symbol, equal not defined
         */
        val gammaRate: Char
            get() = if (zeros > ones) '0' else '1'

        /**
         * Most frequent symbol, 1 if equal
         */
        val oxygenGeneratorRating: Char
            get() = if (ones >= zeros) '1' else '0'

        /**
         * Least frequent symbol, 0 if equal
         */
        val co2ScrubberRating: Char
            get() = if (zeros <= ones) '0' else '1'

        operator fun plus(ch: Char) = Counter(zeros + (ch == '0').int, ones + (ch == '1').int)
    }

    // Extension methods
    fun Iterable<BitString>.counters() = fold(Array(first().length) { Counter() }.toList()) { l, x ->
        l.mapIndexed { i, e -> e + x[i] }
    }

    fun List<BitString>.leftToRightSelection(matcher: (Char, Counter) -> Boolean): BitString {
        var data = this.toList()
        for (i in 0..data[0].lastIndex) {
            val counters = data.counters()

            data = data.filter { matcher(it[i], counters[i]) }
            if (data.size == 1) {
                break
            }
        }
        return data.single()
    }

    val Iterable<Counter>.epsilonRate
        get() = BitString(map { it.gammaRate }.joinToString(""))
    val Iterable<Counter>.gammaRate
        get() = BitString(map { it.epsilonRate }.joinToString(""))

    // Data
    override fun dataStar1(lines : List<String>) = lines.map(::BitString)
    override fun dataStar2(lines : List<String>) = lines.map(::BitString)

    // 1 Star
    override fun star1(data: List<BitString>): Number {
        val counters = data.counters()

        val gammaRate = counters.gammaRate
        val epsilonRate = counters.epsilonRate

        return gammaRate.int * epsilonRate.int
    }

    // 2 Stars
    override fun star2(data: List<BitString>): Number {
        val oxygenGeneratorRating = data.leftToRightSelection { ch, counter -> ch == counter.oxygenGeneratorRating }

        val co2ScrubberRating = data.leftToRightSelection { ch, counter -> ch == counter.co2ScrubberRating }

        return oxygenGeneratorRating.int * co2ScrubberRating.int
    }
}

fun main() {
    Day3()()
}