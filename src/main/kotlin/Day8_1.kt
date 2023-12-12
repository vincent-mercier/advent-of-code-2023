import java.io.File
import java.io.InputStream

fun directionsSequence(directions: String): Sequence<Char> = sequence {
    var curPos = 0
    while (true) {
        yield(directions[curPos])
        if (++curPos == directions.length) curPos = 0
    }
}

fun main() {
    val directionRegex = "(?<key>[A-Z]+) = \\((?<left>[A-Z]+), (?<right>[A-Z]+)\\)".toRegex()
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

    var currentPos = "AAA"
    var steps: Long = 0
    val directionsIterator = directionsSequence(directions).iterator()

    while (currentPos != "ZZZ") {
        steps++
        currentPos = directionMap[currentPos]!![directionsIterator.next()]!!
    }

    println(steps)
}
