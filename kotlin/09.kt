class Day9 : Day<TwoDimensionalArray<Int>>("09") {
    override fun dataStar1(lines: List<String>): TwoDimensionalArray<Int> =
        TwoDimensionalArray(lines.map { line -> line.map(Char::digitToInt) })

    override fun dataStar2(lines: List<String>): TwoDimensionalArray<Int> = dataStar1(lines)

    override fun star1(data: TwoDimensionalArray<Int>): Number =
        data.with2DIndex().filter { (row, col, v) -> data.getHorizontalAndVerticalNeighbors(row, col).all { it > v } }.sumOf { it.value + 1 }

    fun flood(row: Int, col: Int, data: TwoDimensionalArray<Int>, filled: TwoDimensionalArray<Boolean>): Int {
        return if (row !in 0 until data.rows || col !in 0 until data.cols || filled[row, col]) 0
        else {
            filled[row, col] = true
            1 + flood(row - 1, col, data, filled) +
                    flood(row + 1, col, data, filled) +
                    flood(row, col - 1, data, filled) +
                    flood(row, col + 1, data, filled)
        }
    }

    override fun star2(data: TwoDimensionalArray<Int>): Number {
        val filled = data.map { it == 9 }
        return data.indices()
            .map { (row, col) -> flood(row, col, data, filled) }
            .sortedDescending()
            .take(3)
            .toList()
            .let { (a, b, c) -> a * b * c }
    }
}

fun main() {
    Day9()()
}