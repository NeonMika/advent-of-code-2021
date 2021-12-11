class Day11 : Day<TwoDimensionalArray<Int>, TwoDimensionalArray<Int>>("11") {
    override fun dataStar1(lines: List<String>): TwoDimensionalArray<Int> =
        TwoDimensionalArray(lines.map { it.toCharArray().map(Char::digitToInt) })

    override fun dataStar2(lines: List<String>): TwoDimensionalArray<Int> = dataStar1(lines)

    fun step(data: TwoDimensionalArray<Int>): Pair<TwoDimensionalArray<Int>, Int> {
        var cur = data
        var localIncs = 0
        cur = cur.map { it + 1 }
        while (cur.any { it > 9 }) {
            for (row in 0 until cur.rows) {
                for (col in 0 until cur.cols) {
                    if (cur[row, col] > 9) {
                        cur[row, col] = Integer.MIN_VALUE
                        localIncs++
                        for (neighborRow in row - 1..row + 1) {
                            for (neighborCol in col - 1..col + 1) {
                                if (neighborRow to neighborCol in cur) {
                                    cur[neighborRow, neighborCol]++
                                }
                            }
                        }
                    }
                }
            }
        }
        return cur.map { if (it < 0) 0 else it } to localIncs
    }

    override fun star1(data: TwoDimensionalArray<Int>): Number {
        var cur = data
        var incs = 0
        repeat(100) {
            step(cur).let { (stepCur, stepIncs) ->
                cur = stepCur
                incs += stepIncs
            }
        }
        return incs
    }

    override fun star2(data: TwoDimensionalArray<Int>): Number {
        var cur = data
        repeat(1_000_000) { runIndex ->
            step(cur).let { (stepCur, stepIncs) ->
                cur = stepCur
                if (stepIncs == 100) return runIndex + 1
            }
        }
        return -1
    }
}

fun main() {
    Day11()()
}