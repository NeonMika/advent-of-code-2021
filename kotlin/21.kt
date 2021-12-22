import java.lang.Integer.min

class Day21 : Day<Day21.Board>("21") {
    data class Player(var position: Int, var score: Int = 0) {
        var index
            get() = position - 1
            set(value) {
                position = value + 1
            }

        fun mutatingStep(dist: Int, boardLen: Int = 10) {
            index = (index + dist) % boardLen
            score += position
        }

        fun immutableStep(dist: Int, boardLen: Int = 10) =
            (((index + dist) % boardLen) + 1).let { newPos ->
                copy(position = newPos, score = score + newPos)
            }
    }

    data class Board(val player1: Player, val player2: Player, val len: Int = 10)

    class HundredDie {
        var rolls = 0
        var cur = 1
        fun next(): Int {
            var sum = 0
            repeat(3) {
                sum += cur
                cur++
                if (cur == 101) cur = 1
            }
            rolls += 3
            return sum
        }
    }

    override fun dataStar1(lines: List<String>) =
        """Player \d starting position: (\d+)""".toRegex().findAll(lines.joinToString("")).toList().let {
            Board(Player(it[0].groupValues[1].toInt()), Player(it[1].groupValues[1].toInt()))
        }

    override fun star1(data: Board): Any {
        var moveP1 = true
        val die = HundredDie()
        while (data.player1.score < 1000 && data.player2.score < 1000) {
            (if (moveP1) data.player1 else data.player2).mutatingStep(die.next())
            moveP1 = !moveP1
        }
        return min(data.player1.score, data.player2.score) * die.rolls
    }

    override fun star2(data: Board): Any {
        var moveP1 = true
        var universes = mapOf(data to 1L)
        while (universes.count { (board, _) -> board.player1.score < 21 && board.player2.score < 21 } > 0) {
            universes = universes.flatMap { (board, n) ->
                if (board.player1.score < 21 && board.player2.score < 21) {
                    if (moveP1) {
                        listOf(
                            board.copy(player1 = board.player1.immutableStep(3)) to n * 1,
                            board.copy(player1 = board.player1.immutableStep(4)) to n * 3,
                            board.copy(player1 = board.player1.immutableStep(5)) to n * 6,
                            board.copy(player1 = board.player1.immutableStep(6)) to n * 7,
                            board.copy(player1 = board.player1.immutableStep(7)) to n * 6,
                            board.copy(player1 = board.player1.immutableStep(8)) to n * 3,
                            board.copy(player1 = board.player1.immutableStep(9)) to n
                        )
                    } else {
                        listOf(
                            board.copy(player2 = board.player2.immutableStep(3)) to n * 1,
                            board.copy(player2 = board.player2.immutableStep(4)) to n * 3,
                            board.copy(player2 = board.player2.immutableStep(5)) to n * 6,
                            board.copy(player2 = board.player2.immutableStep(6)) to n * 7,
                            board.copy(player2 = board.player2.immutableStep(7)) to n * 6,
                            board.copy(player2 = board.player2.immutableStep(8)) to n * 3,
                            board.copy(player2 = board.player2.immutableStep(9)) to n
                        )
                    }
                } else {
                    listOf(board to n)
                }
            }.groupingBy { it.first }.fold(0L) { sum, (_, n) -> sum + n }
            //println(universes)
            //println()
            moveP1 = !moveP1
        }
        val p1Wins = universes.entries.sumOf { (board, n) -> if(board.player1.score > board.player2.score) n else 0 }
        val p2Wins = universes.entries.sumOf { (board, n) -> if(board.player2.score > board.player1.score) n else 0 }
        return p1Wins to p2Wins
    }
}

fun main() {
    Day21()()
}