package zerodi.adventofcode;

import java.util.*
import kotlin.collections.ArrayList

class Graph {
    private var nodes: TreeSet<String> = TreeSet()
    private var edges: HashMap<String, ArrayList<String>> = HashMap()

    fun addEdge(from: String, to: String) {
        nodes.add(from)
        nodes.add(to)

        if (edges.containsKey(from)) {
            edges[from]!!.add(to)
        } else {
            edges[from] = ArrayList()
            edges[from]!!.add(to)
        }
    }

    fun lexicographicSort(): String {
        val sorted: ArrayList<String> = ArrayList()
        val edgesCopy: HashMap<String, ArrayList<String>> = HashMap()
        edges.forEach { key, value -> edgesCopy[key] = value }

        val dependencies = edgesCopy.values.flatten().toHashSet()

        // find which roots do not have any dependencies and add them
        val roots = (nodes - dependencies)
        val pending = PriorityQueue<String>()
        pending.addAll(roots)

        while (pending.isNotEmpty()) {
            val node = pending.remove()
            sorted.add(node)

            val from = edgesCopy.remove(node)
            from?.forEach {
                if (!edgesCopy.values.flatten().contains(it)) {
                    pending.add(it)
                }
            }
        }

        return sorted.joinToString("")
    }

    fun sortForMultipleWorkers(workerCount: Int): String {
        val sorted: ArrayList<String> = ArrayList()
        val edgesCopy: HashMap<String, ArrayList<String>> = HashMap()
        edges.forEach { key, value -> edgesCopy[key] = value }

        val dependencies = edgesCopy.values.flatten().toHashSet()

        // initialize workers
        val workers = HashMap<Int, Int>()
        (0..workerCount).forEach { workers[it] = 0 }

        // roots without any dependencies
        val roots = (nodes - dependencies)
        val pending = PriorityQueue<String>()
        pending.addAll(roots)

        val currentTime = 0

        while (pending.isNotEmpty()) {
            // remove up to free worker item count
            val freeWorkers = workers.filter { (_, v) -> v == 0 }
            if (freeWorkers.isEmpty()) {
                break
            }

            val node = pending.poll()

            // pick a worker
            val key = freeWorkers.keys.random()
            workers[key] = letterValue(node)
        }

        return sorted.joinToString("")
    }

    fun letterValue(letter: String): Int {
        return letter.first().toInt() - 4
    }
}