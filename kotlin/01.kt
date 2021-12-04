class Day1 : Day<List<Int>, List<Int>>("01") {
    override fun dataStar1(test: Boolean) = readData(1, test) { it.toInt() }
    override fun dataStar2(test: Boolean) = readData(2, test) { it.toInt() }

    // 1 star
    data class Agg(val last: Int = -1, val incCount: Int = -1)

    override fun star1(test: Boolean, data: List<Int>) =
        data.fold(Agg()) { agg, num -> Agg(num, (agg.incCount + if (num > agg.last) 1 else 0)) }.incCount

    // 2 stars
    override fun star2(test: Boolean, data: List<Int>) =
        data.windowed(3) { it.sum() }
            .windowed(2) { it[1] > it[0] }
            .count { it }
}

fun main() {
    Day1()()
}