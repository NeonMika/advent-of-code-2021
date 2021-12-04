class Day1 : Day<List<Int>>("01") {
    override fun data(star: Int, test: Boolean): List<Int> = readData(filePostfix(1, test)) { it.toInt() }

    // 1 star
    override fun star1(test: Boolean) =
        data(1, test)
            .fold(-1 to -1) { res, next -> next to res.second + if (next > res.first) 1 else 0 }
            .second

    // 2 stars
    override fun star2(test: Boolean) =
        data(1, test)
            .windowed(3) { it.sum() }
            .windowed(2) { it[1] > it[0] }
            .count { it }
}

fun main() {
    Day1()()
}