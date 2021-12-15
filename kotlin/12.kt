typealias G = Graph
typealias N = Graph.Node
typealias E = Graph.Edge

class Day12 : Day<G, G>("12") {
    val G.start get() = nodes.find { it.name == "start" }!!
    val G.end get() = nodes.find { it.name == "end" }!!
    val N.isSmall get() = name[0].isLowerCase()
    val N.isBig get() = !isSmall
    fun G.dfs(cur: N, path: List<N> = mutableListOf(), continuePred: G.(N, List<N>) -> Boolean): Int = when {
        cur == end -> 1
        continuePred(cur, path) -> cur.neighbors.sumOf { dfs(it, path + cur, continuePred) }
        else -> 0
    }

    val bigOrNew: G.(N, List<N>) -> Boolean = { node, path -> node.isBig || node !in path }

    override fun dataStar1(lines: List<String>): G = G(lines.map { it.strings("-").run { E(get(0), get(1)) } })
    override fun dataStar2(lines: List<String>): Graph = dataStar1(lines)

    override fun star1(data: G): Number = data.dfs(data.start, continuePred = bigOrNew)
    override fun star2(data: G): Number = data.dfs(data.start) { node, path ->
        bigOrNew(node, path) || (node != start && path.filter { it.isSmall }.run { size == distinct().size })
    }
}

fun main() {
    Day12()()
}