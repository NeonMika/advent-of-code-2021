class Day6 : Day<LongArray>("06") {
    override fun dataStar1(lines: List<String>): LongArray =
        lines[0].split(",").map(String::toInt).fold(LongArray(9)) { arr, i -> arr.also { it[i]++ } }
    override fun dataStar2(lines: List<String>) = dataStar1(lines)

    fun LongArray.step(days: Int) = (1..days).fold(this) { arr, _ -> LongArray(9) { i -> arr[(i + 1) % 9] + (if (i == 6) arr[0] else 0) } }.sum()
    override fun star1(data: LongArray): Number = data.step(80)
    override fun star2(data: LongArray): Number = data.step(256)
}

fun main() {
    Day6()()
}