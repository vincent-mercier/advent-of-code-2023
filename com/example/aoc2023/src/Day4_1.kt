import java.io.File
import java.io.InputStream
import kotlin.streams.asSequence


fun main() {
    val inputStream: InputStream = File("./com/example/aoc2023/input/day4.txt").inputStream()
    val lineRegex = "(Card\\s+\\d+:)\\s+(?<winningNumbers>(\\d+\\s*)+) \\|\\s+(?<gameNumbers>(\\d+\\s*)+)".toRegex()
    val intRegex = "\\d+".toRegex()

    val sum = inputStream.bufferedReader().lines()
        .asSequence()
        .map {
            val match = lineRegex.find(it)!!.groups
            val winners = intRegex.findAll(match["winningNumbers"]!!.value)
                .map { it.value.toInt() }
                .toSet()

            intRegex.findAll(match["gameNumbers"]!!.value)
                .map { it.value.toInt() }
                .filter { winners.contains(it) }
                .count()
        }
        .filter { it > 0 }
        .map { 1 shl (it - 1) }
        .sum()

    println(sum)
}