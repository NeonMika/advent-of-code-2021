fun main() {
    // Settings
    val day = "01"

    // 1 star
    fun star1(test: Boolean = false) =
        readData("01", if (test) "test" else "01") { it.toInt() }
            .fold(-1 to -1) { res, next -> next to res.second + if (next > res.first) 1 else 0 }
            .second
    output(day, 1, ::star1)

    // 2 stars
    fun star2(test: Boolean = false) =
        readData("01", if (test) "test" else "02") { it.toInt() }
            .windowed(3) { it.sum() }
            .windowed(2) { it[1] > it[0] }
            .count { it }

    output(day, 2, ::star2)
}