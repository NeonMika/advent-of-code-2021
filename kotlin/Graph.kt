data class Graph<D>(val edges: List<Edge<D>>) {
    val nodes: Map<D, Node<D>> =
        edges.flatMap { listOf(Node(it.fromData), Node(it.toData)) }.distinct().associateBy { it.data }

    init {
        edges.forEach { edge ->
            edge.resolve(this)
        }
    }

    fun dijkstra(cur: Node<D>): DijkstraData<D> {
        val visited = mutableSetOf<Node<D>>()
        val distances = nodes.values.associateWith { Integer.MAX_VALUE }.toMutableMap().also { it[cur] = 0 }
        val paths = nodes.values.associateWith { mutableListOf<Edge<D>>() }.toMutableMap()
        dijkstra(
            mutableSetOf(cur),
            visited,
            distances,
            paths
        )
        return DijkstraData(visited, distances, paths)
    }

    fun dijkstra(
        seenButNotVisited: MutableSet<Node<D>>,
        visited: MutableSet<Node<D>>,
        distances: MutableMap<Node<D>, Int>,
        paths: MutableMap<Node<D>, MutableList<Edge<D>>>
    ) {
        do {
            val cur = seenButNotVisited.minByOrNull { distances[it]!! }!!
            for (e in cur.outEdges) {
                val neighbor = e.to
                if (neighbor !in visited) {
                    seenButNotVisited += neighbor
                    val possibleNewMinDist: Int = distances[cur]!! + e.weight
                    if (possibleNewMinDist < distances[neighbor]!!) {
                        distances[neighbor] = possibleNewMinDist
                        paths[neighbor]!!.clear()
                        paths[neighbor]!! += paths[cur]!!
                        paths[neighbor]!! += e
                    }
                }
            }
            visited += cur
            seenButNotVisited -= cur
            distances -= cur
        } while (visited.size < nodes.size)
    }

    override fun toString(): String = buildString {
        for (e in edges) {
            appendLine(e)
        }
    }
}

data class Node<D>(val data: D) {
    var outEdges: List<Edge<D>> = mutableListOf()
    var inEdges: List<Edge<D>> = mutableListOf()
    val undirNeighbors: List<Node<D>>
        get() = outEdges.map { it.to } + inEdges.map { it.from }

    override fun toString(): String = data.toString()
}

data class Edge<D>(val fromData: D, val toData: D, val weight: Int = 1) {
    lateinit var from: Node<D>
    lateinit var to: Node<D>

    fun opposite(node: Node<D>) =
        if (from == node) to
        else if (to == node) from
        else null

    fun resolve(graph: Graph<D>) {
        from = graph.nodes[fromData]!!
        to = graph.nodes[toData]!!

        from.outEdges += this
        to.inEdges += this
    }

    override fun toString(): String {
        return "$from -> $to [$weight]"
    }
}

data class DijkstraData<D>(
    val visited: MutableSet<Node<D>>,
    val distances: MutableMap<Node<D>, Int>,
    val paths: MutableMap<Node<D>, MutableList<Edge<D>>>
)