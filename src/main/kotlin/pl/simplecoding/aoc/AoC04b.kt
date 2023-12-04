package pl.simplecoding.aoc

import java.util.*

fun main(args: Array<String>) {
    val fileContent = AoCUtils.getFile(if (args.isNotEmpty()) args[0] else "aoc04b.txt")
    AoC04b.solve(fileContent).also { println(it) }
}

object AoC04b {
    fun solve(fileContent: String): Int {
        val cardNumberToWinningCards =
            fileContent.lines()
                .map { toCard(it) }
                .associate { it.number to it.otherWinningCardGames() }

        var winningsCards = 0
        val queue = LinkedList<Int>()
        cardNumberToWinningCards.forEach { (cardNumber, _) -> queue.add(cardNumber) }

        do {
            val cardNr = queue.remove()
            winningsCards++

            val winningCardGames = cardNumberToWinningCards[cardNr]
            winningCardGames
                ?.filter { it in cardNumberToWinningCards }
                ?.forEach { queue.add(it) }

        } while (queue.isNotEmpty())

        return winningsCards
    }

    private fun toCard(line: String): Card {
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

    data class Card(
        val number: Int,
        val winningNumbers: List<Int>,
        val cardNumbers: List<Int>
    ) {
        fun otherWinningCardGames(): List<Int> {
            val winningNumbersFound = cardNumbers
                .count { it in winningNumbers }

            if (winningNumbersFound == 0) {
                return listOf()
            }

            val result = mutableListOf<Int>()
            for (i in 1..winningNumbersFound) {
                result.add(number + i)
            }

            return result
        }
    }
}