class Day4 : Day<Day4.Input, Day4.Input>("04") {
    // Classes
    data class BingoNumber(val num: Int, var drawn: Boolean = false) {
        constructor(str: String) : this(str.toInt())
    }

    open class TwoDimensionalArray<T>(
        val rows: Int = 0,
        val cols: Int = 0,
        val initFn: ((row: Int, col: Int) -> T)
    ) {
        @OptIn(ExperimentalStdlibApi::class)
        protected val data = buildList(rows) {
            repeat(rows) { row -> add(buildList(cols) { repeat(cols) { col -> add(initFn(row, col)) } }) }
        }

        val flat = data.flatten()

        constructor(d: List<List<T>>) : this(d.size, d.firstOrNull()?.size ?: 0, { r, c -> d[r][c] })

        operator fun get(row: Int, col: Int) = data[row][col]

        override fun toString(): String = data.toString()
    }

    class Board(d: List<List<BingoNumber>>) : TwoDimensionalArray<BingoNumber>(d) {
        val isSolved
            get() = isSolvedViaRow || isSolvedViaColumn
        val isSolvedViaRow
            get() = data.any { row -> row.all(BingoNumber::drawn) }
        val isSolvedViaColumn
            get() = (0 until cols).any { col -> data.all { row -> row[col].drawn } }

        val undrawnSum
            get() = flat.filter { !it.drawn }.map { it.num }.sum()
    }

    data class Input(val numbers: List<Int>, val boards: List<Board>)

    // Data
    private fun data(star: Int, test: Boolean): Input {
        val readLines = readData<String>(star, test).filter { it.isNotBlank() }
        val numbers = readLines[0].split(",").map(String::toInt)
        val boards =
            readLines
                .drop(1)
                .chunked(5)
                .map { boardLines -> boardLines.map { line -> line.parts().map(::BingoNumber) } }
                .map(::Board)
        return Input(numbers, boards)
    }

    override fun dataStar1(test: Boolean): Input = data(1, test)
    override fun dataStar2(test: Boolean): Input = data(2, test)

    // 1 Star
    override fun star1(test: Boolean, data: Input): Int {
        var finalNumber = 0
        for (n in data.numbers) {
            for (board in data.boards) {
                board.flat.find { it.num == n }?.drawn = true
            }
            if (data.boards.any { it.isSolved }) {
                finalNumber = n
                break
            }
        }
        val solved = data.boards.first { it.isSolved }
        return finalNumber * solved.undrawnSum
    }

    // 2 Stars
    override fun star2(test: Boolean, data: Input): Int {
        var finalNumber = 0
        var finalBoard: Board? = null
        for (n in data.numbers) {
            for (board in data.boards) {
                board.flat.find { it.num == n }?.drawn = true
            }
            if (data.boards.count { it.isSolved } == data.boards.count() - 1) {
                finalBoard = data.boards.single { !it.isSolved }
            }
            if (data.boards.all { it.isSolved }) {
                finalNumber = n
                break
            }
        }
        return finalNumber * (finalBoard?.undrawnSum ?: error("here be dragon squids"))
    }
}

// main
fun main() {
    Day4()()
}