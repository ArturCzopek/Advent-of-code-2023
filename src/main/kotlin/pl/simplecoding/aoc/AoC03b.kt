package pl.simplecoding.aoc

//--- Part Two ---
//The engineer finds the missing part and installs it in the engine! As the engine springs to life, you jump in the closest gondola, finally ready to ascend to the water source.
//
//You don't seem to be going very fast, though. Maybe something is still wrong? Fortunately, the gondola has a phone labeled "help", so you pick it up and the engineer answers.
//
//Before you can explain the situation, she suggests that you look out the window. There stands the engineer, holding a phone in one hand and waving with the other. You're going so slowly that you haven't even left the station. You exit the gondola.
//
//The missing part wasn't the only issue - one of the gears in the engine is wrong. A gear is any * symbol that is adjacent to exactly two part numbers. Its gear ratio is the result of multiplying those two numbers together.
//
//This time, you need to find the gear ratio of every gear and add them all up so that the engineer can figure out which gear needs to be replaced.
//
//Consider the same engine schematic again:
//
//467..114..
//...*......
//..35..633.
//......#...
//617*......
//.....+.58.
//..592.....
//......755.
//...$.*....
//.664.598..
//In this schematic, there are two gears. The first is in the top left; it has part numbers 467 and 35, so its gear ratio is 16345. The second gear is in the lower right; its gear ratio is 451490. (The * adjacent to 617 is not a gear because it is only adjacent to one part number.) Adding up all of the gear ratios produces 467835.
//
//What is the sum of all of the gear ratios in your engine schematic?

fun main(args: Array<String>) {
    val fileContent = AoCUtils.getFile(if (args.isNotEmpty()) args[0] else "aoc03b.txt")
    AoC03b.solve(fileContent).also { println(it) }
}

object AoC03b {

    fun solve(fileContent: String): Long {
        val symbolsLines = fileContent.lines()
            .mapIndexed { lineNr, lineValue -> lineNr to getAllSymbols(lineValue) }
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
                    if (line[i] == '*') {
                        symbols.add(Gear(i))
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

    data class Gear(
        override val firstIndex: Int
    ) : Symbol<Char>('*', firstIndex, firstIndex)
}

