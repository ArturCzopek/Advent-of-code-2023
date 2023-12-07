package pl.simplecoding.aoc


const val JOKER = 'J'
const val MAX_CARDS = 5
val cardsInOrder = listOf(JOKER, '2', '3', '4', '5', '6', '7', '8', '9', 'T', 'J', 'Q', 'K', 'A')

fun main() {
    Day07a.solve(readInput("Day07")).println()
    Day07b.solve(readInput("Day07")).println()
}

abstract class Day07 {
    abstract val isJoker: Boolean

    fun solve(input: List<String>) = parseHands(input).map {
        ComboWithHand(
            combo = findCombo(it.cards, isJoker), hand = it
        )
    }.sortedWith(ComboWithHandComparator())
        .mapIndexed { idx, comboWithHand -> (input.size - idx) to comboWithHand.hand.bid }
        .sumOf { it.first * it.second }.toLong()

    private fun parseHands(input: List<String>) = input.map {
        val parts = it.split(" ")
        Hand(parts[0], parts[1].toInt())
    }
}

object Day07a : Day07() {
    override val isJoker get() = false
}

object Day07b : Day07() {
    override val isJoker get() = true
}


fun findCombo(cards: String, withJoker: Boolean): Combo = try {
    when {
        FiveOfKind.valid(cards, withJoker) -> FiveOfKind
        FourOfKind.valid(cards, withJoker) -> FourOfKind
        FullHouse.valid(cards, withJoker) -> FullHouse
        ThreeOfKind.valid(cards, withJoker) -> ThreeOfKind
        TwoPair.valid(cards, withJoker) -> TwoPair
        OnePair.valid(cards, withJoker) -> OnePair
        HighCard.valid(cards, withJoker) -> HighCard
        else -> None
    }
} catch (ex: Exception) {
    println(cards)
    throw ex
}

data class Hand(
    val cards: String, val bid: Int
)


sealed class Combo(val order: Int, private val condition: (String, Boolean) -> Boolean) {
    fun valid(cards: String, withJoker: Boolean) = condition.invoke(cards, withJoker)

}

class ComboComparator : Comparator<Combo> {
    override fun compare(combo1: Combo, combo2: Combo) = combo1.order - combo2.order
}

fun matchJoker(withJoker: Boolean, char: Char) = (if (withJoker) char != JOKER else true)


data object FiveOfKind : Combo(1, { cards, withJoker ->
    val jokers = if (withJoker) cards.count { it == JOKER } else 0
    if (jokers >= 4) {
        true
    } else {
        val firstChar = cards.first { it != JOKER }
        cards.count { it == firstChar } == MAX_CARDS || (withJoker && cards.all { it == firstChar || it == JOKER })
    }
})

data object FourOfKind : Combo(2, { cards, withJoker ->
    val jokers = if (withJoker) cards.count { it == JOKER } else 0
    if (jokers >= 3) {
        true
    } else {
        val firstChar = cards.first { if (withJoker) it != JOKER else true }
        val secondChar = cards.first { it != firstChar && matchJoker(withJoker, it) }
        cards.count { it == firstChar } + jokers == 4 || cards.count { it == secondChar } + jokers == 4
    }
})

data object FullHouse : Combo(3, { cards, withJoker ->
    val firstChar = cards.first { if (withJoker) it != JOKER else true }
    val secondChar = cards.first { it != firstChar && matchJoker(withJoker, it) }
    cards.all { it == firstChar || it == secondChar || (if (withJoker) it == JOKER else false) }
})

data object ThreeOfKind : Combo(4, { cards, withJoker ->
    val jokers = if (withJoker) cards.count { it == JOKER } else 0
    if (jokers >= 2) {
        true
    } else {
        val firstChar = cards.first { if (withJoker) it != JOKER else true }
        val secondChar = cards.first { it != firstChar && matchJoker(withJoker, it) }
        val thirdChar = cards.first { it != firstChar && it != secondChar && matchJoker(withJoker, it) }
        cards.count { char -> char == firstChar } + jokers == 3 || cards.count { char -> char == secondChar } + jokers == 3 || cards.count { char -> char == thirdChar } + jokers == 3
    }
})


data object TwoPair : Combo(5, { cards, withJoker ->
    val charSet = HashSet<Char>()
    cards.forEach { char -> charSet.add(char) }
    charSet.size == 3 || (charSet.size == 4 && withJoker && cards.any { it == JOKER })
})

data object OnePair : Combo(6, { cards, withJoker ->
    if (withJoker && cards.any { it == JOKER }) {
        true
    } else {
        val charSet = HashSet<Char>()
        cards.forEach { char -> charSet.add(char) }
        charSet.size == 4
    }
})

data object HighCard : Combo(7, { cards, _ ->
    (0..3).all { idx -> cardsInOrder.indexOf(cards[idx]) < cardsInOrder.indexOf(cards[idx + 1]) }
})

data object None : Combo(8, { _, _ -> true })

data class ComboWithHand(
    val combo: Combo, val hand: Hand
) {
    fun mapCardsToScores() = hand.cards.map { cardsInOrder.indexOf(it) }
}

class ComboWithHandComparator : Comparator<ComboWithHand> {
    override fun compare(comboWithHand1: ComboWithHand, comboWithHand2: ComboWithHand): Int {
        val comboCompare = ComboComparator().compare(comboWithHand1.combo, comboWithHand2.combo)
        if (comboCompare != 0) {
            return comboCompare
        }

        val cards1Scores = comboWithHand1.mapCardsToScores()
        val cards2Scores = comboWithHand2.mapCardsToScores()
        (0..<MAX_CARDS).forEach {
            val cardScoreCompare = cards2Scores[it] - cards1Scores[it]
            if (cardScoreCompare != 0) {
                return cardScoreCompare
            }
        }

        return 0
    }
}
