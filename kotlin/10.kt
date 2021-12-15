class Day10 : Day<List<List<Char>>>("10") {
    val pairs = mapOf(
        ')' to '(',
        ']' to '[',
        '}' to '{',
        '>' to '<'
    )

    val corruptedCost = mapOf(
        ')' to 3L,
        ']' to 57L,
        '}' to 1197L,
        '>' to 25137L
    )

    val invalidCost = mapOf(
        '(' to 1,
        '[' to 2,
        '{' to 3,
        '<' to 4
    )

    override fun dataStar1(lines: List<String>) = lines.map { line -> line.toCharArray().toList() }

    override fun dataStar2(lines: List<String>) = dataStar1(lines)

    override fun star1(data: List<List<Char>>): Number {
        val corruptedEnds = mutableListOf<Char>()
        for (line in data) {
            line.fold(mutableListOf()) { acc: MutableList<Char>?, e ->
                when {
                    acc == null -> null
                    e in pairs.values -> acc.apply { add(e) }
                    acc.last() == pairs[e] -> acc.apply { removeLast() }
                    else -> {
                        corruptedEnds += e
                        null
                    }
                }
            }
        }
        return corruptedEnds.sumOf { corruptedCost[it]!! }
    }

    override fun star2(data: List<List<Char>>): Number {
        val scores = mutableListOf<Long>()
        lineLoop@ for (line in data) {
            val stack = mutableListOf<Char>()
            for (ch in line) {
                when (ch) {
                    in pairs.values -> stack.add(ch)
                    else -> if (stack.last() == pairs[ch]) stack.removeLast() else continue@lineLoop
                }
            }
            if (stack.isNotEmpty()) {
                scores += stack.reversed().fold(0L) { acc, e -> acc * 5 + invalidCost[e]!! }
            }
        }
        return scores.sorted()[scores.size / 2]
    }

}

fun main() {
    Day10()()
}