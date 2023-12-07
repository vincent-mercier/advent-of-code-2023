import java.io.File
import java.io.InputStream
import kotlin.streams.asSequence


fun main() {
    val inputStream: InputStream = File("./src/main/resources/day4.txt").inputStream()
    val lineRegex = "(Card\\s+\\d+:)\\s+(?<winningNumbers>(\\d+\\s*)+) \\|\\s+(?<gameNumbers>(\\d+\\s*)+)".toRegex()
    val intRegex = "\\d+".toRegex()

    val winningTicketsPerLine = inputStream.bufferedReader().lines()
        .asSequence()
        .mapIndexed {i: Int, it: String ->
            val match = lineRegex.find(it)!!.groups
            val winners = intRegex.findAll(match["winningNumbers"]!!.value)
                .map { it.value.toInt() }
                .toSet()

            i to intRegex.findAll(match["gameNumbers"]!!.value)
                .map { it.value.toInt() }
                .filter { winners.contains(it) }
                .count()
        }.toList()

    val workingSet = winningTicketsPerLine.toMutableList()
    var i = 0
    while (i < workingSet.size) {
        val ticket = workingSet[i++]
        for (i in 1 .. ticket.second) {
            workingSet.add(winningTicketsPerLine[ticket.first + i])
        }
    }

    println(workingSet.size)
}