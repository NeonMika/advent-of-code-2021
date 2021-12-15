class Day15 : Day<Graph<Pair<Int, Int>>>("15") {
    override fun dataStar1(lines: List<String>): Graph<Pair<Int, Int>> {
        val arr = TwoDimensionalArray(lines.ints(""))
        val edges = arr.map2DIndexed { row, col, _ ->
            arr.getHorizontalAndVerticalNeighbors(row, col)
                .map { neighbor -> Edge(row to col, neighbor.row to neighbor.col, arr[neighbor.row, neighbor.col]) }
        }.flatMap { it }
        val g = Graph(edges)
        return g
    }

    override fun dataStar2(lines: List<String>): Graph<Pair<Int, Int>> {
        val firstList = lines.map { line ->
            line.ints("") +
                    line.ints("").map { it + 1 } +
                    line.ints("").map { it + 2 } +
                    line.ints("").map { it + 3 } +
                    line.ints("").map { it + 4 }
        }.map { line -> line.map { if (it >= 10) it - 9 else it } }
        val list = (firstList +
                firstList.map { it.map { it + 1 } } +
                firstList.map { it.map { it + 2 } } +
                firstList.map { it.map { it + 3 } } +
                firstList.map { it.map { it + 4 } })
            .map { line -> line.map { if (it >= 10) it - 9 else it } }
        val arr = TwoDimensionalArray(list)
        val edges = arr.map2DIndexed { row, col, _ ->
            arr.getHorizontalAndVerticalNeighbors(row, col)
                .map { neighbor -> Edge(row to col, neighbor.row to neighbor.col, arr[neighbor.row, neighbor.col]) }
        }.flatMap { it }
        val g = Graph(edges)
        return g
    }

    override fun star1(data: Graph<Pair<Int, Int>>): Int {
        val path =
            data.dijkstra(data.nodes[0 to 0]!!).paths[data.nodes.values.maxByOrNull { it.data.first * 10 + it.data.second }!!]!!
        return path.sumOf { e -> e.weight }
    }

    override fun star2(data: Graph<Pair<Int, Int>>): Int {
        return star1(data)
    }
}

fun main() {
    Day15()()
}