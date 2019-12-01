import zerodi.adventofcode.Graph

fun main() {
    val fileContent = readResource("/example06.txt").lineSequence()
    val graph = buildGraph(fileContent)

    //part1(graph)
    part2(graph)
}

fun buildGraph(fileContent: Sequence<String>): Graph {
    val graph = Graph()

    val regex = Regex("Step (.*) must be finished before step (.*) can begin.")
    fileContent.forEach {
        val rule = regex.find(it)?.groupValues!!
        val from = rule[1]
        val to = rule[2]

        graph.addEdge(from, to)
    }

    return graph
}

fun part1(graph: Graph) {
    println(graph.lexicographicSort())
}

fun part2(graph: Graph) {
    println(graph.sortForMultipleWorkers(5))
}

fun readResource(name: String): String {
    return object {}.javaClass.getResource(name).readText()
}