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

fun <A, B> Iterable<A>.cross(other: Iterable<B>) = cross(other) { a, b -> a to b }

fun <A, B, C> Iterable<A>.cross(other: Iterable<B>, mapper: (A, B) -> C) =
    this.flatMap { a -> other.map { b -> mapper(a, b) } }

fun <A, B> Iterable<A>.crossNotSelf(other: Iterable<B>) = crossNotSelf(other) { a, b -> a to b }

fun <A, B, C> Iterable<A>.crossNotSelf(other: Iterable<B>, mapper: (A, B) -> C) =
    this.flatMapIndexed { ia, a -> other.filterIndexed { ib, _ -> ia != ib }.map { b -> mapper(a, b) } }

fun <A> Iterable<Iterable<A>>.smallPrint() {
    for (row in this) {
        for (x in row) {
            print(x)
        }
        println()
    }
}