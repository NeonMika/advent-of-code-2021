import Day16.Packet.Companion.fromString

class Day16 : Day<Day16.Packet>("16") {
    data class Packet(
        val version: Int,
        val type: Int,
        val length: Int,
        val literal: Long = -1L,
        val packets: List<Packet> = listOf()
    ) {
        val versionSum: Int
            get() = version + packets.sumOf { it.versionSum }

        val result: Long = when (type) {
            4 -> literal

            0 -> packets.sumOf { it.result }
            1 -> packets.fold(1L) { acc, e -> acc * e.result }
            2 -> packets.minOf { it.result }
            3 -> packets.maxOf { it.result }
            5 -> if (packets[0].result > packets[1].result) 1 else 0
            6 -> if (packets[0].result < packets[1].result) 1 else 0
            7 -> if (packets[0].result == packets[1].result) 1 else 0

            else -> error("Who broke my code!? :P")
        }

        companion object {
            fun fromString(str: String): Packet {
                var read = 0
                var rem = str
                fun next(i: Int): String {
                    read += i
                    val r = rem.take(i)
                    rem = rem.drop(i)
                    return r
                }

                val version = next(3).toInt(2)
                val type = next(3).toInt(2)

                var literal = -1L
                val packets = mutableListOf<Packet>()

                when (type) {
                    4 -> {
                        var litStr = ""
                        do {
                            val part = next(5)
                            val partStart = part.first()
                            litStr += part.drop(1)
                        } while (partStart == '1')
                        literal = litStr.toLong(2)
                    }
                    else -> {
                        when (next(1)) {
                            "0" -> {
                                val len = next(15).toInt(2)

                                var readSubPacketLen = 0
                                while (readSubPacketLen < len) {
                                    val subPacket = fromString(rem)
                                    readSubPacketLen += subPacket.length
                                    read += subPacket.length
                                    rem = rem.drop(subPacket.length)
                                    packets += subPacket
                                }
                            }
                            "1" -> {
                                val n = next(11).toInt(2)
                                repeat(n) {
                                    val subPacket = fromString(rem)
                                    read += subPacket.length
                                    rem = rem.drop(subPacket.length)
                                    packets += subPacket
                                }
                            }
                        }
                    }
                }
                // rem = rem.drop(4 - read % 4)
                // read += 4 - read % 4
                return Packet(version, type, read, literal, packets)
            }
        }
    }

    private val hex = mapOf(
        "0" to "0000",
        "1" to "0001",
        "2" to "0010",
        "3" to "0011",
        "4" to "0100",
        "5" to "0101",
        "6" to "0110",
        "7" to "0111",
        "8" to "1000",
        "9" to "1001",
        "A" to "1010",
        "B" to "1011",
        "C" to "1100",
        "D" to "1101",
        "E" to "1110",
        "F" to "1111"
    )

    override fun dataStar1(lines: List<String>): Packet {
        val bitString = lines[0].asIterable().joinToString("") { hex[it.toString()]!! }
        return fromString(bitString)
    }

    override fun star1(data: Packet) = data.versionSum

    override fun star2(data: Packet) = data.result
}

fun main() {
    Day16()()
}