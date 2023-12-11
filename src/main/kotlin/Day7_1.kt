import java.io.File
import java.io.InputStream

private val orderedCards = "AKQJT98765432".reversed()
private class Hand1(val cards: String): Comparable<Hand1> {
    enum class HandType(val value: Int) {
        IMPOSSIBLE(-1),
        HIGH_CARD(0),
        ONE_PAIR(1),
        TWO_PAIR(2),
        THREE_OF_A_KIND(3),
        FULL_HOUSE(4),
        FOUR_OF_A_KIND(5),
        FIVE_OF_A_KIND(6)
    }

    private val cardMap: Map<Char, Int> = cards.fold(mutableMapOf()) { acc, elem ->
        acc[elem] = acc.getOrDefault(elem, 0) + 1
        acc
    }

    fun type(): HandType {
        return when {
            this.cardMap.size == 5 -> HandType.HIGH_CARD
            this.cardMap.size == 4 -> HandType.ONE_PAIR
            this.cardMap.size == 3 -> {
                if (this.cardMap.values.any { it == 3 })
                    HandType.THREE_OF_A_KIND
                else
                    HandType.TWO_PAIR
            }
            this.cardMap.size == 2 -> {
                if (this.cardMap.values.any { it == 4 })
                    HandType.FOUR_OF_A_KIND
                else
                    HandType.FULL_HOUSE
            }
            this.cardMap.size == 1 -> HandType.FIVE_OF_A_KIND
            else -> HandType.IMPOSSIBLE
        }
    }

    override fun compareTo(other: Hand1): Int {
        val comparison = this.type().value.compareTo(other.type().value)
        if (comparison == 0) {
            for (index in this.cards.indices) {
                if (this.cards[index] != other.cards[index]) {
                    return orderedCards.indexOf(this.cards[index]).compareTo(orderedCards.indexOf(other.cards[index]))
                }
            }
            return 0
        } else {
            return comparison
        }
    }

}

fun main() {

    val cardsRegex = "(?<handCards>[$orderedCards]+) (?<bid>\\d+)".toRegex()
    val inputStream: InputStream = File("./src/main/resources/day7.txt").inputStream()

    val inputText = inputStream.bufferedReader().readLines()
    val input = inputText.asSequence()
        .map { cardsRegex.find(it) }
        .map { Hand1(it!!.groups["handCards"]!!.value) to it.groups["bid"]!!.value.toInt() }
        .sortedBy { it.first }

    println(
        input.mapIndexed { index, pair ->
            pair.second * (index + 1)
        }
            .sum()
    )
}
