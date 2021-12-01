import java.io.File

// Level 01, 1 star
println(
    File("../data/01_01.txt")
        .readLines()
        .map { it.toInt() }
        .fold(-1 to -1) { res, next -> next to res.second + if (next > res.first) 1 else 0 }
        .second
)

// Level 01, 2 start
println(
    File("../data/01_02.txt")
        .readLines()
        .map { it.toInt() }
        .run {
            dropLast(2).foldIndexed(mutableListOf<Int>()) { i, list, _ ->
                list.also { it += subList(i, i + 3).sum() }
            }
        }
        .fold(-1 to -1) { res, next -> next to res.second + if (next > res.first) 1 else 0 }
        .second)