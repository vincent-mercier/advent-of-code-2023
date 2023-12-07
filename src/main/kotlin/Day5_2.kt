import java.io.File
import java.io.InputStream
import java.lang.RuntimeException

// Doesn't work yet, but getting close. Since it execs in 55ms, it's probably much better than the brute force.
fun main() {
    val inputStream: InputStream = File("./src/main/resources/day5.txt").inputStream()

    val inputText = inputStream.bufferedReader().readText().split("\\n\\n".toRegex()).toMutableList()
    val seeds = inputText.removeFirst().removePrefix("seeds: ").split(' ').map { it.toLong() }
    val seedRanges = seeds.asSequence()
        .filterIndexed { index: Int, element: Long -> index % 2 == 0 }
        .mapIndexed { index, element -> LongRange(element, element + seeds[index * 2 + 1] - 1) }
        .toList()

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
                    LongRange(sourceStart, sourceStart + rangeLength) to LongRange(destinationStart, destinationStart + rangeLength)
                }
                .sortedBy { it.second.first }
                .toMutableList()

            val rangeFirst = map.first().second.first
            if (rangeFirst > 0) {
                map.add(0, LongRange(0, rangeFirst - 1) to LongRange(0, rangeFirst - 1))
            }

            map.asSequence()
        }

    val bestRanges = figureOutOrderedBestRanges(maps.reversed()).toList()


    println(
        seed_location(
            bestRanges.firstNotNullOf { bestRange ->
                seedRanges.firstNotNullOfOrNull { seedRange ->
                    valuesOfIn(bestRange, seedRange)
                }?.let {
                    bestRange.first + it.first
                }
            },
            maps.map {
                it.map {
                    it.first to it.second.first
                }.toList()
            }
        )
    )
}

fun figureOutOrderedBestRanges(maps: List<Sequence<Pair<LongRange, LongRange>>>): Sequence<LongRange> {
    var bestRanges = maps.first().map { it.first }
    for (i in 1 until maps.size) {
        bestRanges = bestRanges.flatMap { getBestRangesFor(it, maps[i])}
    }
    return bestRanges
}

fun getBestRangesFor(inputRange: LongRange, nextRanges: Sequence<Pair<LongRange, LongRange>>): Sequence<LongRange> =
    nextRanges.mapNotNull { pair ->
        valuesOfIn(inputRange, pair.second)?.let {
            LongRange(pair.first.first + it.first, pair.first.first + it.last)
        }
    }

fun valuesOfIn(input: LongRange, reference: LongRange): LongRange? {
    return if (input.last < reference.first || input.first > reference.last) {
        //ref: [ ]
        //input:  [ ]
        null
    } else if (reference.contains(input.first) && reference.contains(input.last)) {
        //ref: [    ]
        //input: [ ]
        LongRange(
            input.first - reference.first,
            input.last - reference.first
        )
    } else if (reference.contains(input.first)) {
        //ref: [  ]
        //input: [  ]
        LongRange(
            input.first - reference.first,
            reference.last - reference.first
        )
    } else if (reference.contains(input.last)) {
        //ref:     [  ]
        //input: [  ]
        LongRange(
            0,
            input.last - reference.first
        )
    } else if (input.contains(reference.first) && input.contains(reference.last)) {
        //ref:    [ ]
        //input: [   ]
        LongRange(
            0,
            reference.last - reference.first
        )
    } else {
        throw RuntimeException("wat")
    }
}