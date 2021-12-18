class Day18 : Day<List<Day18.FishNumber>>("18") {
    class FishNumber(var value: Int, var left: FishNumber?, var right: FishNumber?, val strLen: Int) {
        var parent: FishNumber? = null
        val depth: Int
            get() = if (parent == null) 0 else parent!!.depth + 1
        val magnitude: Int
            get() = if (value > -1) value else 3 * left!!.magnitude + 2 * right!!.magnitude

        init {
            left?.parent = this
            right?.parent = this
        }

        operator fun plus(other: FishNumber) = FishNumber(-1, this, other, -1).apply { adjustAll() }

        fun leaves(): List<FishNumber> =
            if (left == null) listOf(this)
            else left!!.leaves() + right!!.leaves()

        fun adjustAll() {
            do {
                var somethingDone = tryExplodeAll()
                somethingDone = somethingDone or trySplitOnce()
            } while (somethingDone)
        }

        fun tryExplodeAll(): Boolean {
            var somethingDone = false
            while (tryExplodeOnce()) {
                somethingDone = true
            }
            return somethingDone
        }

        /*
        fun trySplitAll(): Boolean {
            var somethingDone = false
            while (trySplitOnce()) {
                somethingDone = true
            }
            return somethingDone
        }*/

        fun tryExplodeOnce(): Boolean {
            if (depth >= 4 && left != null) {
                val leaves = parent!!.parent!!.parent!!.parent!!.leaves()
                val leftIndex = leaves.indexOf(left)
                val rightIndex = leftIndex + 1
                if (leftIndex > 0)
                    leaves[leftIndex - 1].value += left!!.value
                if (rightIndex < leaves.lastIndex) {
                    leaves[rightIndex + 1].value += right!!.value
                }
                // replace with 0
                right = null
                left = null
                value = 0
                return true
            }

            if (left?.tryExplodeOnce() ?: false) return true
            if (right?.tryExplodeOnce() ?: false) return true
            return false
        }

        fun trySplitOnce(): Boolean {
            if (left == null && value >= 10) {
                left = FishNumber(value / 2, null, null, -1).also { it.parent = this }
                right = FishNumber((value + 1) / 2, null, null, -1).also { it.parent = this }
                value = -1
                return true
            }

            if (left?.trySplitOnce() ?: false) return true
            if (right?.trySplitOnce() ?: false) return true
            return false
        }


        override fun toString() = if (left != null) "[$left,$right]" else "$value"
        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (javaClass != other?.javaClass) return false

            other as FishNumber

            if (value != other.value) return false
            if (left != other.left) return false
            if (right != other.right) return false
            if (strLen != other.strLen) return false
            if (parent !== other.parent) return false

            return true
        }

        override fun hashCode(): Int {
            var result = value
            result = 31 * result + (left?.hashCode() ?: 0)
            result = 31 * result + (right?.hashCode() ?: 0)
            result = 31 * result + strLen
            result = 31 * result + (parent?.hashCode() ?: 0)
            return result
        }

        companion object {
            fun fromString(str: String): FishNumber {
                var read = 0
                var rem = str

                fun next(i: Int = 1): String {
                    read += i
                    val r = rem.take(i)
                    rem = rem.drop(i)
                    return r
                }

                fun la(i: Int = 1): String = rem.take(i)

                next() //[
                var ch = la()
                val left = when (ch) {
                    "[" -> fromString(rem).apply { next(strLen) }
                    else -> FishNumber(next().toInt(), null, null, 1)
                }
                next() //,
                ch = la()
                val right = when (ch) {
                    "[" -> fromString(rem).apply { next(strLen) }
                    else -> FishNumber(next().toInt(), null, null, 1)
                }
                next() // ]
                return FishNumber(-1, left, right, read)
            }
        }
    }

    override fun dataStar1(lines: List<String>): List<FishNumber> = lines.map(FishNumber::fromString)

    override fun star1(data: List<FishNumber>) = data.reduce { a, b -> (a + b).also(::println) }.magnitude

    override fun star2(data: List<FishNumber>) = data.crossNotSelf(data)
        .maxOf { (a, b) -> (FishNumber.fromString(a.toString()) + FishNumber.fromString(b.toString())).magnitude }
}

fun main() {
    Day18()()
}