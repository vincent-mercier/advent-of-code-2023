import java.io.File
import java.io.InputStream
import kotlin.streams.asSequence

fun main() {
    val inputStream: InputStream = File("./src/main/resources/day2.txt").inputStream()

    val max = mapOf(
        "red" to 12,
        "green" to 13,
        "blue" to 14
    )

    val colorRegex = "((?<nbGem>\\d+) (?<color>red|green|blue),? ?)".toRegex()
    val gameRegex = "Game (?<gameId>\\d+)".toRegex()

    println(
        inputStream.bufferedReader().lines()
            .asSequence()
            .map {
                val pieces = it.split(":")
                val gameIdMatch = gameRegex.find(pieces.first())
                val gameId = gameIdMatch!!.groups["gameId"]!!.value.toInt()

                val gemMatches = colorRegex.findAll(pieces[1])

                return@map gameId to gemMatches
                    .all {gemMatch ->
                        max[gemMatch.groups["color"]!!.value]!! >= gemMatch.groups["nbGem"]!!.value.toInt()
                    }
            }
            .filter { it.second }
            .sumOf { it.first }
    )
}