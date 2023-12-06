import java.io.File
import java.io.InputStream
import kotlin.streams.asSequence

fun validInts(rowNb: Int, index: Int, matchResults: List<MatchResult>): List<Pair<Pair<Int, Int>, Int>> =
    matchResults
        .asSequence()
        .filter {
            it.range.contains(index)
                    || index == it.range.start - 1
                    || index == it.range.endInclusive + 1
        }
        .map { (rowNb to it.range.start) to it.value.toInt() }
        .toList()

fun main() {
    val inputStream: InputStream = File("./com/example/aoc2023/input/day3.txt").inputStream()
    val intRegex = "(\\d+)".toRegex()

    val input = inputStream.bufferedReader().lines()
        .asSequence()
        .map { c ->
            val intMatches = intRegex.findAll(c).toList()
            val symbolIndices = c.asSequence()
                .mapIndexed { i2, c2 ->
                    i2 to (!c2.isDigit() && c2 != '.')
                }
                .filter { it.second }
                .map { it.first }

            intMatches to symbolIndices
        }
        .toList()

    println(
        input.flatMapIndexed { index: Int, pair: Pair<List<MatchResult>, Sequence<Int>> ->
            when (index) {
                0 -> {
                    pair.second.flatMap {
                        validInts(index, it, input[index].first) +
                                validInts(index + 1, it, input[index + 1].first)
                    }
                }

                input.size - 1 -> {
                    pair.second.flatMap {
                        validInts(index - 1, it, input[index - 1].first) +
                                validInts(index, it, input[index].first)
                    }
                }

                else -> {
                    pair.second.flatMap {
                        validInts(index - 1, it, input[index - 1].first) +
                                validInts(index, it, input[index].first) +
                                validInts(index + 1, it, input[index + 1].first)
                    }
                }
            }
        }
            .groupBy { it.first }
            .map {
                it.value.first().second
            }
            .sum()
    )
}