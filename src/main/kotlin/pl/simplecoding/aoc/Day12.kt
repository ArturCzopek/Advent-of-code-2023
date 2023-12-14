package pl.simplecoding.aoc

import java.util.*

const val OPERATIONAL = '.'
const val DAMAGED = '#'
const val UNKNOWN = '?'

fun main() {
    Day12a.solve(readInput("Day12")).println()
    Day12b.solve(readInput("Day12")).println()
}

abstract class Day12 {
    abstract val unfoldMap: Boolean

    abstract fun solve(input: List<String>): Long

    internal fun parseSprings(input: List<String>) = input.map { line ->
        val parts = line.split(" ", limit = 2)
        val map = if (unfoldMap) (0..<5).joinToString(UNKNOWN.toString()) { parts.first() } else parts.first()
        val groups = unfoldGroupsIfNeeded(parts[1].split(",").map { it.toInt() })
        Spring(map, groups)
    }

    private fun unfoldGroupsIfNeeded(groups: List<Int>): List<Int> {
        val unfoldGroups = mutableListOf<Int>()
        repeat(if (unfoldMap) 5 else 1) {
            unfoldGroups.addAll(groups)
        }
        return unfoldGroups.toList()
    }

    internal fun isValidForGroup(map: String, groupSizes: List<Int>): Boolean {
        val groups = map.split(OPERATIONAL).filter { it.all { char -> char == DAMAGED } && it.isNotEmpty() }
        if (groups.size != groupSizes.size) {
            return false
        }
        return groups.mapIndexed { index, group -> groupSizes[index] to group }
            .all { it.first == it.second.length }
    }
}

object Day12a : Day12() {
    override fun solve(input: List<String>) = parseSprings(input).sumOf { spring ->
        val currentDamaged = spring.map.count { it == DAMAGED }
        val currentOperational = spring.map.count { it == OPERATIONAL }
        val sumDamaged = spring.groupSizes.sum()
        val sumOperational = spring.map.length - sumDamaged
        val combinedMaps =
            generateCombinationMaps(spring.map, sumDamaged - currentDamaged, sumOperational - currentOperational)
        combinedMaps.count { isValidForGroup(it, spring.groupSizes) }.toLong()
    }

    private fun fillMap(map: String, unknownSigns: Int, combination: String): String {
        var filledMap = map
        repeat(unknownSigns) { idx ->
            filledMap = filledMap.replaceFirst(UNKNOWN, combination[idx])
        }
        return filledMap
    }

    private fun generateCombinationMaps(map: String, damagedCount: Int, operationalCount: Int): List<String> {
        val result = mutableListOf<String>()
        val unknownSigns = damagedCount + operationalCount
        generateCombinationsHelper(map, unknownSigns, damagedCount, operationalCount, "", result)
        return result.toList()
    }

    private fun generateCombinationsHelper(
        map: String,
        unknownSigns: Int,
        damagedCount: Int,
        operationalCount: Int,
        current: String,
        mapResult: MutableList<String>
    ) {
        if (damagedCount == 0 && operationalCount == 0) {
            mapResult.add(fillMap(map, unknownSigns, current))
        }

        if (damagedCount > 0) {
            generateCombinationsHelper(
                map,
                unknownSigns,
                damagedCount - 1,
                operationalCount,
                current + DAMAGED,
                mapResult
            )
        }

        if (operationalCount > 0) {
            generateCombinationsHelper(
                map,
                unknownSigns,
                damagedCount,
                operationalCount - 1,
                current + OPERATIONAL,
                mapResult
            )
        }
    }

    override val unfoldMap: Boolean
        get() = false

}

object Day12b : Day12() {
    override val unfoldMap: Boolean
        get() = true

    override fun solve(input: List<String>) = parseSprings(input).sumOf { spring ->
        val puzzlePieces = spring.groupSizes.map { DAMAGED.toString().repeat(it) + OPERATIONAL }.toMutableList()
        puzzlePieces[puzzlePieces.size - 1] = puzzlePieces.last().removeSuffix(OPERATIONAL.toString())
        val placesForMissingOperationalGroups = puzzlePieces.size + 1
        val missingOperationals = spring.map.length - puzzlePieces.sumOf { it.length }
        val distributions = distributeOperationals(placesForMissingOperationalGroups, missingOperationals)
        distributions.map {
            var potentialMap = ""
            (0..<puzzlePieces.size).forEach { idx ->
                potentialMap += it[idx]
                potentialMap += puzzlePieces[idx]
            }
            potentialMap += it.last()
            potentialMap
        }
            .filter { potentialMap -> (potentialMap.indices).all { spring.map[it] == potentialMap[it] || spring.map[it] == UNKNOWN } }
            .size
            .toLong()
    }

    private fun distributeOperationals(n: Int, m: Int): List<List<String>> {
        val result = LinkedList<MutableList<String>>()
        distributeOperationalsHelper(n, m, LinkedList<String>(), result)
        return result
    }

    private fun distributeOperationalsHelper(
        n: Int,
        m: Int,
        currentDistribution: MutableList<String>,
        result: MutableList<MutableList<String>>
    ) {
        if (n == 0) {
            result.add(currentDistribution)
            return
        }

        for (operationals in 0..m) {
            currentDistribution.add(OPERATIONAL.toString().repeat(operationals))
            distributeOperationalsHelper(n - 1, m - operationals, currentDistribution, result)
            currentDistribution.removeAt(currentDistribution.size - 1)
        }
    }
}

data class Spring(
    val map: String,
    val groupSizes: List<Int>
)