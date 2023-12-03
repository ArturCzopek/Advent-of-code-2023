package pl.simplecoding.aoc
//
//Your calculation isn't quite right. It looks like some of the digits are actually spelled out with letters: one, two, three, four, five, six, seven, eight, and nine also count as valid "digits".
//
//Equipped with this new information, you now need to find the real first and last digit on each line. For example:
//
//two1nine
//eightwothree
//abcone2threexyz
//xtwone3four
//4nineeightseven2
//zoneight234
//7pqrstsixteen
//In this example, the calibration values are 29, 83, 13, 24, 42, 14, and 76. Adding these together produces 281.
//
//What is the sum of all of the calibration values?

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


fun main(args: Array<String>) {
    val fileContent = AoCUtils.getFile(if (args.isNotEmpty()) args[0] else "aoc01b.txt")
    AoC01b.solve(fileContent)
}

object AoC01b {
    fun solve(fileContent: String) = fileContent.lines()
        .sumOf { getLineCalibrationValue(it) }
        .also { println(it) }

    private fun getLineCalibrationValue(line: String): Int {
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
