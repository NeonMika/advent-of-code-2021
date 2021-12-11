import java.io.File
import kotlin.system.measureTimeMillis

val Boolean.int
    get() = if (this) 1 else 0

fun String.parts(sep: String = " ") = split(sep).filter { it.isNotBlank() }

open class TwoDimensionalArray<T>(
    val rows: Int = 0,
    val cols: Int = 0,
    val initFn: ((row: Int, col: Int) -> T)
) : Iterable<T> {
    @OptIn(ExperimentalStdlibApi::class)
    protected val data = buildList(rows) {
        repeat(rows) { row -> add(buildList(cols) { repeat(cols) { col -> add(initFn(row, col)) } }.toMutableList()) }
    }

    val flat = data.flatten()

    constructor(d: List<List<T>>) : this(d.size, d.firstOrNull()?.size ?: 0, { r, c -> d[r][c] })

    operator fun set(row: Int, col: Int, v: T) {
        data[row][col] = v
    }

    operator fun get(row: Int, col: Int) = data[row][col]

    override fun toString(): String = data.toString()
    override fun iterator(): Iterator<T> = iterator {
        repeat(rows) { row ->
            repeat(cols) { col ->
                yield(get(row, col))
            }
        }
    }

    operator fun contains(pos: Pair<Int, Int>) = pos.first in 0 until rows && pos.second in 0 until cols

    data class Index(val row: Int, val col: Int)

    fun indices() = sequence {
        repeat(rows) { row ->
            repeat(cols) { col ->
                yield(Index(row, col))
            }
        }
    }


    data class IndexedElement<T>(val row: Int, val col: Int, val value: T)

    fun with2DIndex() = sequence {
        repeat(rows) { row ->
            repeat(cols) { col ->
                yield(IndexedElement(row, col, get(row, col)))
            }
        }
    }

    fun horVertNeighbors(row: Int, col: Int) = sequence {
        if (row !in 0 until rows || col !in 0 until cols) return@sequence
        if (row - 1 in 0 until rows) {
            yield(get(row - 1, col))
        }
        if (row + 1 in 0 until rows) {
            yield(get(row + 1, col))
        }
        if (col - 1 in 0 until cols) {
            yield(get(row, col - 1))
        }
        if (col + 1 in 0 until cols) {
            yield(get(row, col + 1))
        }
    }

    fun <X> map(mapper: (T) -> X) = TwoDimensionalArray(rows, cols) { row, col -> mapper(get(row, col)) }
    fun <X> mapIndexed(mapper: (Int, Int, T) -> X) =
        TwoDimensionalArray(rows, cols) { row, col -> mapper(row, col, get(row, col)) }
}

abstract class Day<D1, D2>(val day: String) {
    abstract fun dataStar1(lines: List<String>): D1
    abstract fun dataStar2(lines: List<String>): D2

    fun readData(star: Int, test: Boolean) = readData(filePostfix(star, test))
    fun readData(stars: String) = File("data/${day}_$stars.txt").readLines().filter { it.isNotBlank() }

    fun filePostfix(star: Int, test: Boolean) = if (test) "test" else ("0$star")
    fun output(star: Int) {
        println("##############")
        println("Day $day, Star $star")
        println("##############")
        println("Data (test): ${if (star == 1) dataStar1(readData(1, true)) else dataStar2(readData(2, true))}")
        if (star == 1) {
            var testResult: Number
            val testTime = measureTimeMillis {
                testResult = star1(true)
            }
            println("Result (test - main): $testResult in ${testTime}ms")
            var realResult: Number
            val realTime = measureTimeMillis {
                realResult = star1()
            }
            println("Result (real - main): $realResult in ${realTime}ms")

            for ((i, addSol) in additionalStar1Solutions.withIndex()) {
                var testResult: Number
                val testTime = measureTimeMillis {
                    testResult = addSol(dataStar1(readData(1, true)))
                }
                println("Result (test - additional solution #$i): $testResult in ${testTime}ms")
                var realResult: Number
                val realTime = measureTimeMillis {
                    realResult = addSol(dataStar1(readData(1, false)))
                }
                println("Result (real - additional solution #$i): $realResult in ${realTime}ms")
            }
        } else {
            var testResult: Number
            val testTime = measureTimeMillis {
                testResult = star2(true)
            }
            println("Result (test - main): $testResult in ${testTime}ms")
            var realResult: Number
            val realTime = measureTimeMillis {
                realResult = star2()
            }
            println("Result (real - main): $realResult in ${realTime}ms")

            for ((i, addSol) in additionalStar2Solutions.withIndex()) {
                var testResult: Number
                val testTime = measureTimeMillis {
                    testResult = addSol(dataStar2(readData(2, true)))
                }
                println("Result (test - additional solution #${i + 1}): $testResult in ${testTime}ms")
                var realResult: Number
                val realTime = measureTimeMillis {
                    realResult = addSol(dataStar2(readData(2, false)))
                }
                println("Result (real - additional solution #${i + 1}): $realResult in ${realTime}ms")
            }
        }
        println()
    }

    protected open val additionalStar1Solutions: List<(D1) -> Number>
        get() = emptyList()
    protected open val additionalStar2Solutions: List<(D2) -> Number>
        get() = emptyList()

    protected abstract fun star1(data: D1): Number

    protected abstract fun star2(data: D2): Number

    private fun star1(test: Boolean = false) = star1(dataStar1(readData(1, test)))

    private fun star2(test: Boolean = false) = star2(dataStar2(readData(2, test)))

    operator fun invoke() {
        output(1)
        output(2)
    }
}