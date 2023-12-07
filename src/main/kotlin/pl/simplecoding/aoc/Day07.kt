package pl.simplecoding.aoc


fun main() {
    Day07a.solve(readInput("Day07")).println()
//    Day07b.solve(readInput("Day07")).println()
}

val cardsInOrder = listOf('2', '3', '4', '5', '6', '7', '8', '9', 'T', 'J', 'Q', 'K', 'A')

abstract class Day07 {

    abstract fun solve(input: List<String>): Long

    fun parseHands(input: List<String>) = input.map {
        val parts = it.split(" ")
        Hand(parts[0], parts[1].toInt())
    }
}

object Day07a : Day07() {
    override fun solve(input: List<String>) = parseHands(input)
        .map {
            ComboWithHand(
                combo = findCombo(it.cards),
                hand = it
            )
        }
        .sortedWith(ComboWithHandComparator())
        .mapIndexed { idx, comboWithHand -> (input.size - idx) to comboWithHand.hand.bid }
        .sumOf { it.first * it.second }
        .toLong()
}

fun findCombo(cards: String) = when {
    FiveOfKind.valid(cards) -> FiveOfKind
    FourOfKind.valid(cards) -> FourOfKind
    FullHouse.valid(cards) -> FullHouse
    ThreeOfKind.valid(cards) -> ThreeOfKind
    TwoPair.valid(cards) -> TwoPair
    OnePair.valid(cards) -> OnePair
    HighCard.valid(cards) -> HighCard
    else -> None
}

data class Hand(
    val cards: String,
    val bid: Int
)


sealed class Combo(val order: Int, private val condition: (String) -> Boolean) {
    fun valid(cards: String) = condition.invoke(cards)
}

class ComboComparator : Comparator<Combo> {
    override fun compare(combo1: Combo, combo2: Combo) = combo1.order - combo2.order
}

data object FiveOfKind : Combo(
    1,
    {
        val firstChar = it[0]
        it.count { char -> char == firstChar } == 5
    }
)

data object FourOfKind : Combo(
    2,
    {
        val firstChar = it[0]
        val secondChar = it[1]
        it.count { char -> char == firstChar } == 4 ||
                it.count { char -> char == secondChar } == 4
    }
)

data object FullHouse : Combo(
    3,
    {
        val firstChar = it[0]
        val secondChar = it.first { other -> other != firstChar }
        (it.count { char -> char == firstChar } == 3 && it.count { char -> char == secondChar } == 2) ||
                (it.count { char -> char == firstChar } == 2 && it.count { char -> char == secondChar } == 3)
    }
)

data object ThreeOfKind : Combo(
    4,
    {
        val firstChar = it[0]
        val secondChar = it.first { other -> other != firstChar }
        val thirdChar = it.first { other -> other != firstChar && other != secondChar }
        it.count { char -> char == firstChar } == 3 ||
                it.count { char -> char == secondChar } == 3 ||
                it.count { char -> char == thirdChar } == 3
    }
)

data object TwoPair : Combo(
    5,
    {
        val charSet = HashSet<Char>()
        it.forEach { char -> charSet.add(char) }
        charSet.size == 3// if there are two pairs and 3 of pair already checked then only combo is 2 + 1 + 2 so all cards checked
    }
)

data object OnePair : Combo(
    6,
    {
        val charSet = HashSet<Char>()
        it.forEach { char -> charSet.add(char) }
        charSet.size == 4
    }
)

data object HighCard : Combo(
    7,
    {
        (0..3).all { idx -> cardsInOrder.indexOf(it[idx]) < cardsInOrder.indexOf(it[idx + 1]) }
    }
)

data object None : Combo(
    8,
    { true }
)

data class ComboWithHand(
    val combo: Combo,
    val hand: Hand
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
        (0..4).forEach {
            val cardScoreCompare = cards2Scores[it] - cards1Scores[it]
            if (cardScoreCompare != 0) {
                return cardScoreCompare
            }
        }

        return 0
    }
}
