package pl.simplecoding.aoc

import java.util.*
import kotlin.math.pow

fun main() {
    Day04a.solve(readInput("Day04")).println()
    Day04b.solve(readInput("Day04")).println()
}

abstract class Day04 {
    abstract fun solve(input: List<String>): Long

    fun toCard(line: String): Card {
        val parts = line.split(":")
        val cardNumber = parts[0].trim()
            .substringAfter("Card")
            .trim()
            .toInt()

        val numberLists = parts[1].split("|")
        val winningNumbers = numberLists[0].trim().split(" ")
            .filter { it.isNotEmpty() }
            .map { it.toInt() }
        val cardNumbers = numberLists[1].trim().split(" ")
            .filter { it.isNotEmpty() }
            .map { it.toInt() }

        return Card(cardNumber, winningNumbers, cardNumbers)
    }
}

object Day04a : Day04() {
    override fun solve(input: List<String>) = input.map { toCard(it) }
        .sumOf { it.pointsForGame { count -> 2.0.pow(count - 1).toInt() } }
        .toLong()
}

object Day04b : Day04() {
    override fun solve(input: List<String>): Long {
        val cardNumberToWinningCards = input
            .map { toCard(it) }
            .associate { it.number to it.otherWinningCardGames() }

        var winningsCardsCount = 0L
        val queue = LinkedList<Int>()
        cardNumberToWinningCards.forEach { (cardNumber, _) -> queue.add(cardNumber) }

        do {
            val cardNr = queue.remove()
            winningsCardsCount++

            val winningCardGames = cardNumberToWinningCards[cardNr]
            winningCardGames
                ?.filter { it in cardNumberToWinningCards }
                ?.forEach { queue.add(it) }

        } while (queue.isNotEmpty())

        return winningsCardsCount
    }
}

data class Card(
    val number: Int,
    val winningNumbers: List<Int>,
    val cardNumbers: List<Int>
) {
    fun pointsForGame(pointCountingRule: (Int) -> Int) = pointCountingRule(winNumbersCount())

    fun otherWinningCardGames(): List<Int> {
        val winNumbersCount = winNumbersCount()
        return if (winNumbersCount == 0) listOf() else (1..winNumbersCount).map { number + it }
    }

    private fun winNumbersCount() = cardNumbers
        .count { it in winningNumbers }
}