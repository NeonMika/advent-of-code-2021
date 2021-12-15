class Day13 : Day<Day13.PaperFolding, Day13.PaperFolding>("13") {
    data class FoldInstruction(val axis: Char, val line: Int)

    infix fun Char.foldBy(amount: Int) = FoldInstruction(this, amount)

    data class PaperFolding(val dots: TwoDimensionalArray<Boolean>, val folds: List<FoldInstruction>) {
        val nextFold
            get() = folds.first()

        val nextFoldAxis
            get() = nextFold.axis

        fun fold(): PaperFolding {
            val mergeRows = (0 until dots.rows).associateBy { it }.toMutableMap()
            val mergeCols = (0 until dots.cols).associateBy { it }.toMutableMap()
            if (nextFoldAxis == 'y') {
                (folds[0].line + 1 until dots.rows).forEachIndexed { i, r -> mergeRows[folds[0].line - 1 - i] = r }
            } else {
                (folds[0].line + 1 until dots.cols).forEachIndexed { i, c -> mergeCols[folds[0].line - 1 - i] = c }
            }
            val rows = if (nextFoldAxis == 'y') folds[0].line else dots.rows
            val cols = if (nextFoldAxis == 'y') dots.cols else folds[0].line
            return PaperFolding(TwoDimensionalArray(rows, cols) { row, col ->
                dots[row, col] or dots[mergeRows[row]!!, mergeCols[col]!!]
            }, folds.drop(1))
        }

        override fun toString() = buildString {
            for (row in 0 until dots.rows) {
                for (col in 0 until dots.cols) {
                    append(if (dots[row, col]) "#" else ".")
                }
                appendLine()
            }
        }
    }

    override fun dataStar1(lines: List<String>): PaperFolding {
        val foldPattern = """fold along (.)=(\d+)""".toRegex()
        val positions = lines.takeWhile { !foldPattern.matches(it) }
            .map { it.ints(",").run { get(1) to get(0) } }.toSet()
        val foldInstructions = lines.dropWhile { !foldPattern.matches(it) }
            .map { foldPattern.find(it)!!.groupValues.run { get(1)[0] foldBy get(2).toInt() } }
        return PaperFolding(
            TwoDimensionalArray(positions.maxOf { it.first } + 1, positions.maxOf { it.second } + 1) { row, col ->
                row to col in positions
            }, foldInstructions
        )
    }

    override fun dataStar2(lines: List<String>) = dataStar1(lines)

    override fun star1(data: PaperFolding): Number {
        return data.fold().apply { println(this) }.dots.count { it }
    }

    override fun star2(data: PaperFolding): PaperFolding {
        var cur = data
        while (cur.folds.isNotEmpty()) {
            cur = cur.fold()
        }
        return cur
    }
}

fun main() {
    Day13()()
}