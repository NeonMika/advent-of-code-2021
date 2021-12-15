import kotlin.reflect.full.createInstance

fun main() {
    repeat(25) { dayMin1 ->
        val nDay = dayMin1 + 1
        try {
            val day = Class.forName("Day$nDay").kotlin.createInstance() as Day<*>
            day()
        } catch (ex: Exception) {
            println("Could not execute day $nDay")
        }
    }
}