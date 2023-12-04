package pl.simplecoding.aoc

import kotlin.math.max

private val gameNumberRegex = Regex("Game (\\d+):")
private val boxRegex = Regex("(\\d+)\\s+(blue|red|green)")

fun main() {
    Day02a.solve(readInput("Day02")).println()
    Day02b.solve(readInput("Day02")).println()
}

abstract class Day02 {
    abstract fun solve(input: List<String>): Int

    fun toGame(line: String): Game {
        val gameIdMatches = gameNumberRegex.find(line)
        val gameId = gameIdMatches!!.groupValues[1].toInt()

        val gameSets = line.substringAfter(":").trim().split(";")
            .map { it.trim() }
            .map {
                var blue = 0
                var red = 0
                var green = 0

                val boxesMatches = boxRegex.findAll(it)
                for (boxMatch in boxesMatches) {
                    val count = boxMatch.groupValues[1].toInt()
                    when (boxMatch.groupValues[2]) {
                        "blue" -> blue = count
                        "red" -> red = count
                        "green" -> green = count
                    }
                }
                GameSet(blue = blue, red = red, green = green)
            }

        return Game(gameId, gameSets)
    }
}

object Day02a : Day02() {
    override fun solve(input: List<String>): Int {
        val gameRule = GameRule()

        return input
            .map { toGame(it) }
            .filter { isValidWithRules(it, gameRule) }
            .sumOf { it.id }
    }

    private fun isValidWithRules(game: Game, gameRule: GameRule) = game.gameSets
        .all {
            it.blue <= gameRule.blueAllowed && it.green <= gameRule.greenAllowed && it.red <= gameRule.redAllowed
        }
}

object Day02b : Day02() {
    override fun solve(input: List<String>) = input
        .map { toGame(it) }
        .map { getMaxGameSet(it.gameSets) }
        .sumOf { it.power() }

    private fun getMaxGameSet(gameSets: List<GameSet>): GameSet {
        var blue = 0
        var red = 0
        var green = 0

        gameSets.forEach {
            blue = max(blue, it.blue)
            red = max(red, it.red)
            green = max(green, it.green)
        }

        return GameSet(blue, red, green)
    }
}

data class Game(
    val id: Int,
    val gameSets: List<GameSet>
)

data class GameRule(
    val blueAllowed: Int = 14,
    val redAllowed: Int = 12,
    val greenAllowed: Int = 13
)

data class GameSet(
    val blue: Int,
    val red: Int,
    val green: Int
) {
    fun power() = blue * red * green
}