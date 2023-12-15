package pl.simplecoding.aoc


const val ROUNDED = 'O'
const val CUBE = '#'
fun main() {
    Day14a.solve(readInput("Day14")).println()
    Day14b.solve(readInput("Day14")).println()
}

abstract class Day14 {

    abstract fun solve(input: List<String>): Long

    internal fun parseColumns(input: List<String>) = (0..<input[0].length)
        .map { columnIdx ->
            (input.indices)
                .map { rowIdx -> input[rowIdx][columnIdx] }.joinToString("")
        }
}

object Day14a : Day14() {
    override fun solve(input: List<String>) = parseColumns(input).sumOf { column ->
        var maxScoring = column.length
        var fullScoring = 0L
        var splits = column.split(CUBE)
        splits.forEach { split ->
            if (split.isEmpty()) {
                maxScoring -= 1
            } else {
                val rounded = split.count { it == ROUNDED }
                fullScoring += (maxScoring downTo maxScoring - rounded + 1).sumOf { it }
                maxScoring = maxScoring - split.length - 1
            }
        }
        fullScoring
    }
}

object Day14b : Day14() {
    override fun solve(input: List<String>): Long {
        TODO("Not yet implemented")
    }

}