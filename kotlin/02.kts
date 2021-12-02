import java.io.File

// General helper functions
fun <A> readData(day: String, stars: String, lineMapper: (String) -> A) =
    File("../data/${day}_$stars.txt")
        .readLines()
        .map { lineMapper(it) }

fun filePostfix(star: Int, test: Boolean) = if (test) "test" else ("0$star")
fun output(star: Int) {
    println("##############")
    println("Day $day, Star $star")
    println("##############")
    println("Test data: ${data(star, true)}")
    println("Result (test): ${if (star == 1) star1(true) else star2(true)}")
    println("Result (real): ${if (star == 1) star1() else star2()}")
    println()
}

// Settings
val day = "02"

// Day 2: Classes
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

// Day 02: Data
fun data(star: Int, test: Boolean = false) =
    readData(day, filePostfix(star, test)) { line -> line.split(" ").let { Instruction(it[0], it[1].toInt()) } }

// Day 02: 1 Star
fun star1(test: Boolean = false) = data(1, test).fold(Submarine()) { sub, i -> sub.next(i) }.result
output(1)

// Day 02, 2 Stars
fun star2(test: Boolean = false) = data(2, test).fold(ExtendedSubmarine()) { sub, i -> sub.next(i) }.result
output(2)