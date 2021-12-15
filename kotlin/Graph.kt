data class Graph(val edges: List<Edge>) {
    val nodes: List<N> = edges.flatMap { listOf(Node(it.fromName), Node(it.toName)) }.distinct()
    
    init {
        edges.forEach { it.resolve(this) }
        nodes.forEach { it.resolve(this) }
    }

    data class Node(val name: String) {
        lateinit var neighbors: List<N>

        fun resolve(graph: Graph) {
            this.neighbors =
                graph.edges.filter { it.from == this || it.to == this }
                    .map { if (it.from == this) it.to else it.from }
        }
    }

    data class Edge(val fromName: String, val toName: String) {
        lateinit var from: N
        lateinit var to: N

        fun resolve(graph: Graph) {
            from = graph.nodes.find { it.name == fromName }!!
            to = graph.nodes.find { it.name == toName }!!
        }
    }
}