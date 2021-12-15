class Day12 : Day<Graph<String>>("12") {
    val Graph<String>.start get() = nodes["start"]!!
    val Graph<String>.end get() = nodes["end"]!!
    val Node<String>.isSmall get() = data[0].isLowerCase()
    val Node<String>.isBig get() = !isSmall
    fun Graph<String>.dfs(
        cur: Node<String>,
        path: List<Node<String>> = mutableListOf(),
        continuePred: Graph<String>.(Node<String>, List<Node<String>>) -> Boolean
    ): Int = when {
        cur == end -> 1
        continuePred(cur, path) -> cur.undirNeighbors.sumOf { dfs(it, path + cur, continuePred) }
        else -> 0
    }

    val bigOrNew: Graph<String>.(Node<String>, List<Node<String>>) -> Boolean =
        { node, path -> node.isBig || node !in path }

    override fun dataStar1(lines: List<String>) = Graph(lines.map { it.strings("-").run { Edge(get(0), get(1)) } })
    override fun dataStar2(lines: List<String>) = dataStar1(lines)

    override fun star1(data: Graph<String>): Number = data.dfs(data.start, continuePred = bigOrNew)
    override fun star2(data: Graph<String>): Number = data.dfs(data.start) { node, path ->
        bigOrNew(node, path) || (node != start && path.filter { it.isSmall }.run { size == distinct().size })
    }
}

fun main() {
    Day12()()
}