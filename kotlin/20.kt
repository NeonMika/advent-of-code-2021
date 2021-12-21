class Day20 : Day<Day20.ImageAndLookup>("20") {
    data class ImageAndLookup(val image: TwoDimensionalArray<Char>, val lookup: String)

    var infinity = '.'
    private val emptyBorder = 100

    @OptIn(ExperimentalStdlibApi::class)
    fun <A> listsOf(a: A, lines: Int = emptyBorder, width: Int) =
        buildList { repeat(lines) { add(buildList { repeat(width) { add(a) } }) } }

    fun r(str: String, n: Int) = str.repeat(n)

    fun adjustedImage(imageLines: List<String>) =
        listsOf('.', emptyBorder, 2 * emptyBorder + imageLines[0].length) +
                imageLines.map { (r(".", emptyBorder) + it + r(".", emptyBorder)).toList() } +
                listsOf('.', emptyBorder, 2 * emptyBorder + imageLines[0].length)


    override fun dataStar1(lines: List<String>): ImageAndLookup {
        infinity = '.'
        return ImageAndLookup(
            TwoDimensionalArray(adjustedImage(lines.filter(String::isNotBlank).drop(1))),
            lines.first()
        )
    }

    private fun enhance(data: ImageAndLookup): ImageAndLookup {
        val new = data.copy(image = data.image.map2DIndexed { row, col, _ ->
            data.lookup[buildString {
                for (r in row - 1..row + 1) {
                    for (c in col - 1..col + 1) {
                        append(if ((if (r to c !in data.image) infinity else data.image[r, c]) == '.') '0' else '1')
                    }
                }
            }.toInt(2)]
        })
        infinity = if (infinity == '.') data.lookup[0] else data.lookup[511]
        return new
    }

    override fun star1(data: ImageAndLookup) = enhance(enhance(data)).image.count { it == '#' }

    override fun star2(data: ImageAndLookup) = (1..50).fold(data) { acc, _ -> enhance(acc) }.image.count { it == '#' }
}

fun main() {
    Day20()()
}