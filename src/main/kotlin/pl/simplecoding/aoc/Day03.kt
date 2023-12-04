package pl.simplecoding.aoc

fun main() {
    Day03a.solve(readInput("Day03")).println()
    Day03b.solve(readInput("Day03")).println()
}

abstract class Day03 {
    abstract fun solve(input: List<String>): Long

    fun getAllSymbols(line: String, supportGears: Boolean): List<Symbol<*>> {
        var currentNumber = ""
        var currentNumberFirstIdx = 0
        val symbols = mutableListOf<Symbol<*>>()

        for (i in line.indices) {
            when {
                line[i].isDigit() -> {
                    if (currentNumber.isEmpty()) {
                        currentNumberFirstIdx = i
                    }
                    currentNumber += line[i]

                    if (i == line.length - 1) {
                        symbols.add(
                            Number(
                                currentNumber.toInt(),
                                currentNumberFirstIdx,
                                currentNumberFirstIdx + currentNumber.length - 1
                            )
                        )
                    }
                }

                else -> {
                    if (line[i] == '*' && supportGears) {
                        symbols.add(Gear(i))
                    } else if (line[i] != '.') {
                        symbols.add(Sign(line[i], i))
                    }

                    if (currentNumber.isNotEmpty()) {
                        symbols.add(
                            Number(
                                currentNumber.toInt(),
                                currentNumberFirstIdx,
                                currentNumberFirstIdx + currentNumber.length - 1
                            )
                        )
                        currentNumber = ""
                    }
                }
            }
        }

        return symbols
    }
}

object Day03a : Day03() {
    override fun solve(input: List<String>): Long {
        val symbolsLines = input
            .mapIndexed { lineNr, lineValue -> lineNr to getAllSymbols(lineValue, false) }
            .toMap()

        return symbolsLines.map { lineEntry ->
            lineEntry.value.filterIsInstance<Number>()
                .filter {
                    isAnySymbolAdjacent(
                        it,
                        getSignsOnly(symbolsLines[lineEntry.key - 1]),
                        getSignsOnly(lineEntry.value),
                        getSignsOnly(symbolsLines[lineEntry.key + 1])
                    )
                }.sumOf { it.value }
        }
            .sum()
            .toLong()
    }

    private fun getSignsOnly(symbolsLine: List<Symbol<*>>?) = symbolsLine?.filterIsInstance<Sign>() ?: listOf()

    private fun isAnySymbolAdjacent(
        number: Number,
        previousLineSigns: List<Sign>,
        currentLineSigns: List<Sign>,
        nextLineSigns: List<Sign>
    ): Boolean {
        val currentLineIndexesValid =
            number.firstIndex - 1..number.lastIndex + 1 step (number.lastIndex - number.firstIndex + 2) // first left and right to the number
        val otherLinesIndexesValid = number.firstIndex - 1..number.lastIndex + 1 // all above plus diagonals

        return previousLineSigns.map { it.firstIndex }.any { it in otherLinesIndexesValid }
                || currentLineSigns.map { it.firstIndex }.any { it in currentLineIndexesValid }
                || nextLineSigns.map { it.firstIndex }.any { it in otherLinesIndexesValid }
    }
}

object Day03b : Day03() {
    override fun solve(input: List<String>): Long {
        val symbolsLines = input
            .mapIndexed { lineNr, lineValue -> lineNr to getAllSymbols(lineValue, true) }
            .toMap()

        return symbolsLines.map { lineEntry ->
            lineEntry.value.filterIsInstance<Gear>().sumOf {
                getGearMultipliedValue(
                    it,
                    getNumbersOnly(symbolsLines[lineEntry.key - 1]),
                    getNumbersOnly(lineEntry.value),
                    getNumbersOnly(symbolsLines[lineEntry.key + 1])
                )
            }
        }
            .sum()
    }

    private fun getNumbersOnly(symbolsLine: List<Symbol<*>>?) = symbolsLine?.filterIsInstance<Number>() ?: listOf()

    private fun getGearMultipliedValue(
        gear: Gear,
        previousLineNumbers: List<Number>,
        currentLineNumbers: List<Number>,
        nextLineNumbers: List<Number>
    ): Long {
        val currentLineIndexesValid = listOf(gear.firstIndex - 1, gear.firstIndex + 1)
        val otherLinesIndexesValid = listOf(gear.firstIndex - 1, gear.firstIndex, gear.lastIndex + 1)
        val numberValues = listOf(
            previousLineNumbers
                .filter { number -> otherLinesIndexesValid.any { it in number.firstIndex..number.lastIndex } }
                .map { it.value },
            currentLineNumbers
                .filter { number -> currentLineIndexesValid.any { it in number.firstIndex..number.lastIndex } }
                .map { it.value },
            nextLineNumbers
                .filter { number -> otherLinesIndexesValid.any { it in number.firstIndex..number.lastIndex } }
                .map { it.value }
        )
            .flatten()
            .map { it.toLong() }

        return if (numberValues.size == 2) numberValues.reduce { acc, i -> acc * i } else 0
    }
}

open class Symbol<T>(
    open val value: T,
    open val firstIndex: Int,
    open val lastIndex: Int
)

data class Number(
    override val value: Int,
    override val firstIndex: Int,
    override val lastIndex: Int
) : Symbol<Int>(value, firstIndex, lastIndex)

data class Sign(
    override val value: Char,
    override val firstIndex: Int
) : Symbol<Char>(value, firstIndex, firstIndex)

data class Gear(
    override val firstIndex: Int
) : Symbol<Char>('*', firstIndex, firstIndex)
