import java.io.File
import kotlin.system.measureTimeMillis

val Boolean.int
    get() = if (this) 1 else 0

fun String.parts(sep: String = " ") = split(sep).filter { it.isNotBlank() }

abstract class Day<D1, D2>(val day: String) {
    abstract fun dataStar1(test: Boolean = false): D1
    abstract fun dataStar2(test: Boolean = false): D2

    inline fun <reified A> readData(star: Int, test: Boolean, lineMapper: (String) -> A = { it as A }) =
        readData(filePostfix(star, test), lineMapper)

    inline fun <reified A> readData(stars: String, lineMapper: (String) -> A = { it as A }) =
        File("data/${day}_$stars.txt").readLines().map { lineMapper(it) }

    fun filePostfix(star: Int, test: Boolean) = if (test) "test" else ("0$star")
    fun output(star: Int) {
        println("##############")
        println("Day $day, Star $star")
        println("##############")
        println("Data (test): ${if (star == 1) dataStar1(true) else dataStar2(true)}")
        if (star == 1) {
            var testResult: Int
            val testTime = measureTimeMillis {
                testResult = star1(true)
            }
            println("Result (test): $testResult in ${testTime}ms")
            var realResult: Int
            val realTime = measureTimeMillis {
                realResult = star1()
            }
            println("Result (real): $realResult in ${realTime}ms")
        } else {
            var testResult: Int
            val testTime = measureTimeMillis {
                testResult = star2(true)
            }
            println("Result (test): $testResult in ${testTime}ms")
            var realResult: Int
            val realTime = measureTimeMillis {
                realResult = star2()
            }
            println("Result (real): $realResult in ${realTime}ms")
        }
        println()
    }

    protected abstract fun star1(test: Boolean = false, data: D1 = dataStar1(test)): Int

    protected abstract fun star2(test: Boolean = false, data: D2 = dataStar2(test)): Int

    operator fun invoke() {
        output(1)
        output(2)
    }
}