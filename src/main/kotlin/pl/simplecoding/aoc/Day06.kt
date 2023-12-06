package pl.simplecoding.aoc

fun main() {
    Day06a.solve(readInput("Day06")).println()
    Day06b.solve(readInput("Day06")).println()
}

abstract class Day06 {

    fun solve(input: List<String>) = parseRaces(input)
        .map { getWinningCombinationsAmount(it) }
        .reduce { acc, i -> acc * i }

    abstract fun parseRaces(input: List<String>): List<Race>

    private fun getWinningCombinationsAmount(race: Race) = (0..race.time).count {
        (race.time - it) * it > race.distance
    }.toLong()
}

object Day06a : Day06() {
    override fun parseRaces(input: List<String>): List<Race> {
        val timeLine = input[0].split("\\s+".toRegex()).mapNotNull { it.toLongOrNull() }
        val distanceLine = input[1].split("\\s+".toRegex()).mapNotNull { it.toLongOrNull() }

        return List(timeLine.size) { index ->
            Race(time = timeLine[index], distance = distanceLine[index])
        }
    }
}

object Day06b : Day06() {
    private const val timePrefix = "Time:"
    private const val distancePrefix = "Distance:"

    override fun parseRaces(input: List<String>): List<Race> {
        val time = input[0].replace("\\s+".toRegex(), "").removePrefix(timePrefix).toLong()
        val distance = input[1].replace("\\s+".toRegex(), "").removePrefix(distancePrefix).toLong()

        return listOf(Race(time = time, distance = distance))
    }
}

data class Race(
    val time: Long,
    val distance: Long
)