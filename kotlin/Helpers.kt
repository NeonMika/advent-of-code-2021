import java.io.File
import kotlin.system.measureTimeMillis

fun <A> readData(day: String, stars: String, lineMapper: (String) -> A) =
    File("data/${day}_$stars.txt").readLines().map { lineMapper(it) }

fun filePostfix(star: Int, test: Boolean) = if (test) "test" else ("0$star")
fun output(day: String, star: Int, f: (test: Boolean) -> Int) {
    println("##############")
    println("Day $day, Star $star")
    println("##############")
    var testResult : Int
    val testTime = measureTimeMillis {
        testResult = f(true)
    }
    println("Result (test): $testResult in ${testTime}ms")
    var realResult : Int
    val realTime = measureTimeMillis {
        realResult = f(false)
    }
    println("Result (real): $realResult in ${realTime}ms")
    println()
}

val Boolean.int
    get() = if (this) 1 else 0