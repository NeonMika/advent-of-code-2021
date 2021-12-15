class Day5 :
    Day<List<Pair<Day5.Point, Day5.Point>>>("05") {
    // Classes
    data class Point(val x: Int, val y: Int) : Comparable<Point> {
        operator fun rangeTo(other: Point) = sequence {
            var cur = this@Point
            yield(cur)
            while (cur != other) {
                cur = Point(cur.x + (other.x - cur.x).coerceIn(-1..1), cur.y + (other.y - cur.y).coerceIn(-1..1))
                yield(cur)
            }
        }

        fun sameX(otherPoint: Point) = x == otherPoint.x
        fun sameY(otherPoint: Point) = y == otherPoint.y

        companion object Factory {
            fun fromString(str: String) = str.split(",").run { Point(get(0).toInt(), get(1).toInt()) }
        }

        override operator fun compareTo(other: Point): Int = when {
            !sameX(other) -> x - other.x
            else -> y - other.y
        }
    }

    // Data
    fun List<String>.data() =
        map { it.split("->").map(String::trim).map(Point.Factory::fromString) }.map { it[0] to it[1] }

    override fun dataStar1(lines: List<String>) = lines.data()
    override fun dataStar2(lines: List<String>) = lines.data()

    fun work(data: List<Pair<Point, Point>>, pairFilter: (Pair<Point, Point>) -> Boolean) =
        data.filter(pairFilter)
            .flatMap { (from, to) -> (from..to).toList() }
            .groupingBy { it }.eachCount()
            .count { (_, v) -> v >= 2 }

    override fun star1(data: List<Pair<Point, Point>>): Number =
        work(data) { it.first.sameX(it.second) || it.first.sameY(it.second) }

    override fun star2(data: List<Pair<Point, Point>>): Number = work(data) { true }
}

fun main() {
    Day5()()
}