import java.io.File
import java.io.InputStream


fun main() {
    val intRegex = "([\\d ]+)".toRegex()
    val inputStream: InputStream = File("./src/main/resources/day6.txt").inputStream()

    val inputText = inputStream.bufferedReader().readText().lines().toList()
    val timeDistance = intRegex.findAll(inputText[0])
        .map { it.value.replace(" ", "").toLong() }
        .zip(
            intRegex.findAll(inputText[1]).map { it.value.replace(" ", "").toLong() }
        )
        .map { possibleHoldTimesToBeat(it).count() }
        .reduce { acc, i -> acc * i }

    println(timeDistance)
}

private fun possibleHoldTimesToBeat(it: Pair<Long, Long>): LongRange {
    // Quadratic math would be faster
    val time = it.first
    val currentRecord = it.second

    val minTimeToBeat = (0..time).first { distance(it, time) > currentRecord }
    val maxTimeToBeat =  (minTimeToBeat..time).first { distance(it, time) <= currentRecord } - 1

    return minTimeToBeat .. maxTimeToBeat
}

private fun distance(holdTime: Long, totalTime: Long): Long {
    return (totalTime - holdTime) * holdTime
}
