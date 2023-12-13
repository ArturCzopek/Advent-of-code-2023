package pl.simplecoding.aoc

const val OPERATIONAL = '.'
const val DAMAGED = '#'
const val UNKNOWN = '?'

fun main() {
    Day12a.solve(readInput("Day12")).println()
//    Day12b.solve(readInput("Day12")).println()
}

abstract class Day12 {
    abstract fun solve(input: List<String>): Long

    internal fun parseSprings(input: List<String>) = input.map { line ->
        val parts = line.split(" ", limit = 2)
        val map = parts.first()
        val groups = parts[1]?.split(",")?.mapNotNull { it.toInt() }.orEmpty()
        Spring(map, groups)
    }

    internal fun generateCombinations(n: Int): MutableList<String> {
        val result = mutableListOf<String>()
        generateCombinationsHelper(n, "", result)
        return result
    }

    private fun generateCombinationsHelper(n: Int, current: String, result: MutableList<String>) {
        if (n == 0) {
            result.add(current)
            return
        }

        generateCombinationsHelper(n - 1, current + OPERATIONAL, result)
        generateCombinationsHelper(n - 1, current + DAMAGED, result)
    }

}

object Day12a : Day12() {
    override fun solve(input: List<String>) = parseSprings(input).sumOf { spring ->
        val unknownSigns = spring.map.count { it == UNKNOWN }
        val combinations = generateCombinations(unknownSigns)
        val combinedMaps = fillMap(spring.map, combinations, unknownSigns)
        combinedMaps.count { isValidForGroup(it, spring.groupSizes) }.toLong()
    }

    private fun isValidForGroup(map: String, groupSizes: List<Int>): Boolean {
        val groups = map.split(OPERATIONAL).filter { it.all { char -> char == DAMAGED } && it.isNotEmpty() }
        if (groups.size != groupSizes.size) {
            return false
        }
        return groups.mapIndexed { index, group -> groupSizes[index] to group }
            .all { it.first == it.second.length }
    }

    private fun fillMap(map: String, combinations: MutableList<String>, unknownSigns: Int) =
        combinations.map {
            var filledMap = map
            repeat(unknownSigns) { idx ->
                filledMap = filledMap.replaceFirst(UNKNOWN, it[idx])
            }
            filledMap
        }
}

object Day12b : Day12() {
    override fun solve(input: List<String>): Long {
        TODO("Not yet implemented")
    }
}

data class Spring(
    val map: String,
    val groupSizes: List<Int>
)