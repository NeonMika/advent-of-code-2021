import kotlin.math.max
import kotlin.math.min
import kotlin.math.sign

class Day17 : Day<Day17.Target>("17") {
    data class Target(val x: IntRange, val y: IntRange)
    data class Projectile(val xVel: Int, val yVel: Int, val x: Int = 0, val y: Int = 0) {
        fun step(): Projectile = copy(x = x + xVel, y = y + yVel, xVel = (if (xVel == 0) 0 else xVel - sign(xVel.toDouble())).toInt(), yVel = yVel - 1)
        fun hits(target: Target): Boolean = x in target.x && y in target.y
    }

    override fun dataStar1(lines: List<String>): Target {
        val (x1, x2, y1, y2) = Regex("""target area: x=(-?\d+)\.\.(-?\d+), y=(-?\d+)\.\.(-?\d+)""").matchEntire(lines[0])!!.groupValues.drop(1).map { it.toInt() }
        return Target(min(x1, x2)..max(x1, x2), min(y1, y2)..max(y1, y2))
    }

    private fun hitlists(data: Target) = (0..data.x.last).asSequence().flatMap { xVel ->
        (data.y.minOrNull()!!..1000).asSequence().map { yVel ->
            sequence {
                var cur = Projectile(xVel, yVel)
                yield(cur)
                while (cur.x < data.x.last && cur.y > data.y.minOrNull()!!) {
                    cur = cur.step()
                    yield(cur)
                }
            }
        }
    }.filter { list -> list.any { it.hits(data) } }

    override fun star1(data: Target): Any = hitlists(data).maxOf { list -> list.maxOf { it.y } }

    override fun star2(data: Target) = hitlists(data).count()
}

fun main() {
    Day17()()
}