import java.io.File
import java.io.InputStream


private fun gcd(x: Long, y: Long): Long {
    return if ((y == 0L)) x else gcd(y, x % y)
}

private fun lcm(numbers: Sequence<Long>): Long {
    return numbers
        .fold(1) { x: Long, y: Long ->
            x * (y / gcd(x, y))
        }
}

fun firstZ(
    directions:Sequence<Char>,
    directionMap: Map<String, Map<Char, String>>,
    starting: String
): Long {
    var current = starting
    var steps = 0L
    for (direction in directions) {
        steps++
        current = directionMap[current]!![direction]!!
        if (current.last() == 'Z') return steps
    }

    return -1L
}

fun main() {
    val directionRegex = "(?<key>[A-Z]{3}) = \\((?<left>[A-Z]{3}), (?<right>[A-Z]{3})\\)".toRegex()
    val inputStream: InputStream = File("./src/main/resources/day8.txt").inputStream()

    val inputText = inputStream.bufferedReader().readLines()
    val directions = inputText.removeFirst()
    val directionMap = inputText.mapNotNull { directionRegex.find(it) }
        .map {
            it.groups["key"]!!.value to mapOf(
                'L' to it.groups["left"]!!.value,
                'R' to it.groups["right"]!!.value
            )
        }
        .toMap()


    var aNodes = directionMap.keys.filter { it.last() == 'A' }
    val firstZs = aNodes.asSequence().map { firstZ(directionsSequence(directions), directionMap, it) }
    var steps: Long = lcm(firstZs)

    println(steps)
}

fun containAllSame(aNodes: List<String>, startingToLastAndZs: Map<String, Pair<String, Set<Int>>>): Int? {
    val lists = aNodes.map { startingToLastAndZs[it]!!.second }
    return lists.removeFirst().firstOrNull {value -> lists.all { it.contains(value) } }
}
