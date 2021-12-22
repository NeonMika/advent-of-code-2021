import java.lang.Integer.min
import java.lang.Integer.max

class Day22 : Day<List<Day22.Instruction>>("22") {
    data class Instruction(val on: Boolean = true, val x: IntRange, val y: IntRange, val z: IntRange) {
        val x50 = max(x.start, -50)..min(50, x.last)
        val y50 = max(y.start, -50)..min(50, y.last)
        val z50 = max(z.start, -50)..min(50, z.last)
    }

    override fun dataStar1(lines: List<String>) =
        Regex("""(?<mode>on|off) x=(?<x1>-?\d+)..(?<x2>-?\d+),y=(?<y1>-?\d+)..(?<y2>-?\d+),z=(?<z1>-?\d+)..(?<z2>-?\d+)""").let { regex ->
            lines.map { line ->
                regex.matchEntire(line)!!.groups.run {
                    Instruction(
                        get("mode")!!.value == "on",
                        get("x1")!!.value.toInt()..get("x2")!!.value.toInt(),
                        get("y1")!!.value.toInt()..get("y2")!!.value.toInt(),
                        get("z1")!!.value.toInt()..get("z2")!!.value.toInt()
                    )
                }
            }
        }

    override fun star1(data: List<Instruction>): Any {
        val on = mutableSetOf<Triple<Int, Int, Int>>()
        data.forEach { instruction ->
            for (x in instruction.x50)
                for (y in instruction.y50)
                    for (z in instruction.z50)
                        Triple(x, y, z).let { if (instruction.on) on += it else on -= it }
        }
        return on.count()
    }

    override fun star2(data: List<Instruction>): Any {
        TODO("Not yet implemented")
    }
}

fun main() {
    Day22()()
}