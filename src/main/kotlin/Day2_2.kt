import java.io.File
import java.io.InputStream
import kotlin.streams.asSequence

fun main() {
    val inputStream: InputStream = File("./src/main/resources/day2.txt").inputStream()

    val colorRegex = "((?<nbGem>\\d+) (?<color>red|green|blue),? ?)".toRegex()

    println(
        inputStream.bufferedReader().lines()
            .asSequence()
            .map {
                val pieces = it.split(":")
                val gemMatches = colorRegex.findAll(pieces[1])

                gemMatches
                    .map {gemMatch ->
                        val color = gemMatch.groups["color"]!!.value
                        val nbGems = gemMatch.groups["nbGem"]!!.value.toInt()

                        color to nbGems
                    }
                    .groupBy { it.first }
                    .map { it.value.maxBy { it.second }.second }
                    .reduce { acc, i -> acc * i }
            }
            .sum()
    )
}