@file:OptIn(ExperimentalStdlibApi::class)

import java.lang.Integer.min
import java.lang.Integer.max

class Day22 : Day<List<Day22.Instruction>>("22") {
    data class Cube(val x: IntRange, val y: IntRange, val z: IntRange) {
        val x50 = max(x.first, -50)..min(50, x.last)
        val y50 = max(y.first, -50)..min(50, y.last)
        val z50 = max(z.first, -50)..min(50, z.last)
        val volume get() = 1L * (x.last - x.first + 1) * (y.last - y.first + 1) * (z.last - z.first + 1)

        operator fun minus(other: Cube): List<Cube> {
            val x1 = x.first
            val x2 = max(x.first, other.x.first)
            val x3 = min(x.last, other.x.last)
            val x4 = x.last
            val y1 = y.first
            val y2 = max(y.first, other.y.first)
            val y3 = min(y.last, other.y.last)
            val y4 = y.last
            val z1 = z.first
            val z2 = max(z.first, other.z.first)
            val z3 = min(z.last, other.z.last)
            val z4 = z.last

            // Overlap
            if (x2 <= x3 && y2 <= y3 && z2 <= z3) {
                val removedCube = Cube(x2..x3, y2..y3, z2..z3)
                // Create all 3x3x3-1=26 subcubes around removed cube (filtering non-existing ones)
                return buildList {
                    for (xRange in listOf(x1 until x2, x2..x3, (x3 + 1)..x4))
                        for (yRange in listOf(y1 until y2, y2..y3, (y3 + 1)..y4))
                            for (zRange in listOf(z1 until z2, z2..z3, (z3 + 1)..z4))
                                add(Cube(xRange, yRange, zRange))
                }.filter { ol -> ol.x.last >= ol.x.first && ol.y.last >= ol.y.first && ol.z.last >= ol.z.first } - removedCube
            }
            return listOf(this)
        }

        override fun toString() = "($x, $y, $z ($volume))"
    }

    data class Instruction(val on: Boolean = true, val cube: Cube)

    override fun dataStar1(lines: List<String>) =
        Regex("""(?<mode>on|off) x=(?<x1>-?\d+)..(?<x2>-?\d+),y=(?<y1>-?\d+)..(?<y2>-?\d+),z=(?<z1>-?\d+)..(?<z2>-?\d+)""").let { regex ->
            lines.map { line ->
                regex.matchEntire(line)!!.groups.run {
                    Instruction(
                        get("mode")!!.value == "on",
                        Cube(
                            get("x1")!!.value.toInt()..get("x2")!!.value.toInt(),
                            get("y1")!!.value.toInt()..get("y2")!!.value.toInt(),
                            get("z1")!!.value.toInt()..get("z2")!!.value.toInt()
                        )
                    )
                }
            }
        }

    override fun star1(data: List<Instruction>): Any? {
        val on = mutableSetOf<Triple<Int, Int, Int>>()
        data.forEach { instruction ->
            for (x in instruction.cube.x50)
                for (y in instruction.cube.y50)
                    for (z in instruction.cube.z50)
                        Triple(x, y, z).let { if (instruction.on) on += it else on -= it }
        }
        return on.count()
    }

    override fun star2(data: List<Instruction>): Any? {
        var cubes = listOf<Cube>()
        for (instruction in data) {
            cubes = cubes.flatMap { it - instruction.cube }.toMutableList().apply {
                if (instruction.on)
                    add(instruction.cube)
            }
        }
        return cubes.sumOf { it.volume }
    }
}

fun main() {
    Day22()()
}