package pl.simplecoding.aoc

fun main() {
    Day01a.solve(readInput("Day01")).println()
    Day01b.solve(readInput("Day01")).println()
}

val NUMBERS = mapOf(
    "zero" to 0,
    "one" to 1,
    "two" to 2,
    "three" to 3,
    "four" to 4,
    "five" to 5,
    "six" to 6,
    "seven" to 7,
    "eight" to 8,
    "nine" to 9,
    "0" to 0,
    "1" to 1,
    "2" to 2,
    "3" to 3,
    "4" to 4,
    "5" to 5,
    "6" to 6,
    "7" to 7,
    "8" to 8,
    "9" to 9,
)

abstract class Day01 {
    fun solve(input: List<String>) = input
        .sumOf { getLineCalibrationValue(it) }
        .also { println(it) }

    abstract fun getLineCalibrationValue(line: String): Int
}

object Day01a : Day01() {
    override fun getLineCalibrationValue(line: String): Int {
        val digits = line.filter { it.isDigit() }
        return digits.first().digitToInt() * 10 + digits.last().digitToInt()
    }
}

object Day01b : Day01() {
    override fun getLineCalibrationValue(line: String): Int {
        val digits = getDigits(line)
        return digits.first() * 10 + digits.last()
    }

    private fun getDigits(line: String): List<Int> {
        val indexToNumber = mutableMapOf<Int, Int>()

        NUMBERS.forEach { (key, value) ->
            var index = -1
            do {
                index = line.indexOf(key, index + 1)
                if (index != -1) {
                    indexToNumber[index] = value
                }
            } while (index != -1)
        }

        return indexToNumber
            .toSortedMap()
            .map { it.value }
    }
}
