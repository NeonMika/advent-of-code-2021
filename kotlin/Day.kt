import java.io.File
import kotlin.system.measureTimeMillis

abstract class Day<D : Any>(val day: String) {
    abstract fun dataStar1(lines: List<String>): D
    open fun dataStar2(lines: List<String>) = dataStar1(lines)

    fun readData(star: Int, test: Boolean) = readData(filePostfix(star, test))
    fun readData(stars: String) = File("data/${day}_$stars.txt").readLines().filter { it.isNotBlank() }

    fun filePostfix(star: Int, test: Boolean) = if (test) "test" else ("0$star")
    fun output(star: Int) {
        println("##############")
        println("Day $day, Star $star")
        println("##############")
        println("Data (test):\n${if (star == 1) dataStar1(readData(1, true)) else dataStar2(readData(2, true))}")
        if (star == 1) {
            var testResult: Any?
            val testTime = measureTimeMillis {
                testResult = star1(true)
            }
            println("Result (test - main):\n$testResult in ${testTime}ms")
            var realResult: Any?
            val realTime = measureTimeMillis {
                realResult = star1()
            }
            println("Result (real - main):\n$realResult in ${realTime}ms")

            for ((i, addSol) in additionalStar1Solutions.withIndex()) {
                var testResult: Any
                val testTime = measureTimeMillis {
                    testResult = addSol(dataStar1(readData(1, true)))
                }
                println("Result (test - additional solution #$i):\n$testResult in ${testTime}ms")
                var realResult: Any
                val realTime = measureTimeMillis {
                    realResult = addSol(dataStar1(readData(1, false)))
                }
                println("Result (real - additional solution #$i):\n$realResult in ${realTime}ms")
            }
        } else {
            var testResult: Any?
            val testTime = measureTimeMillis {
                testResult = star2(true)
            }
            println("Result (test - main):\n$testResult in ${testTime}ms")
            var realResult: Any?
            val realTime = measureTimeMillis {
                realResult = star2()
            }
            println("Result (real - main):\n$realResult in ${realTime}ms")

            for ((i, addSol) in additionalStar2Solutions.withIndex()) {
                var testResult: Any
                val testTime = measureTimeMillis {
                    testResult = addSol(dataStar2(readData(2, true)))
                }
                println("Result (test - additional solution #${i + 1}):\n$testResult in ${testTime}ms")
                var realResult: Any
                val realTime = measureTimeMillis {
                    realResult = addSol(dataStar2(readData(2, false)))
                }
                println("Result (real - additional solution #${i + 1}):\n$realResult in ${realTime}ms")
            }
        }
        println()
    }

    protected open val additionalStar1Solutions: List<(D) -> Any>
        get() = emptyList()
    protected open val additionalStar2Solutions: List<(D) -> Any>
        get() = emptyList()

    protected abstract fun star1(data: D): Any?

    protected abstract fun star2(data: D): Any?

    private fun star1(test: Boolean = false) = star1(dataStar1(readData(1, test)))

    private fun star2(test: Boolean = false) = star2(dataStar2(readData(2, test)))

    operator fun invoke() {
        output(1)
        output(2)
    }
}