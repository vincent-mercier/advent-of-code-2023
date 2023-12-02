import java.io.File
import java.io.InputStream
import kotlin.streams.asSequence

fun main() {
    val inputStream: InputStream = File("./com/example/aoc2023/input/day1.txt").inputStream()

    println(
        inputStream.bufferedReader().lines()
            .asSequence()
            .map {
                val digit1 = it.first {c -> c.isDigit() }
                val digit2 = it.last {c -> c.isDigit() }

                return@map "$digit1$digit2".toInt()
            }
            .sum()
    )
}