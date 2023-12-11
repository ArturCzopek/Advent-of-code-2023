package pl.simplecoding.aoc

import kotlin.math.abs

fun main() {
    Day11a.solve(readInput("Day11")).println()
    Day11b.solve(readInput("Day11")).println()
}

const val EMPTY_PLACE = '.'
const val MILLION_PLACE = 'O'
const val GALAXY = '#'

abstract class Day11 {
    fun solve(input: List<String>) = with(parseGalaxyMap(input)) {
        generateDistinctPairs(this)
            .map { pair -> pair to countShortestPath(pair) }
            .sumOf { it.second }
    }

    abstract fun extendMap(input: List<String>): List<String>

    private fun parseGalaxyMap(input: List<String>): GalaxyMap {
        val map = extendMap(input)
        val galaxies = findGalaxies(map)
        return GalaxyMap(map, galaxies)
    }

    private fun generateDistinctPairs(galaxyMap: GalaxyMap): List<Pair<Galaxy, Galaxy>> {
        val pairs = mutableListOf<Pair<Galaxy, Galaxy>>()
        val range = (0..galaxyMap.galaxies.size)

        for (i in range) {
            for (j in (i + 1)..<range.last) {
                pairs.add(galaxyMap.galaxies[i] to galaxyMap.galaxies[j])
            }
        }

        return pairs
    }

    private fun countShortestPath(pair: Pair<Galaxy, Galaxy>) =
        abs(pair.first.x - pair.second.x) + abs(pair.first.y - pair.second.y).toLong()

    private fun findGalaxies(map: List<String>): List<Galaxy> {
        var galaxyNo = 0
        val galaxies = mutableListOf<Galaxy>()
        var millionsInColumn = 0
        map.forEachIndexed { yIdx, row ->
            if (row[0] == MILLION_PLACE) {
                millionsInColumn++
            } else {
                var millionsInRow = 0
                row.forEachIndexed { xIdx, sign ->
                    if (sign == GALAXY) {
                        val realX = millionsInRow * 999_999 + xIdx
                        val realY = millionsInColumn * 999_999 + yIdx
                        galaxies.add(Galaxy(galaxyNo, realX, realY))
                        galaxyNo++
                    }

                    if (sign == MILLION_PLACE) {
                        millionsInRow += 1
                    }
                }
            }
        }

        return galaxies.toList()
    }
}

object Day11a : Day11() {
    override fun extendMap(input: List<String>): List<String> {
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

object Day11b : Day11() {
    override fun extendMap(input: List<String>): List<String> {
        val xSize = input[0].length
        val ySize = input.size

        val copyRowsIdx = mutableListOf<Int>()
        input.forEachIndexed { idx, row ->
            if (row.count { it == EMPTY_PLACE } == xSize) {
                copyRowsIdx.add(idx)
            }
        }
        val copyColumnIdx = (0..<xSize).filter {
            input.map { line -> line[it] }.count { it == EMPTY_PLACE || it == MILLION_PLACE } == ySize
        }

        val newMap = mutableListOf<String>()
        input.forEachIndexed { idx, line ->
            newMap.add(line)
            if (idx in copyRowsIdx) {
                newMap[idx] = line.replace(EMPTY_PLACE, MILLION_PLACE)
            }
        }

        newMap.forEachIndexed { rowIdx, row ->
            val updatedRow = row.toMutableList()
            copyColumnIdx.forEach { columnIdx ->
                updatedRow[columnIdx] = MILLION_PLACE
                newMap[rowIdx] = updatedRow.joinToString(separator = "")
            }
        }
        return newMap.toList()
    }

}

data class Galaxy(
    val id: Int,
    val x: Int,
    val y: Int
)

data class GalaxyMap(
    val map: List<String>,
    val galaxies: List<Galaxy>
)
