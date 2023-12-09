package pl.simplecoding.aoc

fun main() {
    Day09a.solve(readInput("Day09")).println()
    Day09b.solve(readInput("Day09")).println()
}

abstract class Day09 {
    fun solve(input: List<String>) = parseOasis(input)
        .map { fillQuizRows(it) }
        .map { fillQuizRowsDiffValues(it) }
        .sumOf(sumSelector)
        .toLong()

    abstract val sumSelector: (Oasis) -> Int

    private fun parseOasis(input: List<String>) = input.map { line ->
        Oasis(line.split(" ").map { it.toInt() })
    }

    private fun fillQuizRows(oasis: Oasis) = oasis.apply {
        quizRows += initialNumbers.toMutableList()
        do {
            val currentRow = quizRows.last()
            val nextRow = (0..<currentRow.size - 1)
                .map { currentRow[it + 1] - currentRow[it] }
                .toMutableList()
            quizRows += nextRow
        } while (!nextRow.all { it == 0 })
    }

    abstract fun fillQuizRowsDiffValues(oasis: Oasis): Oasis

}

object Day09a : Day09() {
    override val sumSelector: (Oasis) -> Int
        get() = { it.quizRows.first().last() }

    override fun fillQuizRowsDiffValues(oasis: Oasis) = oasis.apply {
        quizRows.last() += 0
        ((quizRows.size - 2) downTo 0).forEach { idx ->
            val diff = quizRows[idx + 1].last() + quizRows[idx].last()
            quizRows[idx] += diff
        }
    }
}

object Day09b : Day09() {
    override val sumSelector: (Oasis) -> Int
        get() = { it.quizRows.first().first() }

    override fun fillQuizRowsDiffValues(oasis: Oasis) = oasis.apply {
        quizRows.last().add(0, 0)
        ((quizRows.size - 2) downTo 0).forEach { idx ->
            val diff = quizRows[idx].first() - quizRows[idx + 1].first()
            quizRows[idx].add(0, diff)
        }
    }
}

data class Oasis(
    val initialNumbers: List<Int>,
    val quizRows: MutableList<MutableList<Int>> = mutableListOf()
)
