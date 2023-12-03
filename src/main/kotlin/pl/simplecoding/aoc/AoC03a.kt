package pl.simplecoding.aoc

fun main(args: Array<String>) {
    val fileContent = AoCUtils.getFile(if (args.isNotEmpty()) args[0] else "aoc03a.txt")
    AoC03a.solve(fileContent).also { println(it) }
}

object AoC03a {

    fun solve(fileContent: String): Int {
        val symbolsLines = fileContent.lines()
            .mapIndexed { lineNr, lineValue -> lineNr to getAllSymbols(lineValue) }
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
                }
                .map { it.value }
                .reduceOrNull { acc, value -> acc + value }
        }
            .filterNotNull()
            .reduce { acc, value -> acc + value }
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

    private fun getAllSymbols(line: String): List<Symbol<*>> {
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
                    if (line[i] != '.') {
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