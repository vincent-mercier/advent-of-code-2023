import java.io.File
import java.io.InputStream
import kotlin.math.min
import kotlin.streams.asStream

/**
 * Takes about 90s & 1.2GB of heap on my AMD Ryzen 3600.
 * Sequential version takes almost 9 minutes.
 */
fun main() {
    val inputStream: InputStream = File("./src/main/resources/day5.txt").inputStream()

    val inputText = inputStream.bufferedReader().readText().split("\\n\\n".toRegex()).toMutableList()
    val seeds = inputText.removeFirst().removePrefix("seeds: ").split(' ').map { it.toLong() }
    val seedRanges = seeds.asSequence()
        .filterIndexed { index: Int, element: Long -> index % 2 == 0 }
        .mapIndexed { index, element -> LongRange(element, element + seeds[index * 2 + 1] - 1) }

    val maps = inputText
        .map {
            val map = it.lines()
                .asSequence()
                .filterIndexed { index, s -> index != 0 && s.length > 0 }
                .map { line ->
                    val parts = line.split(' ')
                    val destinationStart = parts[0].toLong()
                    val sourceStart = parts[1].toLong()
                    val rangeLength = parts[2].toLong() - 1
                    LongRange(sourceStart, sourceStart + rangeLength) to destinationStart
                }
                .sortedBy { it.second }
                .toMutableList()

            val rangeFirst = map.first().second
            if (rangeFirst > 0) {
                map.add(0, LongRange(0, rangeFirst - 1) to 0)
            }

            map
        }

    val absLowest = seedRanges
        .map {seedR ->
            seedR.asSequence()
                .asStream()
                .parallel()
                .map { seed ->
                    seed_location(seed, maps)
                }
                .reduce(Long.MAX_VALUE) {a, b -> min(a, b)}
        }.min()

    println(absLowest)
}
