package pl.simplecoding.aoc

import kotlin.math.abs

fun main() {
    Day11a.solve(readInput("Day11")).println()
//    Day11b.solve(readInput("Day11")).println()
}

const val EMPTY_PLACE = '.'
const val GALAXY = '#'

abstract class Day11 {
    abstract fun solve(input: List<String>): Long

    abstract fun parseGalaxyMap(input: List<String>): GalaxyMap

    internal fun generateDistinctPairs(galaxyMap: GalaxyMap): List<Pair<Galaxy, Galaxy>> {
        val pairs = mutableListOf<Pair<Galaxy, Galaxy>>()
        val range = (0..galaxyMap.galaxies.size)

        for (i in range) {
            for (j in (i + 1)..<range.last) {
                pairs.add(galaxyMap.galaxies[i] to galaxyMap.galaxies[j])
            }
        }

        return pairs
    }

    private fun findGalaxies(map: List<String>): List<Galaxy> {
        var galaxyNo = 0
        val galaxies = mutableListOf<Galaxy>()
        map.forEachIndexed { yIdx, row ->
            row.forEachIndexed { xIdx, sign ->
                if (sign == GALAXY) {
                    galaxies.add(Galaxy(galaxyNo, xIdx, yIdx))
                    galaxyNo++
                }
            }
        }

        return galaxies.toList()
    }

    private fun extendMap(input: List<String>): List<String> {
        val xSize = input[0].length
        val ySize = input.size

        val copyRowsIdx = mutableListOf<Int>()
        input.forEachIndexed { idx, row ->
            if (row.count { it == EMPTY_PLACE } == xSize) {
                copyRowsIdx.add(idx)
            }
        }
        val copyColumnIdx = (0..<xSize).filter {
            input.map { line -> line[it] }.count { it == EMPTY_PLACE } == ySize
        }

        val newMap = mutableListOf<String>()

        input.forEachIndexed { idx, line ->
            newMap.add(line)
            if (idx in copyRowsIdx) {
                newMap.add(line)
            }
        }

        newMap.forEachIndexed { rowIdx, row ->
            val updatedRow = row.toMutableList()
            copyColumnIdx.forEachIndexed { order, columnIdx ->
                updatedRow.add(columnIdx + order, EMPTY_PLACE)
                newMap[rowIdx] = updatedRow.joinToString(separator = "")
            }
        }
        return newMap.toList()
    }
}

object Day11a : Day11() {
    override fun solve(input: List<String>) = with(parseGalaxyMap(input)) {
        generateDistinctPairs(this)
            .map { pair ->  pair to countShortestPath(pair) }
            .sumOf { it.second }
    }

    override fun parseGalaxyMap(input: List<String>): GalaxyMap {
        val map = extendMap(input)
        val galaxies = findGalaxies(map)
        return GalaxyMap(map, galaxies)
    }


    private fun countShortestPath(pair: Pair<Galaxy, Galaxy>) =
        abs(pair.first.x - pair.second.x) + abs(pair.first.y - pair.second.y).toLong()
}

//object Day11b : Day11() {
//    override val sumSelector: (Oasis) -> Int
//        get() = { it.quizRows.first().first() }
//
//    override fun fillQuizRowsDiffValues(oasis: Oasis) = oasis.apply {
//        quizRows.last().add(0, 0)
//        ((quizRows.size - 2) downTo 0).forEach { idx ->
//            val diff = quizRows[idx].first() - quizRows[idx + 1].first()
//            quizRows[idx].add(0, diff)
//        }
//    }
//}

data class Galaxy(
    val id: Int,
    val x: Int,
    val y: Int
)

data class GalaxyMap(
    val map: List<String>,
    val galaxies: List<Galaxy>
)
