import java.io.File
import java.io.InputStream
import kotlin.streams.asSequence

fun getGearRatios(input: List<Pair<List<MatchResult>, Sequence<Int>>>): List<Int> =
    input.flatMapIndexed { index: Int, pair: Pair<List<MatchResult>, Sequence<Int>> ->
        when (index) {
            0 -> {
                pair.second.map {
                    val nbs = (validInts(index, it, input[index].first) +
                            validInts(index + 1, it, input[index + 1].first))

                    if (nbs.size == 2) {
                        nbs[0].second * nbs[1].second
                    } else {
                        0
                    }
                }
            }
            input.size - 1 -> {
                pair.second.map {
                    val nbs = (validInts(index - 1, it, input[index - 1].first) +
                            validInts(index, it, input[index].first))

                    if (nbs.size == 2) {
                        nbs[0].second * nbs[1].second
                    } else {
                        0
                    }
                }
            }
            else -> {
                pair.second.map {
                    val nbs = (validInts(index - 1, it, input[index - 1].first) +
                            validInts(index, it, input[index].first) +
                            validInts(index + 1, it, input[index + 1].first))

                    if (nbs.size == 2) {
                        nbs[0].second * nbs[1].second
                    } else {
                        0
                    }
                }
            }
        }.toList()
    }

fun main() {
    val inputStream: InputStream = File("./com/example/aoc2023/input/day3.txt").inputStream()
    val intRegex = "(\\d+)".toRegex()

    val input = inputStream.bufferedReader().lines()
        .asSequence()
        .map { c ->
            val intMatches = intRegex.findAll(c).toList()
            val potentialGearIndices = c.asSequence()
                .mapIndexed { i2, c2 -> i2 to (c2 == '*') }
                .filter { it.second }
                .map { it.first }

            intMatches to potentialGearIndices
        }
        .toList()

    println(
        getGearRatios(input).sum()
    )
}