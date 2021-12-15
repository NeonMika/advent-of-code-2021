class Day14 : Day<Day14.Replacement, Day14.Replacement>("14") {
    data class Replacement(val template: String, val mapping: Map<String, String>)

    override fun dataStar1(lines: List<String>) =
        Replacement(lines.first(), lines.drop(1).associate { it.strings("->").let { it[0] to it[1] } })

    override fun dataStar2(lines: List<String>) = dataStar1(lines)

    fun Map<String, Long>.next(data: Replacement) =
        this.entries
            .flatMap { (pair, v) ->
                listOf(
                    (pair[0] + data.mapping[pair]!!) to v,
                    (data.mapping[pair]!! + pair[1]) to v
                )
            }
            .groupingBy { it.first }
            .fold(0L) { sum, (_, v) -> sum + v }

    fun Map<String, Long>.charCounts(data: Replacement) =
        this.entries
            .groupingBy { it.key[0] }
            .fold(0L) { sum, (_, v) -> sum + v }
            .toMutableMap()
            .also { it[data.template.last()] = 1 + (it[data.template.last()] ?: 0) }

    override fun star1(data: Replacement): Any {
        var pairCounts = data.template.windowed(2).groupingBy { it }.eachCount().mapValues { (_, v) -> v.toLong() }
        repeat(10) {
            pairCounts = pairCounts.next(data)
        }
        return pairCounts.charCounts(data).values.sorted().let { it.last() - it.first() }
    }

    override fun star2(data: Replacement): Any {
        var pairCounts = data.template.windowed(2).groupingBy { it }.eachCount().mapValues { (_, v) -> v.toLong() }
        repeat(40) {
            pairCounts = pairCounts.next(data)
        }
        return pairCounts.charCounts(data).values.sorted().let { it.last() - it.first() }
    }

}

fun main() {
    Day14()()
}