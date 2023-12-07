import java.io.File
import java.io.InputStream
import kotlin.streams.asSequence

fun main() {
    val validDigits = mapOf(
        "one" to 1,
        "1" to 1,
        "two" to 2,
        "2" to 2,
        "three" to 3,
        "3" to 3,
        "four" to 4,
        "4" to 4,
        "five" to 5,
        "5" to 5,
        "six" to 6,
        "6" to 6,
        "seven" to 7,
        "7" to 7,
        "eight" to 8,
        "8" to 8,
        "nine" to 9,
        "9" to 9
    )
    val inputStream: InputStream = File("./src/main/resources/day1.txt").inputStream()
    val regexFirst = "(${validDigits.keys.joinToString("|")})".toRegex()
    val regexLast = "(${validDigits.keys.joinToString("|"){ it.reversed() }})".toRegex()

    println(
        inputStream.bufferedReader().lines()
            .asSequence()
            .map {
                val digit1 = validDigits[regexFirst.find(it)?.value]
                val digit2 = validDigits[regexLast.find(it.reversed())?.value?.reversed()]

                return@map "$digit1$digit2".toInt()
            }
            .sum()
    )
}