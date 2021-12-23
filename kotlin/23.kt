import kotlin.math.abs
import kotlin.math.sign

class Day23 : Day<Day23.Map>("23") {
    data class Room(val places: CharArray, val hallwayIndex: Int) {
        val finished
            get() = places.all {
                it == when (hallwayIndex) {
                    2 -> 'A'
                    4 -> 'B'
                    6 -> 'C'
                    8 -> 'D'
                    else -> "Hello GitHub, this is a funny text nobody needs! :D"
                }
            }

        fun copyWithChanged(i: Int, ch: Char) = copy(places = places.copyOf().also { it[i] = ch })

        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (javaClass != other?.javaClass) return false

            other as Room

            if (!places.contentEquals(other.places)) return false
            if (hallwayIndex != other.hallwayIndex) return false

            return true
        }

        override fun hashCode(): Int {
            var result = places.contentHashCode()
            result = 31 * result + hallwayIndex
            return result
        }

        override fun toString(): String = places.contentToString()
    }

    data class Map(val hallway: CharArray, val rooms: List<Room>, val cost: Int = 0) {
        val roomIndices = rooms.map { it.hallwayIndex }.toSet()

        constructor(hallway: CharArray, vararg rooms: Room) : this(hallway, rooms.toList())

        fun step(): List<Map> {
            fun newMap(letter: Char, room: Room, roomI: Int, hallwayI: Int, moveToRoom: Boolean, dist: Int) =
                Map(
                    hallway.copyOf().also { it[hallwayI] = if (moveToRoom) '.' else letter },
                    rooms.map { if (it != room) it else it.copyWithChanged(roomI, if (moveToRoom) letter else '.') },
                    cost + dist * when (letter) {
                        'A' -> 1
                        'B' -> 10
                        'C' -> 100
                        'D' -> 1000
                        else -> 69420
                    }
                )

            val possibleMaps = sequence {
                // yield all "walking out of room into hallway"
                for (room in rooms) {
                    val indexInRoom = room.places.indexOfFirst { it != '.' }
                    if (!room.finished && indexInRoom >= 0) {
                        var moves = indexInRoom + 1
                        val amphipodKind = room.places[indexInRoom]
                        var indexInHallway = room.hallwayIndex
                        // yield all walks to the left
                        while (indexInHallway > 0) {
                            indexInHallway--
                            moves++
                            if (hallway[indexInHallway] == '.') {
                                if (indexInHallway !in roomIndices) {
                                    yield(newMap(amphipodKind, room, indexInRoom, indexInHallway, false, moves))
                                }
                            } else break
                        }
                        moves = indexInRoom + 1
                        indexInHallway = room.hallwayIndex
                        // yield all walks to the right
                        while (indexInHallway < hallway.lastIndex) {
                            indexInHallway++
                            moves++
                            if (hallway[indexInHallway] == '.') {
                                if (indexInHallway !in roomIndices) {
                                    yield(newMap(amphipodKind, room, indexInRoom, indexInHallway, false, moves))
                                }
                            } else break
                        }
                    }
                }
                // yielding all "walks from hallway into target room"
                hallwayLoop@ for ((hallwayIndex, amphipodKind) in hallway.withIndex()) {
                    val targetRoom = rooms[when (amphipodKind) {
                        'A' -> 0
                        'B' -> 1
                        'C' -> 2
                        'D' -> 3
                        else -> continue@hallwayLoop
                    }]
                    val roomReadyForMoveIn = targetRoom.places.all { it == '.' || it == amphipodKind }
                    if (roomReadyForMoveIn) {
                        val indexInRoom = targetRoom.places.indexOfLast { it == '.' }
                        val targetIndex = targetRoom.hallwayIndex
                        val goRight = (targetIndex - hallwayIndex).sign > 0
                        val checkRange = if (goRight) hallwayIndex + 1..targetIndex else targetIndex..hallwayIndex - 1
                        for (stepIndex in checkRange) {
                            if (hallway[stepIndex] != '.') continue@hallwayLoop
                        }

                        val next = newMap(
                            amphipodKind,
                            targetRoom,
                            indexInRoom,
                            hallwayIndex,
                            true,
                            abs(targetIndex - hallwayIndex) + indexInRoom + 1
                        )
                        yield(next)
                    }
                }
            }

            return possibleMaps.toList()
        }


        val finished
            get() = rooms.all { it.finished }

        override fun toString() = buildString {
            hallway.forEach { append(it) }
            append(" ($cost)")
            appendLine()
            repeat(rooms[0].places.size) { l ->
                hallway.indices.forEach { i -> append(rooms.find { it.hallwayIndex == i }?.places?.get(l) ?: " ") }
                appendLine()
            }
        }

        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (javaClass != other?.javaClass) return false

            other as Map

            if (!hallway.contentEquals(other.hallway)) return false
            if (rooms != other.rooms) return false
            if (roomIndices != other.roomIndices) return false

            return true
        }

        override fun hashCode(): Int {
            var result = hallway.contentHashCode()
            result = 31 * result + rooms.hashCode()
            result = 31 * result + roomIndices.hashCode()
            return result
        }


    }

    override fun dataStar1(lines: List<String>) = Map(
        CharArray(11) { '.' },
        Room(charArrayOf('C', 'B'), 2),
        Room(charArrayOf('B', 'C'), 4),
        Room(charArrayOf('D', 'A'), 6),
        Room(charArrayOf('D', 'A'), 8)
    )

    override fun dataStar2(lines: List<String>) = Map(
        CharArray(11) { '.' },
        Room(charArrayOf('C', 'D', 'D', 'B'), 2),
        Room(charArrayOf('B', 'C', 'B', 'C'), 4),
        Room(charArrayOf('D', 'B', 'A', 'A'), 6),
        Room(charArrayOf('D', 'A', 'C', 'A'), 8)
    )

    fun firstFiveSolutions(data: Map): MutableList<Map> {
        val finished = mutableListOf<Map>()
        var maps = listOf<Map>(data)
        while (finished.size < 5) {
            maps = maps.filter { !it.finished }
                .flatMap { it.step() }
                .groupingBy { it }
                .reduce { map, acc, _ -> if (map.cost < acc.cost) map else acc }
                .values
                .toList()
            finished += maps.filter { it.finished }
        }
        return finished
    }

    override fun star1(data: Map) = firstFiveSolutions(data).minOf { it.cost }

    override fun star2(data: Map) = firstFiveSolutions(data).minOf { it.cost }
}

fun main() {
    Day23()()
}