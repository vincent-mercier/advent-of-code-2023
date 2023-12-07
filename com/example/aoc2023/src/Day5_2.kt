import java.io.File
import java.io.InputStream


fun main() {
    val inputStream: InputStream = File("./com/example/aoc2023/input/day5.txt").inputStream()

    val inputText = inputStream.bufferedReader().readText().split("\\n\\n".toRegex()).toMutableList()
    val seeds = inputText.removeFirst().removePrefix("seeds: ").split(' ').map { it.toLong() }
    val maps = inputText
        .map {
            val map = it.lines()
                .asSequence()
                .filterIndexed { index, s -> index != 0 && s.length > 0 }
                .map {line ->
                    val parts = line.split(' ')
                    val destinationStart = parts[0].toLong()
                    val sourceStart = parts[1].toLong()
                    val rangeLength = parts[2].toLong() - 1
                    LongRange(sourceStart, sourceStart + rangeLength) to destinationStart
                }
                .sortedBy { it.second }
                .toMutableList()

            if (map.first().second > 0) {
                map.add(0, LongRange(0, map.first().second - 1) to 0)
            }

            map
        }

    val bestRangesInOrder = figureOutOrderedBestRanges(maps.reversed())


    println(

    )
}

fun figureOutOrderedBestRanges(maps: List<MutableList<Pair<LongRange, Long>>>): List<LongRange> {
    var bestRanges = maps[0].map { it.first }
    for (i in 1 until maps.size) {
        val nextMap = maps[i]

        bestRanges = bestRanges.flatMap {
            nextMap.
        }
    }
}

private fun seed_location(
    seed: Long,
    maps: List<List<Pair<LongRange, Long>>>
): Long {
    var currentVal = seed
    for (map in maps) {
        currentVal = map
            .firstOrNull {
                it.first.contains(currentVal)
            }
            ?.let {
                val distance = currentVal - it.first.start
                it.second + distance
            }
            ?: currentVal
    }
    return currentVal
}