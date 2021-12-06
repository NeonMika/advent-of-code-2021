class Day2 : Day<List<Day2.Instruction>, List<Day2.Instruction>>("02") {
    // Classes
    data class Instruction(val dir: String, val dist: Int)

    data class Submarine(val x: Int = 0, val y: Int = 0) {
        val result: Int
            get() = x * y

        fun next(i: Instruction) = when (i.dir) {
            "forward" -> copy(x = x + i.dist)
            "up" -> copy(y = y - i.dist)
            "down" -> copy(y = y + i.dist)
            else -> Submarine(-1, -1) // here be dragons
        }
    }

    data class ExtendedSubmarine(val x: Int = 0, val y: Int = 0, val aim: Int = 0) {
        val result: Int
            get() = x * y

        fun next(i: Instruction) = when (i.dir) {
            "forward" -> copy(x = x + i.dist, y = y + i.dist * aim)
            "up" -> copy(aim = aim - i.dist)
            "down" -> copy(aim = aim + i.dist)
            else -> ExtendedSubmarine(-1, -1) // here be dragons
        }
    }

    // Data
    override fun dataStar1(lines: List<String>) = lines.map { it.split(" ").let { Instruction(it[0], it[1].toInt()) } }
    override fun dataStar2(lines: List<String>) = lines.map { it.split(" ").let { Instruction(it[0], it[1].toInt()) } }

    // 1 Star
    override fun star1(data: List<Instruction>): Number =
        data.fold(Submarine()) { sub, i -> sub.next(i) }.result

    // Day 02, 2 Stars
    override fun star2(data: List<Instruction>): Number =
        data.fold(ExtendedSubmarine()) { sub, i -> sub.next(i) }.result
}

fun main() {
    Day2()()
}