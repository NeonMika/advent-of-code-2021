class Day11 : Day<TwoDimensionalArray<Int>, TwoDimensionalArray<Int>>("11") {
    override fun dataStar1(lines: List<String>): TwoDimensionalArray<Int> =
        TwoDimensionalArray(lines.map { it.toCharArray().map(Char::digitToInt) })

    override fun dataStar2(lines: List<String>): TwoDimensionalArray<Int> = dataStar1(lines)

    override fun star1(data: TwoDimensionalArray<Int>): Number {
        var cur = data
        var incs = 0
        repeat(100) {
            var localIncs = 0
            cur = cur.map { it + 1 }
            while (cur.any { it > 9 }) {
                for (row in 0 until cur.rows) {
                    for (col in 0 until cur.cols) {
                        if (cur[row, col] > 9) {
                            cur[row, col] = Integer.MIN_VALUE
                            localIncs++
                            for (drow in -1..1) {
                                for (dcol in -1..1) {
                                    if (row + drow to col + dcol in cur) {
                                        cur[row + drow, col + dcol]++
                                    }
                                }
                            }
                        }
                    }
                }
            }
            cur = cur.map { if (it < 0) 0 else it }
            incs += localIncs
        }
        return incs
    }

    override fun star2(data: TwoDimensionalArray<Int>): Number {
        var cur = data
        var incs = 0
        repeat(1_000_000) { runIndex ->
            var localIncs = 0
            cur = cur.map { it + 1 }
            while (cur.any { it > 9 }) {
                for (row in 0 until cur.rows) {
                    for (col in 0 until cur.cols) {
                        if (cur[row, col] > 9) {
                            cur[row, col] = Integer.MIN_VALUE
                            localIncs++
                            for (drow in -1..1) {
                                for (dcol in -1..1) {
                                    if (row + drow to col + dcol in cur) {
                                        cur[row + drow, col + dcol]++
                                    }
                                }
                            }
                        }
                    }
                }
            }
            cur = cur.map { if (it < 0) 0 else it }
            incs += localIncs
            if (localIncs == 100) {
                return runIndex + 1
            }
        }
        return -1
    }
}

fun main() {
    Day11()()
}