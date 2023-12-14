import java.io.File
import java.io.InputStream

private class MirageLine2(val line: List<Long>) {
    fun calcNext(): Long {
        val sublines = mutableListOf(line)
        var currentLine = line
        while (currentLine.any { it != 0L }) {
            val nextLine = (1 until currentLine.size).map { currentLine[it] - currentLine[it - 1] }
            sublines.add(nextLine)
            currentLine = nextLine
        }

        var lastLineFirst: Long = 0
        for (i in sublines.indices.reversed()) {
            lastLineFirst = sublines[i].first() - lastLineFirst
        }

        return lastLineFirst
    }
}

fun main() {
    val inputStream: InputStream = File("./src/main/resources/day9.txt").inputStream()

    val inputText = inputStream.bufferedReader().readLines()
    val sum = inputText
        .filter { it.isNotEmpty() }
        .map {
            MirageLine2(it.split(" ")
                .map { it.toLong() }
                .toList())
        }
        .map {
            it.calcNext()
        }
        .sum()

    println(sum)
}
