import kotlin.math.abs

class Day19 : Day<List<Day19.Scanner>>("19") {
    data class Vertex(val x: Int, val y: Int, val z: Int) {
        // https://stackoverflow.com/questions/16452383/how-to-get-all-24-rotations-of-a-3-dimensional-array
        fun roll() = Vertex(x, z, -y) // Away from me (around x axis)
        fun r() = roll()
        fun turn() = Vertex(-y, x, z) // Top stays top (= around z axis)
        fun t() = turn()

        fun distanceTo(other: Vertex) = abs(other.x - x) + abs(other.y - y) + abs(other.z - z)

        operator fun plus(other: Vertex) = Vertex(x + other.x, y + other.y, z + other.z)
        operator fun minus(other: Vertex) = Vertex(x - other.x, y - other.y, z - other.z)

        val possibleRotations: List<Vertex> by lazy {
            listOf(
                this, t(), t().t(), t().t().t(),
                r(), r().t(), r().t().t(), r().t().t().t(),
                r().r(), r().r().t(), r().r().t().t(), r().r().t().t().t(),
                r().r().r(), r().r().r().t(), r().r().r().t().t(), r().r().r().t().t().t(),
                t().r(), t().r().t(), t().r().t().t(), t().r().t().t().t(),
                t().t().t().r(), t().t().t().r().t(), t().t().t().r().t().t(), t().t().t().r().t().t().t()
            )
        }
    }

    data class Scanner(val id: Int, val beacons: Set<Vertex>) {
        var address: Vertex? = null
        val rotatedBeacons: List<Set<Vertex>> by lazy {
            (0..23).map { i ->
                beacons.map { beacon -> beacon.possibleRotations[i] }.toSet()
            }
        }
    }

    override fun dataStar1(lines: List<String>): List<Scanner> {
        val scanners = mutableListOf<Scanner>()
        var withSeps = lines.map { if (it.startsWith("---")) "-" else it }
        var id = 0
        while (withSeps.isNotEmpty()) {
            withSeps = withSeps.drop(1)
            scanners += Scanner(id, withSeps.takeWhile { it != "-" }
                .map { it.ints(",").let { (x, y, z) -> Vertex(x, y, z) } }.toSet())
            withSeps = withSeps.dropWhile { it != "-" }
            id++
        }
        return scanners
    }

    fun absoluteScanners(originalScanners: List<Scanner>): List<Scanner> {

        val absoluteScanners = mutableListOf(originalScanners[0].copy().apply { address = Vertex(0, 0, 0) })
        val alreadyFixedScanners = mutableListOf(0)

        Outer@ while (absoluteScanners.size < originalScanners.size) {
            val toCheck = absoluteScanners.cross(originalScanners.filter { !alreadyFixedScanners.contains(it.id) })
            for ((absoluteReference, relativeOther) in toCheck) {
                for (rotatedRelativeOther in relativeOther.rotatedBeacons) {
                    val differenceCounts = rotatedRelativeOther
                        .flatMap { otherBeacon -> absoluteReference.beacons.map { my -> my - otherBeacon } }
                        .groupingBy { it }
                        .eachCount()
                    val offsetFromOrigin = differenceCounts.entries.find { (sameDiff, amount) -> amount == 12 }?.key
                    if (offsetFromOrigin != null) {
                        alreadyFixedScanners += relativeOther.id
                        absoluteScanners += Scanner(
                            relativeOther.id,
                            rotatedRelativeOther.map { it + offsetFromOrigin }.toSet()
                        ).apply { address = offsetFromOrigin }
                        continue@Outer
                    }
                }
            }
        }

        return absoluteScanners
    }

    override fun star1(data: List<Scanner>) = absoluteScanners(data).flatMap { it.beacons }.distinct().size

    override fun star2(data: List<Scanner>) = absoluteScanners(data).run {
        this.crossNotSelf(this).maxOf { (a, b) -> a.address!!.distanceTo(b.address!!) }
    }
}

fun main() {
    Day19()()
}