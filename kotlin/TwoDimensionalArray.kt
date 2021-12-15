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

    fun getHorizontalAndVerticalNeighbors(row: Int, col: Int) = sequence {
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

    inline fun mutateAllNeighbors(row: Int, col: Int, mapper: (T) -> T) {
        for (neighborRow in row - 1..row + 1) {
            for (neighborCol in col - 1..col + 1) {
                if (neighborRow to neighborCol in this) {
                    this[neighborRow, neighborCol] = mapper(this[neighborRow, neighborCol])
                }
            }
        }
    }

    inline fun mutateIndexedAllNeighbors(row: Int, col: Int, mapper: (Int, Int, T) -> T) {
        for (neighborRow in row - 1..row + 1) {
            for (neighborCol in col - 1..col + 1) {
                if (neighborRow to neighborCol in this) {
                    this[neighborRow, neighborCol] = mapper(neighborRow, neighborCol, this[neighborRow, neighborCol])
                }
            }
        }
    }

    inline fun <X> mutate(crossinline mapper: (T) -> T) {
        for (row in 0 until rows) {
            for (col in 0 until cols) {
                this[row, col] = mapper(this[row, col])
            }
        }
    }

    inline fun <X> mutateIndexed(crossinline mapper: (Int, Int, T) -> T) {
        for (row in 0 until rows) {
            for (col in 0 until cols) {
                this[row, col] = mapper(row, col, this[row, col])
            }
        }
    }

    inline fun <X> map(crossinline mapper: (T) -> X) =
        TwoDimensionalArray(rows, cols) { row, col -> mapper(get(row, col)) }

    inline fun <X> mapIndexed(crossinline mapper: (Int, Int, T) -> X) =
        TwoDimensionalArray(rows, cols) { row, col -> mapper(row, col, get(row, col)) }
}