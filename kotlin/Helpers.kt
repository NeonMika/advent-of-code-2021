import java.io.File
import kotlin.system.measureTimeMillis

val Boolean.int
    get() = if (this) 1 else 0

fun String.parts(sep : String = " ") = split(sep).filter { it.isNotBlank() }

abstract class Day<DATA>(val day: String) {
    abstract fun data(star: Int, test: Boolean = false): DATA

    inline fun <reified A> readData(stars: String, lineMapper: (String) -> A = { it as A }) =
        File("data/${day}_$stars.txt").readLines().map { lineMapper(it) }

    fun filePostfix(star: Int, test: Boolean) = if (test) "test" else ("0$star")
    fun output(star: Int) {
        println("##############")
        println("Day $day, Star $star")
        println("##############")
        println("Data (test): ${data(star, true)}")
        val f = if (star == 1) ::star1 else ::star2
        var testResult: Int
        val testTime = measureTimeMillis {
            testResult = f(true)
        }
        println("Result (test): $testResult in ${testTime}ms")
        var realResult: Int
        val realTime = measureTimeMillis {
            realResult = f(false)
        }
        println("Result (real): $realResult in ${realTime}ms")
        println()
    }

    abstract fun star1(test: Boolean = false): Int

    abstract fun star2(test: Boolean = false): Int

    operator fun invoke() {
        output(1)
        output(2)
    }
}