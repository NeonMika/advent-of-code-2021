class Day4 : Day<Day4.Input, Day4.Input>("04") {
    // Classes
    data class BingoNumber(val num: Int, var drawn: Boolean = false)

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
    private fun data(lines: List<String>): Input {
        val numbers = lines[0].split(",").map(String::toInt)
        val boards =
            lines
                .drop(1)
                .chunked(5)
                .map { boardLines -> boardLines.map { line -> line.ints().map(::BingoNumber) } }
                .map(::Board)
        return Input(numbers, boards)
    }

    override fun dataStar1(lines: List<String>): Input = data(lines)
    override fun dataStar2(lines: List<String>): Input = data(lines)

    // 1 Star
    override fun star1(data: Input): Number {
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
    override fun star2(data: Input): Number {
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