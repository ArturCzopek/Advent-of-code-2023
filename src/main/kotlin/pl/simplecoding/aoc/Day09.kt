package pl.simplecoding.aoc

fun main() {
    Day09a.solve(readInput("Day09")).println()
//    Day09b.solve(readInput("Day09")).println()
}

abstract class Day09 {
    abstract fun solve(input: List<String>): Long
}

object Day09a : Day09() {
    override fun solve(input: List<String>) = parseOasis(input)
        .map { fillQuizRows(it) }
        .map { fillLastQuizRowsDiffValues(it) }
        .sumOf { it.quizRows.first().last() }
        .toLong()

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

    private fun fillLastQuizRowsDiffValues(oasis: Oasis) = oasis.apply {
        quizRows.last() += 0
        ((quizRows.size - 2) downTo 0).forEach { idx ->
            val diff = quizRows[idx + 1].last() + quizRows[idx].last()
            quizRows[idx] += diff
        }
    }

    private fun parseOasis(input: List<String>) = input.map { line ->
        Oasis(initialNumbers = line.split(" ").map { it.toInt() })
    }
}

//object Day09b : Day09() {
//}

data class Oasis(
    val initialNumbers: List<Int>,
    val quizRows: MutableList<MutableList<Int>> = mutableListOf()
)