import java.io.File
import java.io.InputStream


fun main() {
    val inputStream: InputStream = File("./src/main/resources/day5.txt").inputStream()

    val inputText = inputStream.bufferedReader().readText().split("\\n\\n".toRegex()).toMutableList()
    val seeds = inputText.removeFirst().removePrefix("seeds: ").split(' ').map { it.toLong() }
    val maps = inputText
        .map {
            it.lines()
                .asSequence()
                .filterIndexed { index, s -> index != 0 && s.length > 0 }
                .map {line ->
                    val parts = line.split(' ')
                    val destinationStart = parts[0].toLong()
                    val sourceStart = parts[1].toLong()
                    val rangeLength = parts[2].toLong() - 1 // Ranges are inclusive
                    LongRange(sourceStart, sourceStart + rangeLength) to destinationStart
                }
                .toList()
        }

    println(
        seeds.asSequence()
            .map {seed ->
                seed_location(seed, maps)
            }
            .min()
    )
}

fun seed_location(
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