import java.io.File
import kotlin.system.measureTimeMillis

val Boolean.int
    get() = if (this) 1 else 0

fun List<String>.strings(sep: String = " ") = this.map { it.strings(sep) }
fun List<String>.ints(sep: String = " ") = this.map { it.ints(sep) }
fun List<String>.longs(sep: String = " ") = this.map { it.longs(sep) }
fun List<String>.chars(sep: String = " ") = this.map { it.chars(sep) }

fun String.strings(sep: String = " ") =
    split(sep).map { it.trim() }.filter { it.isNotBlank() }

fun String.ints(sep: String = " ") =
    split(sep).map { it.trim() }.filter { it.isNotBlank() }.map { it.toInt() }

fun String.longs(sep: String = " ") =
    split(sep).map { it.trim() }.filter { it.isNotBlank() }.map { it.toLong() }

fun String.chars(sep: String = " ") =
    split(sep).map(String::trim).filter(String::isNotBlank).flatMap(String::asIterable)



